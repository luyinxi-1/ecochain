package upc.c505.modular.ecochain.service.impl;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.asymmetric.Sign;
import cn.hutool.crypto.asymmetric.SignAlgorithm;
import cn.hutool.http.HttpUtil;
import com.alibaba.cloud.commons.lang.StringUtils;
import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import upc.c505.modular.ecochain.ecoConst.EcoChainWeChatConst;
import upc.c505.modular.ecochain.config.WxPayConfig;
import upc.c505.modular.ecochain.controller.param.EcoChainPreIdSearchParam;
import upc.c505.modular.ecochain.entity.EcoChainWechatPayment;
import upc.c505.modular.ecochain.service.IEcoChainWeChatService;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import upc.c505.modular.ecochain.service.IEcoChainWechatPaymentService;
import upc.c505.modular.ecochain.service.IEcoChainWechatPaymentService;
import upc.c505.modular.ecochain.util.GetUserInfo;
import upc.c505.modular.miniprogram.entity.MiniProgramBindEntity;
import upc.c505.modular.miniprogram.service.IMiniProgramBindService;
import upc.c505.common.UserUtils;
import upc.c505.common.UserInfoToRedis;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static upc.c505.utils.MD5Utils.md5;

@Service
@Slf4j
public class EcoChainWeChatServiceImpl implements IEcoChainWeChatService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private CloseableHttpClient httpClient;

    @Autowired
    private WxPayConfig wxPayConfig;

    @Autowired
    private IEcoChainWechatPaymentService wechatPaymentService;

    @Autowired
    private GetUserInfo getUserInfo;

    @Autowired
    private IMiniProgramBindService miniProgramBindService;

    @Override
    public String getAccessToken() {
        return getAccessToken(EcoChainWeChatConst.APP_ID, EcoChainWeChatConst.APP_SECRET);
    }

    private String getAccessToken(String appId, String appSecret) {
        String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + appId + "&secret="
                + appSecret;
        ResponseEntity<String> resultEntity = restTemplate.getForEntity(url, String.class);
        String result = resultEntity.getBody();
        log.info("result:{}", result);
        JSONObject resultJson = JSONObject.parseObject(result);
        String accessToken = resultJson.getString("access_token");
        log.info("accessToken:{}", accessToken);

        return accessToken;
    }
    @Override
    public String getPhoneNumber(String code, String appId) {
        String accessToken = null;

        // ---------------------------------------------------------
        // 逻辑变更：优先使用前端透传的 appId
        // ---------------------------------------------------------
        if (StringUtils.isNotBlank(appId)) {
            log.info("【模式A】前端显式传入 AppID: {}", appId);

            // 根据 appId 去 mini_program_bind 表获取 appSecret
            MiniProgramBindEntity bindEntity = miniProgramBindService.getByAppId(appId);

            if (bindEntity != null && StringUtils.isNotBlank(bindEntity.getAppSecret())) {
                log.info("数据库中获取到的appSecret: {}", bindEntity.getAppSecret());
                log.info("数据库查获对应 AppSecret，开始获取 AccessToken...");
                // 使用这一对匹配的 ID 和 Secret 获取 Token，绝对不会错
                accessToken = this.getAccessToken(appId, bindEntity.getAppSecret());
            } else {
                log.error("严重错误：数据库中未找到 AppID: {} 的记录或 Secret 为空！", appId);
                throw new RuntimeException("未配置该小程序的 AppSecret，请检查 mini_program_bind 表");
            }
        }
        // ---------------------------------------------------------
        // 兜底逻辑：如果前端没传 appId，尝试从 UserUtils 获取（保持旧逻辑兼容）
        // ---------------------------------------------------------
        else {
            UserInfoToRedis user = UserUtils.get();
            if (user != null && StringUtils.isNotBlank(user.getAppId())) {
                log.info("【模式B】使用用户登录绑定的 AppID: {}", user.getAppId());
                MiniProgramBindEntity bindEntity = miniProgramBindService.getByAppId(user.getAppId());
                if (bindEntity != null) {
                    accessToken = this.getAccessToken(user.getAppId(), bindEntity.getAppSecret());
                } else {
                    accessToken = this.getAccessToken(); // 默认配置
                }
            } else {
                log.info("【模式C】用户未登录/未传入参数，使用系统默认配置");
                accessToken = this.getAccessToken();
            }
        }

        log.info("----------------------------------------------");
        log.info("最终使用的 accessToken: " + accessToken);
        log.info("-----------------------------------------------");

        // 构造请求参数，注意：getuserphonenumber 只需要 code，不需要 appId
        JSONObject jsonCode = new JSONObject();
        jsonCode.put("code", code);

        // 发送请求
        String requestUrl = "https://api.weixin.qq.com/wxa/business/getuserphonenumber?access_token=" + accessToken;
        String resPhone = HttpUtil.post(requestUrl, jsonCode.toString());

        log.info("微信接口返回 resPhone: " + resPhone);

        // 校验结果
        if (StringUtils.isEmpty(resPhone)) {
            log.error("微信接口返回为空");
            return null;
        }

        // 解析 JSON
        JSONObject resPhoneInfo = JSON.parseObject(resPhone);

        // 检查是否有错误码（解决 40029/40013 的关键日志）
        if (resPhoneInfo.containsKey("errcode") && resPhoneInfo.getInteger("errcode") != 0) {
            log.error("获取手机号失败，错误码: {}, 错误信息: {}",
                    resPhoneInfo.get("errcode"), resPhoneInfo.get("errmsg"));
            // 可以选择在这里直接抛出异常，让前端看到具体错误
            // throw new RuntimeException("微信报错: " + resPhoneInfo.getString("errmsg"));
            return null;
        }

        if (!resPhoneInfo.containsKey("phone_info")) {
            log.error("返回结果中不包含 phone_info");
            return null;
        }

        JSONObject phoneInfo = resPhoneInfo.getJSONObject("phone_info");
        String phoneNumber = phoneInfo.getString("phoneNumber"); // 使用 getString 防止 NPE

        log.info("成功解析手机号: " + phoneNumber);
        return phoneNumber;
    }
/*    @Override
    public String getPhoneNumber(String code) {
        String accessToken;
        // 获取当前用户信息
        UserInfoToRedis user = UserUtils.get();
        if (user != null && StringUtils.isNotBlank(user.getAppId())) {
            // 如果用户有appId，则去mini_program_bind表中查询对应的secret
            log.info("当前模式：使用绑定 AppID = {}", user.getAppId());
            MiniProgramBindEntity bindEntity = miniProgramBindService.getByAppId(user.getAppId());
            if (bindEntity != null) {
                log.info("使用用户绑定的AppID: {}", user.getAppId());
                accessToken = this.getAccessToken(user.getAppId(), bindEntity.getAppSecret());
            } else {
                log.warn("用户绑定的AppID: {} 在mini_program_bind表中未找到记录，使用默认配置", user.getAppId());
                accessToken = this.getAccessToken();
            }
        } else {
            log.info("当前模式：用户未登录/未绑定，将使用【默认配置】的 AppID");
            log.info("用户未绑定AppID或未登录，使用默认配置");
            accessToken = this.getAccessToken();
        }
        log.info("----------------------------------------------");
        log.info("1111111111111111111111111");
        log.info("accessToken:" + accessToken);
        log.info("-----------------------------------------------");
        JSONObject jsonCode = new JSONObject();
        jsonCode.put("code", code);
        String resPhone = HttpUtil.post(
                "https://api.weixin.qq.com/wxa/business/getuserphonenumber?access_token=" + accessToken,
                jsonCode.toString());
        log.info("----------------------------------------------");
        log.info("6666666666666666666666666");
        log.info("resPhone:" + resPhone);
        log.info("-----------------------------------------------");
        if (StringUtils.isEmpty(resPhone) || !resPhone.contains("phone_info") || !resPhone.contains("phoneNumber")) {
            return null;
        }
        log.info("----------------------------------------------");
        log.info("222222222222222222222");
        log.info("resPhone:" + resPhone);
        log.info("-----------------------------------------------");
        JSONObject resPhoneInfo = JSON.parseObject(resPhone);
        log.info("----------------------------------------------");
        log.info("333333333333333333333");
        log.info("resPhoneInfo:" + resPhoneInfo);
        log.info("-----------------------------------------------");
        JSONObject phoneInfo = resPhoneInfo.getJSONObject("phone_info");
        log.info("----------------------------------------------");
        log.info("444444444444444444444");
        log.info("phoneInfo:" + phoneInfo);
        log.info("-----------------------------------------------");
        String phoneNumber = phoneInfo.get("phoneNumber").toString();
        log.info("----------------------------------------------");
        log.info("5555555555555555555555");
        log.info("phoneNumber:" + phoneNumber);
        log.info("-----------------------------------------------");
        return phoneNumber;
    }*/

    // 定义一个方法，用于获取微信支付的预支付交易会话标识（prepay_id）和签名
    @Override
    public Map<String, Object> getPreId(EcoChainPreIdSearchParam searchParam) {
        // 初始化一个HashMap，用于存储返回结果
        Map<String, Object> map = new HashMap<>();

        // 创建HttpPost对象，设置请求的URL(获取微信与支付码id的url)
        HttpPost httpPost = new HttpPost(EcoChainWeChatConst.PRE_ID_URL);
        // 添加HTTP请求头部，指定接收和发送的内容类型为JSON
        httpPost.addHeader("Accept", "application/json");
        httpPost.addHeader("Content-type", "application/json; charset=utf-8");

        // 使用ByteArrayOutputStream和ObjectMapper构建JSON请求体
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            ObjectMapper objectMapper = new ObjectMapper();

            // 创建一个ObjectNode，用于构建JSON结构
            ObjectNode rootNode = objectMapper.createObjectNode();
            String s = String.valueOf(System.currentTimeMillis() / 1000);
            // 设置请求参数
            rootNode.put("mchid", EcoChainWeChatConst.MCHID) // 商户号
                    .put("appid", EcoChainWeChatConst.APP_ID) // 小程序ID
                    .put("description", EcoChainWeChatConst.DESCRIPTION) // 商品描述
                    .put("notify_url", EcoChainWeChatConst.NOTIFY_URL) // 回调接口地址
                    .put("out_trade_no", s); // 商户订单号
            // 设置金额信息
            rootNode.putObject("amount")
                    .put("total", searchParam.getTotal()); // 总金额
            // 设置支付者信息
            rootNode.putObject("payer")
                    .put("openid", searchParam.getOpenId()); // 支付者openId

            // 将ObjectNode转换为JSON字符串，并写入ByteArrayOutputStream
            objectMapper.writeValue(bos, rootNode);
            // 设置HttpPost的请求实体为JSON字符串
            httpPost.setEntity(new StringEntity(bos.toString("UTF-8"), "UTF-8"));

            // 执行HTTP请求，获取响应
            try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
                // 获取响应体，并将其转换为字符串
                String preId = EntityUtils.toString(response.getEntity());
                // 从响应中解析出prepay_id
                String prepayId = String.valueOf(JSON.parseObject(preId).get("prepay_id"));
                // 将prepay_id放入返回结果中
                map.put("preId", preId);
                // 生成签名，并将签名也放入返回结果中
                map.put("sign", generateSignature(prepayId));
                // 放入商户订单号
                map.put("outTradeNo", s);

                // 插入订单
                EcoChainWechatPayment wechatPayment = new EcoChainWechatPayment();
                wechatPayment.setFee(searchParam.getTotal());
                wechatPayment.setOpenId(searchParam.getOpenId());
                wechatPayment.setStatus("NOTPAY");
                wechatPayment.setSocialCreditCode(getUserInfo.getSocialCreditCode());
                wechatPayment.setOutTradeNo(s);
                wechatPayment.setEndDate(searchParam.getEndDate());
                wechatPayment.setAuthorizeStatus(searchParam.getAuthorizeStatus());
                wechatPayment.setStorageCapacity(searchParam.getStorageCapacity());
                wechatPayment.setPaymentDate(searchParam.getPaymentDate());
                wechatPaymentService.save(wechatPayment);
            }
        } catch (JsonGenerationException | JsonMappingException e) {
            // 记录日志并处理JSON处理异常
            e.printStackTrace();
            map.put("error", "JSON 处理错误");
        } catch (IOException e) {
            // 记录日志并处理IO异常
            e.printStackTrace();
            map.put("error", "发生 I/O 错误");
        }

        // 返回包含prepay_id和签名的Map
        return map;
    }

    // 定义一个方法，用于生成微信支付签名
    private Map<String, Object> generateSignature(String prePayId) {
        // 获取当前时间戳
        long timeStamp = System.currentTimeMillis() / 1000;
        // 生成一个随机字符串
        String nonceStr = RandomUtil.randomString(20);
        // 构建package字符串，包含prepay_id
        String pkg = "prepay_id=" + prePayId;
        // 构建待签名字符串
        String unsignedString = EcoChainWeChatConst.APP_ID + "\n" +
                timeStamp + "\n" +
                nonceStr + "\n" +
                pkg + "\n";
        // 使用SHA256withRSA算法和商户私钥进行签名
        byte[] signByte = new Sign(SignAlgorithm.SHA256withRSA)
                .setPrivateKey(wxPayConfig.loadPrivateKey(EcoChainWeChatConst.PRIVATE_KEY_PATH)).sign(unsignedString);
        // 将签名结果进行Base64编码
        String sign = Base64.encode(signByte);
        // 初始化一个HashMap，用于存储签名结果和相关参数
        Map<String, Object> map = new HashMap<>();
        // 将签名结果和相关参数放入HashMap
        map.put("package", pkg);
        map.put("timeStamp", timeStamp);
        map.put("nonceStr", nonceStr);
        map.put("appId", EcoChainWeChatConst.APP_ID);
        map.put("sign", sign);

        // 返回包含签名的HashMap
        return map;
    }

}
