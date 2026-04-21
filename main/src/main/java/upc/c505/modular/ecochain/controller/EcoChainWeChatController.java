package upc.c505.modular.ecochain.controller;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.wechat.pay.contrib.apache.httpclient.util.AesUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import upc.c505.common.responseparam.R;
import upc.c505.modular.ecochain.ecoConst.EcoChainWeChatConst;
import upc.c505.modular.ecochain.controller.param.EcoChainPreIdSearchParam;
import upc.c505.modular.ecochain.controller.param.EcoChainWeChatPayCallbackData;
import upc.c505.modular.ecochain.entity.EcoChainEnterpriseAuthorize;
import upc.c505.modular.ecochain.entity.EcoChainWechatPayment;
import upc.c505.modular.ecochain.service.IEcoChainEnterpriseAuthorizeService;
import upc.c505.modular.ecochain.service.IEcoChainWeChatService;
import upc.c505.modular.ecochain.service.IEcoChainWechatPaymentService;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/eco-chain-wechat")
@Slf4j
@Api(tags = "微信接口")
public class EcoChainWeChatController {
    @Autowired
    private IEcoChainWeChatService ecoChainWeChatService;

    @Autowired
    private IEcoChainWechatPaymentService wechatPaymentService;

    @Autowired
    private IEcoChainEnterpriseAuthorizeService ecoChainEnterpriseAuthorizeService;

    @GetMapping("/getAccessToken")
    @ApiOperation("获取access_token")
    public R<String> getAccessToken(){
        return R.ok(ecoChainWeChatService.getAccessToken());
    }
    @PostMapping("/getPhoneNumber")
    @ApiOperation("获取电话号码")
    // 前端写法是 ?code=xx&appId=xx，所以这里必须用 @RequestParam
    public R<String> getPhoneNumber(@RequestParam String code,
                                    @RequestParam(required = false) String appId) {
        // 校验参数
        if (StringUtils.isBlank(code)) {
            return R.fail("code不能为空");
        }
        // 调用 Service，透传 appId
        return R.ok(ecoChainWeChatService.getPhoneNumber(code, appId));
    }
/*    @PostMapping("/getPhoneNumber")
    @ApiOperation("获取电话号码")
    public R<String> getPhoneNumber(@RequestParam String code){
        return R.ok(ecoChainWeChatService.getPhoneNumber(code));
    }*/

    @ApiOperation("获取支付的preId")
    @PostMapping("/getPreId")
    public R<?> getPreId(@RequestBody EcoChainPreIdSearchParam searchParam) {
        Map<String, Object> map = ecoChainWeChatService.getPreId(searchParam);
        log.info("priID和sign为:{}", map);
        return R.ok(map);
    }

    /**
     * 支付成功后的回调
     * 注意该回调是由微信服务器调用的，不是前端
     * @param param 回调参数
     */
    @PostMapping("/callback")
    public Map<String,String> callBack(@RequestBody EcoChainWeChatPayCallbackData param, HttpServletResponse response) {
        System.out.println("--------------------------------" + param);
        if (ObjectUtils.isEmpty(param)) {
            log.warn("未获取到回调参数");
            response.setStatus(500);
            return createErrorResponse();
        }
        // 解密支付数据
        String ciphertext = null;
        ciphertext = decryptOrder(param);
        log.debug("微信支付回调结果数据解密，内容为：{}", ciphertext);
        if (ObjectUtils.isEmpty(ciphertext)) {
            response.setStatus(500);
            return createErrorResponse();
        }

        // 将解密后的字符串转换为JSON格式
        JSONObject jsonObject = new JSONObject(ciphertext);

        // 获取支付状态
        String trade_state = null;
        trade_state = jsonObject.getString("trade_state");
        if (StringUtils.isBlank(trade_state)) {
            response.setStatus(500);
            return createErrorResponse();
        }
        // 根据支付状态来更新数据库中的订单信息
        if (ObjectUtils.isNotEmpty(trade_state)) {
            String out_trade_no = null;
            // 获取订单号，用于匹配数据库中存入的订单
            out_trade_no = jsonObject.getString("out_trade_no");
            EcoChainWechatPayment wechatPayment = new EcoChainWechatPayment();
            if (StringUtils.isNotBlank(out_trade_no)) {
                wechatPayment = wechatPaymentService.getOne(new LambdaQueryWrapper<EcoChainWechatPayment>()
                        .eq(EcoChainWechatPayment::getOutTradeNo, out_trade_no));
            } else {
                response.setStatus(500);
                return createErrorResponse();
            }

            if (trade_state.equals("SUCCESS")) {
                log.info("支付成功");
                wechatPayment.setStatus("SUCCESS");
                wechatPayment.setEndDatetime(LocalDateTime.now());
                String socialCreditCode = wechatPayment.getSocialCreditCode();
                EcoChainEnterpriseAuthorize one = ecoChainEnterpriseAuthorizeService.getOne(new LambdaQueryWrapper<EcoChainEnterpriseAuthorize>()
                        .eq(ObjectUtils.isNotEmpty(socialCreditCode), EcoChainEnterpriseAuthorize::getCreditCode, socialCreditCode));
                // 更新授权表信息
                if (ObjectUtils.isNotEmpty(one)) {
                    one.setAuthorizeStatus(wechatPayment.getAuthorizeStatus());
                    one.setPaymentDate(wechatPayment.getPaymentDate());
                    one.setStorageCapacity(wechatPayment.getStorageCapacity());
                    one.setEndDate(wechatPayment.getEndDate());
                }
                ecoChainEnterpriseAuthorizeService.updateById(one);
            }
            if (trade_state.equals("REFUND")) {
                log.warn("转入退款");
                wechatPayment.setStatus("REFUND");
            }
            if (trade_state.equals("NOTPAY")) {
                log.warn("未支付");
                wechatPayment.setStatus("NOTPAY");
            }
            if (trade_state.equals("PAYERROR")) {
                log.warn("支付失败");
                wechatPayment.setStatus("PAYERROR");
            }
            if (trade_state.equals("CLOSED")) {
                log.warn("已关闭");
                wechatPayment.setStatus("CLOSED");
            }
            if (trade_state.equals("REVOKED")) {
                log.warn("已撤销");
                wechatPayment.setStatus("REVOKED");
            }
            if (trade_state.equals("USERPAYING")) {
                log.warn("用户支付中");
                wechatPayment.setStatus("USERPAYING");
            }

            wechatPaymentService.updateById(wechatPayment);
        }
        response.setStatus(200);
        return createSuccessResponse();
    }

    /**
     * 创建解密失败的回复
     */
    private Map<String, String> createErrorResponse() {
        Map<String, String> response = new HashMap<>();
        response.put("code", "FAIL");
        response.put("message", "失败");
        return response;
    }
    /**
     * 创建解密成功的回复
     */
    private Map<String, String> createSuccessResponse() {
        Map<String, String> response = new HashMap<>();
        response.put("code", "SUCCESS");
        response.put("message", "成功");
        return response;
    }
    /**
     * 解密函数
     */
    private String decryptOrder(EcoChainWeChatPayCallbackData param) {
        try {
            AesUtil util = new AesUtil(EcoChainWeChatConst.API_V3_KEY.getBytes("utf-8"));
            String ciphertext = param.getResource().getCiphertext();
            String associatedData = param.getResource().getAssociatedData();
            String nonce = param.getResource().getNonce();
            return util.decryptToString(associatedData.getBytes("utf-8"), nonce.getBytes("utf-8"), ciphertext);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
