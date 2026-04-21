package upc.c505.modular.villageiot.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import upc.c505.common.UserUtils;
import upc.c505.common.wrapper.MyLambdaQueryWrapper;
import upc.c505.modular.auth.service.ISysAreaService;
import upc.c505.modular.people.service.IPeopleGovernmentService;
import upc.c505.modular.villageiot.controller.param.VideoConst;
import upc.c505.modular.villageiot.controller.param.VideoEsurfingDeviceListSearchParam;
import upc.c505.modular.villageiot.controller.param.VideoEsurfingGroupSearchParam;
import upc.c505.modular.villageiot.entity.VideoConfigEsurfing;
import upc.c505.modular.villageiot.entity.VideoHostConfig;
import upc.c505.modular.villageiot.mapper.VideoConfigEsurfingMapper;
import upc.c505.modular.villageiot.mapper.VideoHostConfigMapper;
import upc.c505.modular.villageiot.service.IVideoConfigEsurfingService;
import upc.c505.modular.villageiot.util.esurfing.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author frd
 * @since 2024-03-20
 */
@Service
@Slf4j
public class VideoConfigEsurfingServiceImpl extends ServiceImpl<VideoConfigEsurfingMapper, VideoConfigEsurfing> implements IVideoConfigEsurfingService {

    // 获取目录树URL
    private static final String REGIN_URL = "https://vcp.21cn.com/open/u/device/getReginWithGroupList";
    // 查询设备列表URL
    private static final String DEVICE_LIST_URL = "https://vcp.21cn.com/open/u/device/getDeviceList";
    // 获取设备直播链接URL
    private static final String DEVICE_MEDIA_URL = "https://vcp.21cn.com/open/u/cloud/getDeviceMediaUrl";

    // 批量查询设备状态URL
    private static final String BATCH_DEVICE_STATUS = "https://vcp.21cn.com/open/u/device/batchDeviceStatus";

    @Autowired
    private IPeopleGovernmentService peopleGovernmentService;

    @Autowired
    private ISysAreaService sysAreaService;

    @Autowired
    private VideoHostConfigMapper videoHostConfigMapper;

    @Autowired
    private VideoConfigEsurfingMapper videoConfigEsurfingMapper;

    @Override
    public String getReginWithGroupList(VideoEsurfingGroupSearchParam param) {
        MyLambdaQueryWrapper<VideoHostConfig> lambdaQueryWrapper = new MyLambdaQueryWrapper<>();
        /**
         * 如果查询的是管辖区域
         *         1、如果传入areaId不为空：判断传入的areaId是不是在当前用户的管辖区域内，
         *         如果传入的areaId合法，当flag为0（或者flag为空）时只查数据库中和传入areaId相等的数据，
         *         当flag为1时先查当前用户的管辖区域，然后再查sys_area表里面传入areaId的子区域，
         *         最后把两者的交集里面的areaId拿出来，到数据库里面查areaId在交集里面的数据。
         *         2、如果传入的areaId为空，那就查询该用户管辖区域的数据。
         */
        if (ObjectUtils.isNotEmpty(param.getIsApplicableArea()) && param.getIsApplicableArea() == 0) {
            //获取当前用户的管辖区域列表
            List<Long> list = peopleGovernmentService.getManageAreaIdList();
            if (ObjectUtils.isNotEmpty(param.getAreaId())) {
                //如果传入的areaId不合法，直接返回空的页面
                if (!list.contains(param.getAreaId())) {
                    return "";
                }
                //当传入的areaId合法时
                // 当flag为0（或者flag为空）时只查数据库中和传入areaId相等的数据
                if (ObjectUtils.isEmpty(param.getFlag()) || param.getFlag() == 0) {
                    lambdaQueryWrapper.eq(VideoHostConfig::getAreaId, param.getAreaId());
                }
                //当flag为1时先查当前用户的管辖区域，然后再查sys_area表里面传入areaId的子区域，
                // 最后把两者的交集里面的areaId拿出来，到数据库里面查areaId在交集里面的数据。
                if (ObjectUtils.isNotEmpty(param.getFlag()) && param.getFlag() == 1) {
                    List<Long> areaIdList = sysAreaService.getChildAreaIdList(param.getAreaId());
                    list.retainAll(areaIdList);
                    lambdaQueryWrapper.in(VideoHostConfig::getAreaId, list);
                }
            } else {
                lambdaQueryWrapper.in(VideoHostConfig::getAreaId, list);
            }
        }
        /**
         * 如果需要查适用区域：
         * 1、如果传入areaId不为空：判断传入的areaId是不是在当前用户所在区域及子区域内，
         * 如果传入的areaId合法，当flag为0（或者flag为空）时只查数据库中和传入areaId相等的数据，当flag为1时查询传入areaId及其子区域的数据。
         * 2、如果传入的areaId为空，那就查该用户areaId的数据。
         */
        if (ObjectUtils.isNotEmpty(param.getIsApplicableArea()) && param.getIsApplicableArea() == 1) {
            List<Long> areaIdList = sysAreaService.getChildAreaIdList(UserUtils.get().getAreaId());
            if (ObjectUtils.isNotEmpty(param.getAreaId())) {
                //如果传入的areaId不合法，直接返回空的页面
                if (!areaIdList.contains(param.getAreaId())) {
                    return "";
                }
                //如果传入的areaId合法，当flag为0（或者flag为空）时只查数据库中和传入areaId相等的数据，当flag为1时查询传入areaId及其子区域的数据。
                if (ObjectUtils.isEmpty(param.getFlag()) || param.getFlag() == 0) {
                    lambdaQueryWrapper.eq(VideoHostConfig::getAreaId, param.getAreaId());
                }
                if (ObjectUtils.isNotEmpty(param.getFlag()) && param.getFlag() == 1) {
                    areaIdList = sysAreaService.getChildAreaIdList(param.getAreaId());
                    lambdaQueryWrapper.in(VideoHostConfig::getAreaId, areaIdList);
                }
            } else {
                lambdaQueryWrapper.eq(VideoHostConfig::getAreaId, UserUtils.get().getAreaId());
            }
        }
        VideoHostConfig videoHostConfig = videoHostConfigMapper.selectOne(lambdaQueryWrapper
                .eq(VideoHostConfig::getDeviceType, VideoConst.ESURFING));

        try {
            // 接口定义具体入参
            Map<String, Object> map = new HashMap<String, Object>();
            if (StringUtils.isNotBlank(param.getRegionId())) {
                map.put("regionId", param.getRegionId());
            } else {
                map.put("regionId", "");
            }
            String json = sendMessage1(REGIN_URL, map, videoHostConfig);
            return json;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "请求失败";
    }

    @Override
    public String getDeviceMediaUrl(String deviceCode, Integer proto, Integer supportDomain) {
        try{
            // 接口定义具体入参
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("deviceCode",deviceCode);
            map.put("proto", ObjectUtils.isNotNull(proto)?proto:3);
            map.put("supportDomain", supportDomain);
            String json = sendMessage(DEVICE_MEDIA_URL,map);
            log.info("返回的数据为：{}",json);
            JSONObject jsonObject = JSONObject.parseObject(json);
            Integer code = jsonObject.getInteger("code");
            if(ObjectUtils.isNotNull(code)&&code == 0){
                // 0表示请求成功
                // 将返回结果的data解码
                String data = XXTeaDEC(jsonObject.getString("data"), CommonUtil.APP_SECRET);
                log.info("XXTea解密后：{}",data);
                return data;
            }else{
                return json;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return "请求失败";
    }

    @Override
    public Integer batchDeviceStatus(Integer commonId) {
        // 取出所有设备的deviceCode
        List<String> deviceCodeList = videoConfigEsurfingMapper.selectList(
                new LambdaQueryWrapper<VideoConfigEsurfing>()
                        .eq(VideoConfigEsurfing::getVideoConfigCommonId, commonId)
                        .select(VideoConfigEsurfing::getDeviceCode)
        ).stream().map(VideoConfigEsurfing::getDeviceCode).collect(Collectors.toList());
        // 临时列表，每20个发一组请求
        List<String> tempDeviceCodeList = new ArrayList<>();
        // 发送列表计数
        int addListCount = 0;
        int i = 0;
        List<VideoConfigEsurfing> updateList = new ArrayList<>();
        for (i = 0; i < deviceCodeList.size(); i++) {
            String deviceCode = deviceCodeList.get(i);
            tempDeviceCodeList.add(deviceCode);
            addListCount++;
            // 如果少于20个，跳出循环，执行最后一次请求
            if (deviceCodeList.size() - i < 20) {
                break;
            }
            // 每20个发送一次请求
            if (addListCount >= 20) {
                // 接口定义具体入参
                Map<String, Object> map = new HashMap<String, Object>();
                // 转化为逗号分隔字符串
                String deviceCodes = String.join(",", tempDeviceCodeList);
                log.info("本次deviceCodes为：{}", deviceCodes);
                map.put("deviceCodes", deviceCodes);
                map.put("queryData", 1);
                // 重置计数
                tempDeviceCodeList = new ArrayList<>();
                addListCount = 0;
                // 发送请求
                try {
                    String result = sendMessage(BATCH_DEVICE_STATUS, map);
                    log.info("result为：{}", result);
                    JSONObject jsonObject = JSONObject.parseObject(result);
                    JSONArray data = jsonObject.getJSONArray("data");
                    // 如果不为空
                    if (ObjectUtils.isNotNull(data)) {
                        for (Object item : data) {
                            // 向更新列表中添加数据
                            String itemStr = JSONObject.toJSONString(item);
                            log.info(itemStr);
                            JSONObject itemJson = JSONObject.parseObject(itemStr);
                            String deviceCode1 = itemJson.getString("deviceCode");
                            Integer status = itemJson.getInteger("status");
                            VideoConfigEsurfing videoConfigEsurfing = new VideoConfigEsurfing();
                            videoConfigEsurfing.setDeviceCode(deviceCode1)
                                    .setOnlineStatus(status);
                            updateList.add(videoConfigEsurfing);
                        }
                    } else {
                        log.info("未获取到data数据！");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        while (i < deviceCodeList.size()) {
            String deviceCode = deviceCodeList.get(i);
            tempDeviceCodeList.add(deviceCode);
            addListCount++;
            i++;
        }
        // 接口定义具体入参
        Map<String, Object> map = new HashMap<String, Object>();
        // 转化为逗号分隔字符串
        String deviceCodes = String.join(",", tempDeviceCodeList);
        log.info("本次deviceCodes为：{}", deviceCodes);
        map.put("deviceCodes", deviceCodes);
        map.put("queryData", 1);
        // 重置计数
        tempDeviceCodeList = new ArrayList<>();
        addListCount = 0;
        // 发送请求
        try {
            String result = sendMessage(BATCH_DEVICE_STATUS, map);
            log.info("result为：{}", result);
            JSONObject jsonObject = JSONObject.parseObject(result);
            JSONArray data = jsonObject.getJSONArray("data");
            // 如果不为空
            if (ObjectUtils.isNotNull(data)) {
                for (Object item : data) {
                    // 向更新列表中添加数据
                    String itemStr = JSONObject.toJSONString(item);
                    log.info(itemStr);
                    JSONObject itemJson = JSONObject.parseObject(itemStr);
                    String deviceCode1 = itemJson.getString("deviceCode");
                    Integer status = itemJson.getInteger("status");
                    VideoConfigEsurfing videoConfigEsurfing = new VideoConfigEsurfing();
                    videoConfigEsurfing.setDeviceCode(deviceCode1)
                            .setOnlineStatus(status);
                    updateList.add(videoConfigEsurfing);
                }
            } else {
                log.info("未获取到data数据！");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Integer updateNumber = 0;
        if (updateList.size() > 0) {
            updateNumber = videoConfigEsurfingMapper.multiUpdateOnlineStatus(updateList);
        }
        return updateNumber;
    }

    @Override
    public String getDeviceList(VideoEsurfingDeviceListSearchParam param) {
        MyLambdaQueryWrapper<VideoHostConfig> lambdaQueryWrapper = new MyLambdaQueryWrapper<>();
        /**
         * 如果查询的是管辖区域
         *         1、如果传入areaId不为空：判断传入的areaId是不是在当前用户的管辖区域内，
         *         如果传入的areaId合法，当flag为0（或者flag为空）时只查数据库中和传入areaId相等的数据，
         *         当flag为1时先查当前用户的管辖区域，然后再查sys_area表里面传入areaId的子区域，
         *         最后把两者的交集里面的areaId拿出来，到数据库里面查areaId在交集里面的数据。
         *         2、如果传入的areaId为空，那就查询该用户管辖区域的数据。
         */
        if (ObjectUtils.isNotEmpty(param.getIsApplicableArea()) && param.getIsApplicableArea() == 0) {
            //获取当前用户的管辖区域列表
            List<Long> list = peopleGovernmentService.getManageAreaIdList();
            if (ObjectUtils.isNotEmpty(param.getAreaId())) {
                //如果传入的areaId不合法，直接返回空的页面
                if (!list.contains(param.getAreaId())) {
                    return "";
                }
                //当传入的areaId合法时
                // 当flag为0（或者flag为空）时只查数据库中和传入areaId相等的数据
                if (ObjectUtils.isEmpty(param.getFlag()) || param.getFlag() == 0) {
                    lambdaQueryWrapper.eq(VideoHostConfig::getAreaId, param.getAreaId());
                }
                //当flag为1时先查当前用户的管辖区域，然后再查sys_area表里面传入areaId的子区域，
                // 最后把两者的交集里面的areaId拿出来，到数据库里面查areaId在交集里面的数据。
                if (ObjectUtils.isNotEmpty(param.getFlag()) && param.getFlag() == 1) {
                    List<Long> areaIdList = sysAreaService.getChildAreaIdList(param.getAreaId());
                    list.retainAll(areaIdList);
                    lambdaQueryWrapper.in(VideoHostConfig::getAreaId, list);
                }
            } else {
                lambdaQueryWrapper.in(VideoHostConfig::getAreaId, list);
            }
        }
        /**
         * 如果需要查适用区域：
         * 1、如果传入areaId不为空：判断传入的areaId是不是在当前用户所在区域及子区域内，
         * 如果传入的areaId合法，当flag为0（或者flag为空）时只查数据库中和传入areaId相等的数据，当flag为1时查询传入areaId及其子区域的数据。
         * 2、如果传入的areaId为空，那就查该用户areaId的数据。
         */
        if (ObjectUtils.isNotEmpty(param.getIsApplicableArea()) && param.getIsApplicableArea() == 1) {
            List<Long> areaIdList = sysAreaService.getChildAreaIdList(UserUtils.get().getAreaId());
            if (ObjectUtils.isNotEmpty(param.getAreaId())) {
                //如果传入的areaId不合法，直接返回空的页面
                if (!areaIdList.contains(param.getAreaId())) {
                    return "";
                }
                //如果传入的areaId合法，当flag为0（或者flag为空）时只查数据库中和传入areaId相等的数据，当flag为1时查询传入areaId及其子区域的数据。
                if (ObjectUtils.isEmpty(param.getFlag()) || param.getFlag() == 0) {
                    lambdaQueryWrapper.eq(VideoHostConfig::getAreaId, param.getAreaId());
                }
                if (ObjectUtils.isNotEmpty(param.getFlag()) && param.getFlag() == 1) {
                    areaIdList = sysAreaService.getChildAreaIdList(param.getAreaId());
                    lambdaQueryWrapper.in(VideoHostConfig::getAreaId, areaIdList);
                }
            } else {
                lambdaQueryWrapper.eq(VideoHostConfig::getAreaId, UserUtils.get().getAreaId());
            }
        }
        VideoHostConfig videoHostConfig = videoHostConfigMapper.selectOne(lambdaQueryWrapper
                .eq(VideoHostConfig::getDeviceType, VideoConst.ESURFING));
        try{
            // 接口定义具体入参
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("regionId", StringUtils.isNotBlank(param.getRegionId()) ? param.getRegionId() : "");
            map.put("pageNo", ObjectUtils.isNotNull(param.getPageNo()) ? param.getPageNo() : 1);
            map.put("pageSize", ObjectUtils.isNotNull(param.getPageSize()) ? param.getPageSize() : 10);
            String json = sendMessage1(DEVICE_LIST_URL, map, videoHostConfig);
            return json;
        }catch (Exception e){
            e.printStackTrace();
        }
        return "请求失败";
    }

    /**
     * 本地XXTea 解密
     * @param encodeData 加密的字符串
     * @param APP_SECRET app_scret
     * @return 明文
     * @throws Exception 异常
     */
    public static String XXTeaDEC(String encodeData, String APP_SECRET) throws Exception {
        return XXTea.decrypt(encodeData,"UTF-8", ByteFormat.toHex(APP_SECRET.getBytes()));
    }

    /**
     * 发送请求
     *
     * @param url url
     * @param map 接口入参
     */
    public static String sendMessage(String url, Map<String, Object> map) throws Exception {
        Map<String, Object> resultMap = GenReqParam.apiGen(map);
        log.info("resultMap的值为{}", resultMap);
        return OkHttpUtil.sendPost(url, resultMap);
    }

    /**
     * 发送请求
     *
     * @param url url
     * @param map 接口入参
     */
    public static String sendMessage1(String url, Map<String, Object> map, VideoHostConfig videoHostConfig) throws Exception {
        Map<String, Object> resultMap = GenReqParam.apiGen1(map, videoHostConfig);
        log.info("resultMap的值为{}", resultMap);
        return OkHttpUtil.sendPost(url, resultMap);
    }
}
