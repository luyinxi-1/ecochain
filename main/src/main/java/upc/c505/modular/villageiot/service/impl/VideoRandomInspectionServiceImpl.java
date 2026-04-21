package upc.c505.modular.villageiot.service.impl;

import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hikvision.artemis.sdk.ArtemisHttpUtil;
import com.hikvision.artemis.sdk.config.ArtemisConfig;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import upc.c505.common.UserUtils;
import upc.c505.common.requestparam.PageBaseSearchParam;
import upc.c505.common.wrapper.MyLambdaQueryWrapper;
import upc.c505.modular.auth.service.ISysAreaService;
import upc.c505.modular.people.service.IPeopleGovernmentService;
import upc.c505.modular.supenterprise.entity.SupEnterprise;
import upc.c505.modular.supenterprise.mapper.SupEnterpriseMapper;
import upc.c505.modular.village.entity.VillageConstructionProjectDetails;
import upc.c505.modular.village.entity.VillageConstructionUnit;
import upc.c505.modular.village.mapper.VillageConstructionProjectDetailsMapper;
import upc.c505.modular.village.mapper.VillageConstructionUnitMapper;
import upc.c505.modular.villageiot.controller.param.*;
import upc.c505.modular.villageiot.entity.*;
import upc.c505.modular.villageiot.mapper.*;
import upc.c505.modular.villageiot.service.IVideoConfigCommonService;
import upc.c505.modular.villageiot.service.IVideoConfigEsurfingService;
import upc.c505.modular.villageiot.service.IVideoConfigGuobiaoService;
import upc.c505.modular.villageiot.service.IVideoRandomInspectionService;
import upc.c505.modular.villageiot.util.digestutils.HttpRequestUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

import static upc.c505.modular.money.service.impl.MoneyProcessServiceImpl.GenerateImage;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author frd
 * @since 2024-03-25
 */
@Service
@Slf4j
public class VideoRandomInspectionServiceImpl implements IVideoRandomInspectionService {

    @Autowired
    private IVideoConfigCommonService videoConfigCommonService;

    @Autowired
    private VideoConfigCommonMapper videoConfigCommonMapper;

    @Autowired
    private VideoHostConfigMapper videoHostConfigMapper;

    private static final String ARTEMIS_PATH = "/artemis";

    @Autowired
    private ISysAreaService sysAreaService;

    @Autowired
    private VideoConfigIoaServiceImpl videoConfigIoaService;
    @Autowired
    private IVideoConfigEsurfingService videoConfigEsurfingService;
    @Autowired
    private IVideoConfigGuobiaoService videoConfigGuobiaoService;

    @Autowired
    private IPeopleGovernmentService peopleGovernmentService;

    @Autowired
    private VideoConfigHaikangiscMapper videoConfigHaikangiscMapper;

    @Autowired
    private VideoConfigDahua7016Mapper videoConfigDahua7016Mapper;

    @Autowired
    private VideoConfigEsurfingMapper videoConfigEsurfingMapper;

    @Autowired
    private VideoConfigGuobiaoMapper videoConfigGuobiaoMapper;

    @Autowired
    private VideoConfigIoaMapper videoConfigIoaMapper;

    @Autowired
    private VideoConfigOtherMapper videoConfigOtherMapper;

    @Autowired
    private VideoConfigUniviewMapper videoConfigUniviewMapper;

    @Autowired
    private VideoConfigZiguangMapper videoConfigZiguangMapper;

    @Autowired
    private VillageConstructionProjectDetailsMapper villageConstructionProjectDetailsMapper;

    @Autowired
    private VillageConstructionUnitMapper villageConstructionUnitMapper;
    @Autowired
    private SupEnterpriseMapper supEnterpriseMapper;
    @Override
    public Page<VideoConfigCommon> getIndex(VideoRandomInspectionIndexSearchParam param) {
        MyLambdaQueryWrapper<VideoConfigCommon> lambdaQueryWrapper = new MyLambdaQueryWrapper<>();
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
                    return new Page<>();
                }
                //当传入的areaId合法时
                // 当flag为0（或者flag为空）时只查数据库中和传入areaId相等的数据
                if (ObjectUtils.isEmpty(param.getFlag()) || param.getFlag() == 0) {
                    lambdaQueryWrapper.eq(VideoConfigCommon::getAreaId, param.getAreaId());
                }
                //当flag为1时先查当前用户的管辖区域，然后再查sys_area表里面传入areaId的子区域，
                // 最后把两者的交集里面的areaId拿出来，到数据库里面查areaId在交集里面的数据。
                if (ObjectUtils.isNotEmpty(param.getFlag()) && param.getFlag() == 1) {
                    List<Long> areaIdList = sysAreaService.getChildAreaIdList(param.getAreaId());
                    list.retainAll(areaIdList);
                    lambdaQueryWrapper.in(VideoConfigCommon::getAreaId, list);
                }
            } else {
                lambdaQueryWrapper.in(VideoConfigCommon::getAreaId, list);
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
                    return new Page<>();
                }
                //如果传入的areaId合法，当flag为0（或者flag为空）时只查数据库中和传入areaId相等的数据，当flag为1时查询传入areaId及其子区域的数据。
                if (ObjectUtils.isEmpty(param.getFlag()) || param.getFlag() == 0) {
                    lambdaQueryWrapper.eq(VideoConfigCommon::getAreaId, param.getAreaId());
                }
                if (ObjectUtils.isNotEmpty(param.getFlag()) && param.getFlag() == 1) {
                    areaIdList = sysAreaService.getChildAreaIdList(param.getAreaId());
                    lambdaQueryWrapper.in(VideoConfigCommon::getAreaId, areaIdList);
                }
            } else {
                lambdaQueryWrapper.eq(VideoConfigCommon::getAreaId, UserUtils.get().getAreaId());
            }
        }
        //如果是建设用户或者企业用户，那么number不为空，去判断一下权限
        List<String> numberList = judgeAuthority();
        if (CollectionUtils.isNotEmpty(numberList)) {
            lambdaQueryWrapper.in(VideoConfigCommon::getEntCreditCode, numberList);
        }

        lambdaQueryWrapper
                .eq(ObjectUtils.isNotEmpty(param.getMonitorAttributes()), VideoConfigCommon::getMonitorAttributes, param.getMonitorAttributes())
                .like(ObjectUtils.isNotEmpty(param.getEntCreditCode()), VideoConfigCommon::getEntCreditCode, param.getEntCreditCode())
                .eq(ObjectUtils.isNotEmpty(param.getCoaType()), VideoConfigCommon::getCoaType, param.getCoaType())
                .like(ObjectUtils.isNotEmpty(param.getMonitorGroupName()), VideoConfigCommon::getMonitorGroupName, param.getMonitorGroupName())
                .orderBy(true, Objects.equals(param.getIsAsc(), 1), VideoConfigCommon::getAddDatetime);
        Page<VideoConfigCommon> videoConfigCommonPage = videoConfigCommonMapper.selectPage(new Page<>(param.getCurrent(), param.getSize()), lambdaQueryWrapper);
        List<VideoConfigCommon> resultList = videoConfigCommonPage.getRecords();
        resultList.forEach(common -> {
            common.setOnlineNumber(getOnlineNumberByCommonId(common.getId()));
        });
        videoConfigCommonPage.setRecords(resultList);
        return videoConfigCommonPage;
    }

    @Override
    public JSONObject getPageCamerasByRegions(PageBaseSearchParam pageQuery, String regionIndexCode) {
        VideoHostConfig haikangHostConfig = videoHostConfigMapper.selectOne(new MyLambdaQueryWrapper<VideoHostConfig>()
                .eq(VideoHostConfig::getDeviceType, VideoConst.HAIKANG));
        init(haikangHostConfig.getHostAddress(), haikangHostConfig.getAppkey(), haikangHostConfig.getAppSecret());
        String http = haikangHostConfig.getProtocol() + "://";
        String getRootApi = ARTEMIS_PATH + "/api/resource/v1/regions/regionIndexCode/cameras";
        Map<String, String> path = new HashMap<String, String>(2) {
            {
                put(http, getRootApi);//根据现场环境部署确认是http还是https
            }
        };
        JSONObject jsonBody = new JSONObject();
        jsonBody.put("regionIndexCode", regionIndexCode);
        jsonBody.put("pageNo", pageQuery.getCurrent());
        jsonBody.put("pageSize", pageQuery.getSize());
        String body = jsonBody.toJSONString();

        String result = ArtemisHttpUtil.doPostStringArtemis(path, body, null, null, "application/json", null);// post请求application/json类型参数
        System.out.println("请求路径：" + getRootApi + ",请求参数：" + body + ",返回结果：" + result);
        JSONObject json = JSONObject.parseObject(result);
        return json;
    }

    @Override
    public List<VideoInspectionPointLocationInfoParam> videoInspection(Long id) {
        VideoConfigCommon common = videoConfigCommonService.getById(id);
        String deviceType = common.getDeviceType();
        String videoSign = common.getVideoSign();
        Long areaId = common.getAreaId();
        //存放监控点名字和取流地址
        List<VideoInspectionPointLocationInfoParam> list = new ArrayList<>();
        switch (deviceType) {
            case VideoConst.HAIKANG:
                //如果是海康的设备
                haikang(id, areaId, list, videoSign);
                break;
            case VideoConst.UNIVIEW:
                //如果是宇视的设备
                uniview(id, areaId, list, videoSign);
                break;
            case VideoConst.DAHUA:
                //如果是大华的设备
                dahua(id, areaId, list, videoSign);
                break;
            case VideoConst.ZIGUANG:
                //如果是紫光的设备
                ziguang(id, areaId, list, videoSign);
                break;
            case VideoConst.IOA:
                //如果是内蒙的设备
                ioa(id, areaId, list, videoSign);
                break;
            case VideoConst.ESURFING:
                //如果是天翼的设备
                esurfing(id, list, videoSign);
                break;
            case VideoConst.GUOBIAO:
                //如果是国标的设备
                guobiao(id, list, videoSign);
                break;
            case VideoConst.OTHER:
                //如果是其他类型的设备
                other(id, list, videoSign);
                break;
            default:
                break;
        }
        return list;
    }


    @Override
    public JSONObject getRSA(Long areaId) {
        VideoHostConfig univiewHostConfig = videoHostConfigMapper.selectOne(
                new MyLambdaQueryWrapper<VideoHostConfig>()
                        .eq(VideoHostConfig::getDeviceType, VideoConst.UNIVIEW)
                        .eq(VideoHostConfig::getAreaId, areaId)
        );
        String url = univiewHostConfig.getProtocol() + "://" + univiewHostConfig.getHostAddress() + "/LAPI/V1.0/System/Security/RSA";
        String json = HttpRequestUtils.sendGet(url, "", univiewHostConfig.getAppkey(), univiewHostConfig.getAppSecret(), "json");
        return JSON.parseObject(json);
    }

    @Override
    public JSONObject getVideoChannelUrl(VideoChannelUrlParam param, Long channelsID, Long streamsID, Long transType, Long transProtocol, Long areaId) {
        VideoHostConfig univiewHostConfig = videoHostConfigMapper.selectOne(
                new MyLambdaQueryWrapper<VideoHostConfig>()
                        .eq(VideoHostConfig::getDeviceType, VideoConst.UNIVIEW)
                        .eq(VideoHostConfig::getAreaId, areaId)
        );
        String url = univiewHostConfig.getProtocol() + "://" + univiewHostConfig.getHostAddress()
                + "/LAPI/V1.0/Channels/" + channelsID + "/Media/Video/Streams/" + streamsID + "/LiveStreamURL";
        String result = HttpRequestUtils.sendPost(url, "TransType=" + transType + "&TransProtocol=" + transProtocol, univiewHostConfig.getAppkey(),
                univiewHostConfig.getAppSecret(), JSON.toJSONString(param), "json");
        return JSON.parseObject(result);
    }

    @Override
    public Map<String, Long> getOnlineAndOfflineNumber(OnlineAndOfflineSearchParam param) {
        //先根据areaId去查
        MyLambdaQueryWrapper<VideoConfigCommon> lambdaQueryWrapper = new MyLambdaQueryWrapper<>();
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
                    return new HashMap<>();
                }
                //当传入的areaId合法时
                // 当flag为0（或者flag为空）时只查数据库中和传入areaId相等的数据
                if (ObjectUtils.isEmpty(param.getFlag()) || param.getFlag() == 0) {
                    lambdaQueryWrapper.eq(VideoConfigCommon::getAreaId, param.getAreaId());
                }
                //当flag为1时先查当前用户的管辖区域，然后再查sys_area表里面传入areaId的子区域，
                // 最后把两者的交集里面的areaId拿出来，到数据库里面查areaId在交集里面的数据。
                if (ObjectUtils.isNotEmpty(param.getFlag()) && param.getFlag() == 1) {
                    List<Long> areaIdList = sysAreaService.getChildAreaIdList(param.getAreaId());
                    list.retainAll(areaIdList);
                    lambdaQueryWrapper.in(VideoConfigCommon::getAreaId, list);
                }
            } else {
                lambdaQueryWrapper.in(VideoConfigCommon::getAreaId, list);
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
                    return new HashMap<>();
                }
                //如果传入的areaId合法，当flag为0（或者flag为空）时只查数据库中和传入areaId相等的数据，当flag为1时查询传入areaId及其子区域的数据。
                if (ObjectUtils.isEmpty(param.getFlag()) || param.getFlag() == 0) {
                    lambdaQueryWrapper.eq(VideoConfigCommon::getAreaId, param.getAreaId());
                }
                if (ObjectUtils.isNotEmpty(param.getFlag()) && param.getFlag() == 1) {
                    areaIdList = sysAreaService.getChildAreaIdList(param.getAreaId());
                    lambdaQueryWrapper.in(VideoConfigCommon::getAreaId, areaIdList);
                }
            } else {
                lambdaQueryWrapper.eq(VideoConfigCommon::getAreaId, UserUtils.get().getAreaId());
            }
        }
        //再根据监控属性去查
        lambdaQueryWrapper.eq(ObjectUtils.isNotEmpty(param.getMonitorAttributes()), VideoConfigCommon::getMonitorAttributes, param.getMonitorAttributes());
        List<VideoConfigCommon> videoConfigCommonList = videoConfigCommonMapper.selectList(lambdaQueryWrapper);
        Long onlineNumber = 0L;
        Long offlineNumber = 0L;
        //对每个视频配置表数据，寻找他们关联的摄像头中对应的在线数和离线数
        for (VideoConfigCommon common : videoConfigCommonList) {
            List<Long> list = OnlineAndOfflineNumber(common.getId(), common.getDeviceType(), param.getAreaId());
            onlineNumber += list.get(0);
            offlineNumber += list.get(1);
        }
        Map<String, Long> map = new HashMap<>();
        map.put("onlineNumber", onlineNumber);
        map.put("offlineNumber", offlineNumber);
        return map;
    }

    private List<Long> OnlineAndOfflineNumber(Integer id, String deviceType, Long areaId) {
        if (Objects.equals(deviceType, VideoConst.HAIKANG)) {
            VideoHostConfig haikangHostConfig = videoHostConfigMapper.selectOne(
                    new MyLambdaQueryWrapper<VideoHostConfig>()
                            .eq(VideoHostConfig::getDeviceType, VideoConst.HAIKANG)
                            .eq(VideoHostConfig::getAreaId, areaId)
            );
            refreshHaikangOnlineNumber(haikangHostConfig);
            List<VideoConfigHaikangisc> list = videoConfigHaikangiscMapper.selectList(
                    new MyLambdaQueryWrapper<VideoConfigHaikangisc>()
                            .eq(VideoConfigHaikangisc::getVideoConfigCommonId, id)
                            .select(VideoConfigHaikangisc::getOnline));
            long online = 0L;
            long offline = 0L;
            for (VideoConfigHaikangisc video : list) {
                if (video.getOnline() == 1) {
                    online++;
                } else if (video.getOnline() == 0) {
                    offline++;
                }
            }
            List<Long> returnList = new ArrayList<>();
            returnList.add(online);
            returnList.add(offline);
            return returnList;
        } else if (Objects.equals(deviceType, VideoConst.DAHUA)) {
            List<VideoConfigDahua7016> list = videoConfigDahua7016Mapper.selectList(
                    new MyLambdaQueryWrapper<VideoConfigDahua7016>()
                            .eq(VideoConfigDahua7016::getVideoConfigCommonId, id));
            long online = 0L;
            long offline = 0L;
            for (VideoConfigDahua7016 video : list) {
                if (video.getOnline() == 1) {
                    online++;
                } else if (video.getOnline() == 0) {
                    offline++;
                }
            }
            List<Long> returnList = new ArrayList<>();
            returnList.add(online);
            returnList.add(offline);
            return returnList;
        } else if (Objects.equals(deviceType, VideoConst.ESURFING)) {
            videoConfigEsurfingService.batchDeviceStatus(id);
            List<VideoConfigEsurfing> list = videoConfigEsurfingMapper.selectList(
                    new MyLambdaQueryWrapper<VideoConfigEsurfing>()
                            .eq(VideoConfigEsurfing::getVideoConfigCommonId, id));
            long online = 0L;
            long offline = 0L;
            for (VideoConfigEsurfing video : list) {
                if (video.getOnlineStatus() == 1) {
                    online++;
                } else if (video.getOnlineStatus() == 0) {
                    offline++;
                }
            }
            List<Long> returnList = new ArrayList<>();
            returnList.add(online);
            returnList.add(offline);
            return returnList;
        } else if (Objects.equals(deviceType, VideoConst.GUOBIAO)) {
            refreshGuoBiaoOnlineNumber(id);
            List<VideoConfigGuobiao> list = videoConfigGuobiaoMapper.selectList(
                    new MyLambdaQueryWrapper<VideoConfigGuobiao>()
                            .eq(VideoConfigGuobiao::getVideoConfigCommonId, id));
            long online = 0L;
            long offline = 0L;
            for (VideoConfigGuobiao video : list) {
                if (video.getOnline() == 1) {
                    online++;
                } else if (video.getOnline() == 0) {
                    offline++;
                }
            }
            List<Long> returnList = new ArrayList<>();
            returnList.add(online);
            returnList.add(offline);
            return returnList;
        } else if (Objects.equals(deviceType, VideoConst.IOA)) {
            List<VideoConfigIoa> list = videoConfigIoaMapper.selectList(
                    new MyLambdaQueryWrapper<VideoConfigIoa>()
                            .eq(VideoConfigIoa::getVideoConfigCommonId, id));
            long online = 0L;
            long offline = 0L;
            for (VideoConfigIoa video : list) {
                if (video.getOnline() == 1) {
                    online++;
                } else if (video.getOnline() == 0) {
                    offline++;
                }
            }
            List<Long> returnList = new ArrayList<>();
            returnList.add(online);
            returnList.add(offline);
            return returnList;
        } else if (Objects.equals(deviceType, VideoConst.UNIVIEW)) {
            List<VideoConfigUniview> list = videoConfigUniviewMapper.selectList(
                    new MyLambdaQueryWrapper<VideoConfigUniview>()
                            .eq(VideoConfigUniview::getVideoConfigCommonId, id));
            long online = 0L;
            long offline = 0L;
            for (VideoConfigUniview video : list) {
                if (video.getOnline() == 1) {
                    online++;
                } else if (video.getOnline() == 0) {
                    offline++;
                }
            }
            List<Long> returnList = new ArrayList<>();
            returnList.add(online);
            returnList.add(offline);
            return returnList;
        } else if (Objects.equals(deviceType, VideoConst.ZIGUANG)) {
            List<VideoConfigZiguang> list = videoConfigZiguangMapper.selectList(
                    new MyLambdaQueryWrapper<VideoConfigZiguang>()
                            .eq(VideoConfigZiguang::getVideoConfigCommonId, id));
            long online = 0L;
            long offline = 0L;
            for (VideoConfigZiguang video : list) {
                if (video.getOnline() == 1) {
                    online++;
                } else if (video.getOnline() == 0) {
                    offline++;
                }
            }
            List<Long> returnList = new ArrayList<>();
            returnList.add(online);
            returnList.add(offline);
            return returnList;
        }
        List<Long> returnList = new ArrayList<>();
        returnList.add(0L);
        returnList.add(0L);
        return returnList;
    }

    private void refreshHaikangOnlineNumber(VideoHostConfig haikangHostConfig) {
        InitParam initParam = new InitParam();
        initParam.setAppkey(haikangHostConfig.getAppkey())
                .setAppsecret(haikangHostConfig.getAppSecret())
                .setHost(haikangHostConfig.getHostAddress())
                .setProtocol(haikangHostConfig.getProtocol());
        init(initParam.getHost(), initParam.getAppkey(), initParam.getAppsecret());
        String getOnlineApi = ARTEMIS_PATH + "/api/nms/v1/online/camera/get";
        Map<String, String> path = Collections.singletonMap(initParam.getProtocol() + "://", getOnlineApi);
        HashMap<String, Object> map = new HashMap<>();
        Integer pageNo = 1;
        Integer pageSize = 100;
//        Integer total = 0;
        int count = 0;
        Integer totalPage = 0;
        map.put("pageNo", pageNo);
        map.put("pageSize", pageSize);
//        map.put("status",0);
        int badCount = 0;
        // 向海康在线平台接口发送请求，获取在线离线设备数量
        String body = JSON.toJSONString(map);
        String result = ArtemisHttpUtil.doPostStringArtemis(path, body, null, null, "application/json", null);
        log.info("请求参数：" + body);
        JSONObject resultJson = JSONObject.parseObject(result);
        if (ObjectUtils.isNotEmpty(resultJson) && ObjectUtils.isNotEmpty(resultJson.getJSONObject("data"))) {
            JSONObject data = resultJson.getJSONObject("data");
            totalPage = data.getInteger("totalPage");
            JSONArray list = data.getJSONArray("list");
            List<JSONObject> jsonObjects = list.toJavaList(JSONObject.class);
            for (JSONObject item : jsonObjects) {
                count++;
                String regionName = item.getString("regionName");
                String indexCode = item.getString("indexCode");
                Integer online = item.getInteger("online");
//            log.info("count = " + count +" , regionName = "+regionName +" , indexCode = "+ indexCode + " , online = "+ online);
            }
            pageNo = 0;
            while (pageNo < totalPage) {
                ++pageNo;
                map.remove("pageNo");
                map.put("pageNo", pageNo);
                body = JSON.toJSONString(map);
                log.info("请求参数：" + body);
                result = ArtemisHttpUtil.doPostStringArtemis(path, body, null, null, "application/json", null);
                resultJson = JSONObject.parseObject(result);
                data = resultJson.getJSONObject("data");
                list = data.getJSONArray("list");
                jsonObjects = list.toJavaList(JSONObject.class);
                //定义一个每100条数据更新一次的List，用于批量修改
                List<VideoConfigHaikangisc> updateList = new ArrayList<>();
                for (JSONObject item : jsonObjects) {
                    count++;
                    String regionName = item.getString("regionName");
                    String indexCode = item.getString("indexCode");
                    Integer online = item.getInteger("online");
                    //向updateList里添加数据，用于批量修改
                    updateList.add(
                            new VideoConfigHaikangisc()
                                    .setCameraIndexCode(indexCode)
                                    .setOnline(online)
                    );

                }
                // 如果updateList不为空
                if (CollectionUtils.isNotEmpty(updateList)) {
                    log.info("本次updateList的size为：{}", updateList.size());
                    int updateNumber = videoConfigHaikangiscMapper.multiUpdateOnlineStatus(updateList);
                    log.info("执行批量修改后，修改了:{}条数据的在线状态", updateNumber);
                }
            }
        } else {
            return;
        }
        // JSONObject data = resultJson.getJSONObject("data");
        // 如果为空，则不继续执行
        // if (ObjectUtils.isEmpty(data)) {
            // return;
        // }
    }

    @Override
    public void refreshGuoBiaoOnlineNumber(Integer commonId) {
        List<VideoConfigGuobiao> addList = new ArrayList<>();
        //只查询筛选出来的视频配置对应的国标摄像头
        List<VideoConfigGuobiao> device = videoConfigGuobiaoMapper.selectList(new MyLambdaQueryWrapper<VideoConfigGuobiao>()
                .eq(VideoConfigGuobiao::getVideoConfigCommonId, commonId)
                .select(VideoConfigGuobiao::getDeviceId));
        List<VideoConfigGuobiao> collect = device.stream().distinct().collect(Collectors.toList());
        System.out.println(collect);
        collect.forEach((i) -> {
            String body = videoConfigGuobiaoService.getDeviceState(i.getDeviceId(), 1, 5000);
            JSONObject object = JSONObject.parseObject(body);
            String data = object.get("data").toString();

            JSONObject datas = JSONObject.parseObject(data);
            JSONArray list = datas.getJSONArray("list");
            for (int j = 0; j < list.size(); j++) {
                String channelId = list.getJSONObject(j).get("channelId").toString();
                String deviceId = list.getJSONObject(j).get("deviceId").toString();
                Integer status = list.getJSONObject(j).getInteger("status");

                VideoConfigGuobiao videoConfigGuobiao = new VideoConfigGuobiao();
                videoConfigGuobiao.setOnline(status);
                videoConfigGuobiao.setChannelId(channelId);
                videoConfigGuobiao.setDeviceId(deviceId);
                addList.add(videoConfigGuobiao);
            }
        });
        if (CollectionUtils.isNotEmpty(addList)) {
            videoConfigGuobiaoMapper.MultiUpdateOnlineStatus(addList);
        }
    }

    @Override
    public String cutPicture(String json) {
        String filename = System.currentTimeMillis() + ".jpg";
        JSONObject obj = JSON.parseObject(json);
        //String filePath=obj.getString("filePath");
        String filePath = "upload/cut/";
        String pic = obj.getString("pic");
        pic = pic.substring(pic.indexOf(",") + 1);
        GenerateImage(pic, filePath + filename);
        return filename;
    }

    @Override
    public Page<ProjectRemoteSupervisionReturnParam> selectProjectRemoteSupervisionPage(ProjectRemoteSupervisionSearchParam param) {
        //首先根据areaId三件套，年份、项目类型、模糊搜索值（搜索项目名称、项目编号），承建单位（模糊搜索）、监理单位（模糊搜索）查询项目详情表
        MyLambdaQueryWrapper<VillageConstructionProjectDetails> lambdaQueryWrapper = new MyLambdaQueryWrapper<>();
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
                    return new Page<>();
                }
                //当传入的areaId合法时
                // 当flag为0（或者flag为空）时只查数据库中和传入areaId相等的数据
                if (ObjectUtils.isEmpty(param.getFlag()) || param.getFlag() == 0) {
                    lambdaQueryWrapper.eq(VillageConstructionProjectDetails::getAreaId, param.getAreaId());
                }
                //当flag为1时先查当前用户的管辖区域，然后再查sys_area表里面传入areaId的子区域，
                // 最后把两者的交集里面的areaId拿出来，到数据库里面查areaId在交集里面的数据。
                if (ObjectUtils.isNotEmpty(param.getFlag()) && param.getFlag() == 1) {
                    List<Long> areaIdList = sysAreaService.getChildAreaIdList(param.getAreaId());
                    list.retainAll(areaIdList);
                    lambdaQueryWrapper.in(VillageConstructionProjectDetails::getAreaId, list);
                }
            } else {
                lambdaQueryWrapper.in(VillageConstructionProjectDetails::getAreaId, list);
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
                    return new Page<>();
                }
                //如果传入的areaId合法，当flag为0（或者flag为空）时只查数据库中和传入areaId相等的数据，当flag为1时查询传入areaId及其子区域的数据。
                if (ObjectUtils.isEmpty(param.getFlag()) || param.getFlag() == 0) {
                    lambdaQueryWrapper.eq(VillageConstructionProjectDetails::getAreaId, param.getAreaId());
                }
                if (ObjectUtils.isNotEmpty(param.getFlag()) && param.getFlag() == 1) {
                    areaIdList = sysAreaService.getChildAreaIdList(param.getAreaId());
                    lambdaQueryWrapper.in(VillageConstructionProjectDetails::getAreaId, areaIdList);
                }
            } else {
                lambdaQueryWrapper.eq(VillageConstructionProjectDetails::getAreaId, UserUtils.get().getAreaId());
            }
        }
        lambdaQueryWrapper
                .select(VillageConstructionProjectDetails::getProjectYear, VillageConstructionProjectDetails::getProjectType, VillageConstructionProjectDetails::getProjectName,
                        VillageConstructionProjectDetails::getProjectNumber, VillageConstructionProjectDetails::getConstructionProjectLeader, VillageConstructionProjectDetails::getConstructionProjectLeaderPhone,
                        VillageConstructionProjectDetails::getConstructionName, VillageConstructionProjectDetails::getSupervisionName)
                .eq(ObjectUtils.isNotEmpty(param.getProjectYear()), VillageConstructionProjectDetails::getProjectYear, param.getProjectYear())
                .eq(ObjectUtils.isNotEmpty(param.getProjectType()), VillageConstructionProjectDetails::getProjectType, param.getProjectType())
                .and(ObjectUtils.isNotEmpty(param.getSearchParam()), wrapper -> {
                    wrapper.like(VillageConstructionProjectDetails::getProjectName, param.getSearchParam())
                            .or()
                            .like(VillageConstructionProjectDetails::getProjectNumber, param.getSearchParam());
                })
                .like(ObjectUtils.isNotEmpty(param.getConstructionName()), VillageConstructionProjectDetails::getConstructionName, param.getConstructionName())
                .like(ObjectUtils.isNotEmpty(param.getSupervisionName()), VillageConstructionProjectDetails::getSupervisionName, param.getSupervisionName());
        List<VillageConstructionProjectDetails> projectDetailsList = villageConstructionProjectDetailsMapper.selectList(lambdaQueryWrapper);
        //用传参里面的areaId三件套和项目编号，然后查监控属性是重点项目的视频配置项
        MyLambdaQueryWrapper<VideoConfigCommon> lambdaQueryWrapper1 = new MyLambdaQueryWrapper<>();
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
                    return new Page<>();
                }
                //当传入的areaId合法时
                // 当flag为0（或者flag为空）时只查数据库中和传入areaId相等的数据
                if (ObjectUtils.isEmpty(param.getFlag()) || param.getFlag() == 0) {
                    lambdaQueryWrapper1.eq(VideoConfigCommon::getAreaId, param.getAreaId());
                }
                //当flag为1时先查当前用户的管辖区域，然后再查sys_area表里面传入areaId的子区域，
                // 最后把两者的交集里面的areaId拿出来，到数据库里面查areaId在交集里面的数据。
                if (ObjectUtils.isNotEmpty(param.getFlag()) && param.getFlag() == 1) {
                    List<Long> areaIdList = sysAreaService.getChildAreaIdList(param.getAreaId());
                    list.retainAll(areaIdList);
                    lambdaQueryWrapper1.in(VideoConfigCommon::getAreaId, list);
                }
            } else {
                lambdaQueryWrapper1.in(VideoConfigCommon::getAreaId, list);
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
                    return new Page<>();
                }
                //如果传入的areaId合法，当flag为0（或者flag为空）时只查数据库中和传入areaId相等的数据，当flag为1时查询传入areaId及其子区域的数据。
                if (ObjectUtils.isEmpty(param.getFlag()) || param.getFlag() == 0) {
                    lambdaQueryWrapper1.eq(VideoConfigCommon::getAreaId, param.getAreaId());
                }
                if (ObjectUtils.isNotEmpty(param.getFlag()) && param.getFlag() == 1) {
                    areaIdList = sysAreaService.getChildAreaIdList(param.getAreaId());
                    lambdaQueryWrapper1.in(VideoConfigCommon::getAreaId, areaIdList);
                }
            } else {
                lambdaQueryWrapper1.eq(VideoConfigCommon::getAreaId, UserUtils.get().getAreaId());
            }
        }
        lambdaQueryWrapper1
                .eq(VideoConfigCommon::getMonitorAttributes, VideoConst.PROJECT);
        List<VideoConfigCommon> configCommonList = videoConfigCommonMapper.selectList(lambdaQueryWrapper1);
        //2024.9.14添加一个返回参数：企业在线视频数量
        for (VideoConfigCommon common : configCommonList) {
            common.setOnlineNumber(getOnlineNumberByCommonId(common.getId()));
        }
        List<ProjectRemoteSupervisionReturnParam> resultList = new ArrayList<>();
        //两个列表通过项目编号去匹配
        for (VillageConstructionProjectDetails details : projectDetailsList) {
            for (VideoConfigCommon common : configCommonList) {
                if (Objects.equals(details.getProjectNumber(), common.getEntCreditCode())) {
                    ProjectRemoteSupervisionReturnParam returnParam = new ProjectRemoteSupervisionReturnParam();
                    BeanUtils.copyProperties(details, returnParam);
                    BeanUtils.copyProperties(common, returnParam);
                    resultList.add(returnParam);
                    break;
                }
            }
        }
        Page<ProjectRemoteSupervisionReturnParam> page = new Page<>(param.getCurrent(), param.getSize(), resultList.size());
        List<ProjectRemoteSupervisionReturnParam> resultList1 = new ArrayList<>();
        for (long i = param.getSize() * (param.getCurrent() - 1); i < param.getSize() * param.getCurrent(); i++) {
            if (i >= resultList.size()) {
                break;
            }
            resultList1.add(resultList.get((int) i));
        }
        page.setRecords(resultList1);
        return page;
    }

    public void haikang(Long id, Long areaId, List<VideoInspectionPointLocationInfoParam> list, String videoSign) {
        List<VideoConfigHaikangisc> haikangiscList = videoConfigHaikangiscMapper.selectList(new MyLambdaQueryWrapper<VideoConfigHaikangisc>()
                .eq(VideoConfigHaikangisc::getVideoConfigCommonId, id));
        //找到host配置
        VideoHostConfig haikangHostConfig = videoHostConfigMapper.selectOne(new MyLambdaQueryWrapper<VideoHostConfig>()
                .eq(VideoHostConfig::getDeviceType, VideoConst.HAIKANG)
                .eq(VideoHostConfig::getAreaId, areaId));
        //遍历集合，将对应得知存入list
        haikangiscList
                .forEach(haikang -> {
                    //获取取流地址
                    String url = getPageCamerasByCamerasIndex(
                            //设置基础配置
                            new HaikangGetStreamParamClass()
                                    .setCameraIndexCode(haikang.getCameraIndexCode())
                                    .setStreamType(haikang.getStreamType())
                                    .setProtocol(haikang.getProtocol()),
                            //设置密钥等操作
                            new InitParam()
                                    .setProtocol(haikangHostConfig.getProtocol())
                                    .setHost(haikangHostConfig.getHostAddress())
                                    .setAppkey(haikangHostConfig.getAppkey())
                                    .setAppsecret(haikangHostConfig.getAppSecret())
                    );
                    list.add(
                            //将数据存入列表
                            new VideoInspectionPointLocationInfoParam()
                                    .setDeviceId(haikang.getId())
                                    .setDeviceType(VideoConst.HAIKANG)
                                    .setName(haikang.getPointName())
                                    .setUrl(url)
                                    .setOnline(haikang.getOnline())
                                    .setVideoSign(videoSign)
                    );
                });
    }

    /**
     * 宇视的，视频巡检用
     *
     * @param id
     * @param areaId
     * @param videoSign
     * @return
     */
    public void uniview(Long id, Long areaId, List<VideoInspectionPointLocationInfoParam> list, String videoSign) {
        VideoHostConfig univiewHostConfig = videoHostConfigMapper.selectOne(
                new MyLambdaQueryWrapper<VideoHostConfig>()
                        .eq(VideoHostConfig::getDeviceType, VideoConst.UNIVIEW)
                        .eq(VideoHostConfig::getAreaId, areaId)
        );
        VideoConfigCommon common = videoConfigCommonService.getById(id);
        List<VideoConfigUniview> univiewList = videoConfigUniviewMapper.selectList(
                new MyLambdaQueryWrapper<VideoConfigUniview>()
                        .eq(VideoConfigUniview::getVideoConfigCommonId, id)
        );
//            找出host配置
        JSONObject rsa = this.getRSA(areaId);
        String RSAPublicKeyE = rsa.getJSONObject("Response").getJSONObject("Data").getString("RSAPublicKeyE");
        String RSAPublicKeyN = rsa.getJSONObject("Response").getJSONObject("Data").getString("RSAPublicKeyN");
//            遍历集合，将对应的值存入list
        univiewList
                .forEach(uniview -> {
//                        得到取流地址
                    VideoChannelUrlParam RSA = new VideoChannelUrlParam();
                    RSA
                            .setRSAPublicKeyE(RSAPublicKeyE)
                            .setRSAPublicKeyN(RSAPublicKeyN);
                    String channelId = uniview.getChannelId();
                    String streamCode = uniview.getStreamCode();
                    String transType = uniview.getTransType();
                    String protocol = uniview.getProtocol();
                    JSONObject videoChannelUrl = getVideoChannelUrl(RSA, Long.valueOf(channelId), Long.valueOf(streamCode), Long.valueOf(transType), Long.valueOf(protocol), areaId);
                    String url1 = videoChannelUrl
                            .getJSONObject("Response")
                            .getJSONObject("Data")
                            .getString("URL");
                    if (StringUtils.isNotBlank(url1)) {
                        String url = url1.split("//")[0] + "//" + univiewHostConfig.getAppkey() +
                                ":" + univiewHostConfig.getAppSecret() + "@" + url1.split("//")[1];
                        list.add(new VideoInspectionPointLocationInfoParam()
                                .setDeviceId(uniview.getId())
                                .setDeviceType(VideoConst.UNIVIEW)
                                .setName(uniview.getPointName())
                                .setUrl(url)
                                .setOnline(uniview.getOnline())
                                .setVideoSign(videoSign)
                        );
//                        调用推流接口
//                        String conmonUrl = "http://27.223.88.150:9102";
//                        String putStreamUrl = conmonUrl + "/stream-process/putStreamToSelfServer";
//                        Dict dict = Dict.create().
//                                set("url", url).
//                                set("duration", duration != null ? duration : 300).
//                                set("streamAdd", common.getCoaCode()).
//                                set("type", "uniview");
//                        try (HttpResponse response = HttpRequest.post(putStreamUrl).body(
//                                JSON.toJSONString(Collections.singletonList(dict))).execute()) {
//                            response.body();
//                        }
                    }
                });
    }

    /**
     * 大华的，视频巡检用
     *
     * @param id
     * @param areaId
     * @param list
     * @param videoSign
     */
    public void dahua(Long id, Long areaId, List<VideoInspectionPointLocationInfoParam> list, String videoSign) {
        List<VideoConfigDahua7016> dahuaList = videoConfigDahua7016Mapper.selectList(
                new MyLambdaQueryWrapper<VideoConfigDahua7016>()
                        .eq(VideoConfigDahua7016::getVideoConfigCommonId, id)
        );
        //找出host配置
        VideoHostConfig dahuaHostConfig = videoHostConfigMapper.selectOne(
                new MyLambdaQueryWrapper<VideoHostConfig>()
                        .eq(VideoHostConfig::getDeviceType, VideoConst.DAHUA)
                        .eq(VideoHostConfig::getAreaId, areaId)
        );
        dahuaList.forEach(dahua -> list.add(
                new VideoInspectionPointLocationInfoParam()
                        .setName(dahua.getPointName())
                        .setUrl(
                                dahuaHostConfig.getProtocol() +
                                        "://" +
                                        dahuaHostConfig.getHostAddress() +
                                        "/live/cameraid/" +
                                        dahua.getDeviceNumber() +
                                        "%24" +
                                        dahua.getChannelNumber() +
                                        "/substream/" +
                                        dahua.getCodeStreamType() +
                                        ".m3u8"
                        )
                        .setDeviceId(dahua.getId())
                        .setDeviceType(VideoConst.DAHUA)
                        .setOnline(dahua.getOnline())
                        .setVideoSign(videoSign)
        ));
    }

    /**
     * 紫光的，视频巡检用
     *
     * @param id
     * @param areaId
     * @param list
     * @param videoSign
     */
    public void ziguang(Long id, Long areaId, List<VideoInspectionPointLocationInfoParam> list, String videoSign) {
        List<VideoConfigZiguang> ziguangList = videoConfigZiguangMapper.selectList(
                new MyLambdaQueryWrapper<VideoConfigZiguang>()
                        .eq(VideoConfigZiguang::getVideoConfigCommonId, id)
        );
        //            找出host配置
        VideoHostConfig ziguangHostConfig = videoHostConfigMapper.selectOne(
                new MyLambdaQueryWrapper<VideoHostConfig>()
                        .eq(VideoHostConfig::getDeviceType, VideoConst.ZIGUANG)
                        .eq(VideoHostConfig::getAreaId, areaId)
        );
        ziguangList.forEach(ziguang -> list.add(
                new VideoInspectionPointLocationInfoParam()
                        .setName(ziguang.getPointName())
                        .setUrl(
                                ziguangHostConfig.getProtocol() +
                                        "://" +
                                        ziguangHostConfig.getHostAddress() +
                                        "/api/vms/v1/webuas/media-server/0/livestream.m3u8?id=oUWeeHOwGDCxrK8oQS1A" +
                                        "&camera_id=" +
                                        ziguang.getCameraId() +
                                        "&stream_type=" +
                                        ziguang.getStreamType() +
                                        "&keep_alive=" +
                                        ziguang.getKeepAlive() +
                                        "&port_type=0&h265_muxer_type=0" +
                                        "&visit_ip=120.224.158.27"
                        )
                        .setDeviceId(ziguang.getId())
                        .setDeviceType(VideoConst.ZIGUANG)
                        .setOnline(ziguang.getOnline())
                        .setVideoSign(videoSign)
        ));
    }

    /**
     * 其他类型的设备，视频巡查用
     *
     * @param id
     * @param list
     * @param videoSign
     */
    public void other(Long id, List<VideoInspectionPointLocationInfoParam> list, String videoSign) {
        List<VideoConfigOther> otherList = videoConfigOtherMapper.selectList(
                new MyLambdaQueryWrapper<VideoConfigOther>()
                        .eq(VideoConfigOther::getVideoConfigCommonId, id)
        );
        otherList.forEach(other -> list.add(
                new VideoInspectionPointLocationInfoParam()
                        .setDeviceId(other.getId())
                        .setDeviceType(VideoConst.OTHER)
                        .setName(other.getPointName())
                        .setUrl(other.getStreamAddress())
                        .setVideoSign(videoSign))
        );
    }

    /**
     * 内蒙的，视频巡检用
     *
     * @param id
     * @param areaId
     * @param list
     * @param videoSign
     */
    public void ioa(Long id, Long areaId, List<VideoInspectionPointLocationInfoParam> list, String videoSign) {
        List<VideoConfigIoa> ioaList = videoConfigIoaMapper.selectList(
                new MyLambdaQueryWrapper<VideoConfigIoa>()
                        .eq(VideoConfigIoa::getVideoConfigCommonId, id)
        );
//            找出host配置
        VideoHostConfig ioaHostConfig = videoHostConfigMapper.selectOne(
                new MyLambdaQueryWrapper<VideoHostConfig>()
                        .eq(VideoHostConfig::getDeviceType, VideoConst.IOA)
                        .eq(VideoHostConfig::getAreaId, areaId)
        );

//            遍历集合，将对应的值存入list
        ioaList
                .forEach(ioa -> {
//                        得到取流地址
                    String url = getIoaUrl(ioa.getCameraIndexCode(), ioa.getStreamType(), ioa.getProtocol());
                    list.add(
//                                将数据存入列表
                            new VideoInspectionPointLocationInfoParam()
                                    .setDeviceId(ioa.getId())
                                    .setDeviceType(VideoConst.IOA)
                                    .setName(ioa.getPointName())
                                    .setUrl(url)
                                    .setOnline(ioa.getOnline())
                                    .setVideoSign(videoSign)
                    );
                });
    }

    /**
     * 天翼的设备
     *
     * @param id
     * @param videoSign
     * @return
     */
    public void esurfing(Long id, List<VideoInspectionPointLocationInfoParam> list, String videoSign) {
        List<VideoConfigEsurfing> esurfingList = videoConfigEsurfingMapper.selectList(
                new MyLambdaQueryWrapper<VideoConfigEsurfing>()
                        .eq(VideoConfigEsurfing::getVideoConfigCommonId, id)
        );
        esurfingList
                .forEach(esurfing -> {
                    // proto默认为3，表示hls地址
                    //2024.4.17新增要求：getDeviceMediaUrl的supportDomain字段直接传1
                    String mediaUrlData = videoConfigEsurfingService.getDeviceMediaUrl(esurfing.getDeviceCode(), esurfing.getProto(), 1);
                    JSONObject mediaUrlJson = JSONObject.parseObject(mediaUrlData);
                    String url = mediaUrlJson.getString("url");
//                    log.info("返回的data为:{}",mediaUrlData);
                    if (ObjectUtils.isEmpty(url)) {
                        url = "未获取到推流地址，请检查设备在线状态";
                    }
                    list.add(
                            //                                将数据存入列表
                            new VideoInspectionPointLocationInfoParam()
                                    .setDeviceId((esurfing.getId()).intValue())
                                    .setDeviceType(VideoConst.ESURFING)
                                    .setName(esurfing.getDeviceName())
                                    .setUrl(url)
                                    .setOnline(esurfing.getOnlineStatus())
                                    .setVideoSign(videoSign)
                    );
                });
    }

    public void guobiao(Long id, List<VideoInspectionPointLocationInfoParam> list, String videoSign) {
        List<VideoConfigGuobiao> guobiaoList = videoConfigGuobiaoMapper.selectList(
                new MyLambdaQueryWrapper<VideoConfigGuobiao>()
                        .eq(VideoConfigGuobiao::getVideoConfigCommonId, id)
        );

        guobiaoList
                .forEach(i -> {
                    GuoBiaoDevice guoBiaoDevice = new GuoBiaoDevice();
                    guoBiaoDevice.setDeviceId(i.getDeviceId());
                    guoBiaoDevice.setChannelId(i.getChannelId());
                    String mediaUrlData = videoConfigGuobiaoService.getFmp4(guoBiaoDevice);
                    JSONObject mediaUrlJson = JSONObject.parseObject(mediaUrlData);

                    JSONObject data = mediaUrlJson.getJSONObject("data");
                    System.out.println(data);
                    String fmp4 = null;
                    if (ObjectUtils.isEmpty(data)) {
                        fmp4 = "";
                    } else {
//                        fmp4 = data.getString("fmp4");
                        fmp4 = data.getString("flv");
                    }
//                    log.info("返回的data为:{}",mediaUrlData);
                    if (ObjectUtils.isEmpty(fmp4)) {
                        fmp4 = "";
                    }
                    System.out.println(fmp4);
                    list.add(
                            //                                将数据存入列表
                            new VideoInspectionPointLocationInfoParam()
                                    .setDeviceId(i.getId())
                                    .setDeviceType(VideoConst.GUOBIAO)
                                    .setName(i.getName())
                                    .setUrl(fmp4)
                                    .setOnline(i.getOnline())
                                    .setVideoSign(videoSign)
                    );
                });
    }

    private String getIoaUrl(String camaraId, String streamType, String streamMode) {
        String url = VideoConfigIoaServiceImpl.URL_PREFIX + VideoConfigIoaServiceImpl.GET_STREAM;
        Map<String, Object> map = new HashMap<>();
        map.put("camera_id", camaraId);
        map.put("stream_type", streamType);
        map.put("stream_mode", streamMode);
//       都是外网
        map.put("is_local", 1);
        map.put("token", JSONObject.parseObject(videoConfigIoaService.getToken()).get("data").toString());
        try (HttpResponse response = HttpUtil.createGet(url).form(map).execute()) {
            return response.body();
        }
    }

    /**
     * 海康取流方法
     *
     * @param param     海康的基础配置信息
     * @param initParam 初始化的参数
     * @return 取流的url
     */
    public String getPageCamerasByCamerasIndex(HaikangGetStreamParamClass param, InitParam initParam) {
        init(initParam.getHost(), initParam.getAppkey(), initParam.getAppsecret());
        String getCamsApi = ARTEMIS_PATH + "/api/video/v1/cameras/previewURLs";
        Map<String, String> path = Collections.singletonMap(initParam.getProtocol() + "://", getCamsApi);
        HashMap<String, Object> map = new HashMap<>();
        map.put("cameraIndexCode", param.getCameraIndexCode());
        map.put("streamType", param.getStreamType());
        map.put("protocol", param.getProtocol());
        map.put("transmode", param.getTransmode());
        if (param.getProtocol().equals("rtsp")) map.put("expand", "streamform=rtp");
        String body = JSON.toJSONString(map);
        //        post请求application/json类型参数
        String result = ArtemisHttpUtil.doPostStringArtemis(path, body, null, null, "application/json", null);
        log.info("请求路径：" + getCamsApi + ",请求参数：" + body + ",返回结果：" + result);
        //        返回中data是    "url": "rtsp://ip:port/EUrl/CLJ52BW"  这样的一条数据
        //        前端拿到的这条url直接调用
        JSONObject json = JSONObject.parseObject(result);
        // JSONObject data = json.getJSONObject("data");
        // return Objects.isNull(data) ? "" : data.getString("url");
        if (json != null && ObjectUtils.isNotEmpty(json.getJSONObject("data"))) {
            JSONObject data = json.getJSONObject("data");
            return Objects.isNull(data) ? "" : data.getString("url");
        } else {
            return "";
        }
    }

    /**
     * 初始化
     *
     * @param host      host地址
     * @param appkey    秘钥appkey
     * @param appsecret 秘钥appSecret
     */
    public static void init(String host, String appkey, String appsecret) {
//        host地址
        ArtemisConfig.host = host;
//        秘钥appkey
        ArtemisConfig.appKey = appkey;
//        秘钥appSecret
        ArtemisConfig.appSecret = appsecret;
    }

    @Data
    @Accessors(chain = true)
    public static class HaikangGetStreamParamClass {
        //        监控点索引
        private String cameraIndexCode;
        //        码流类型
        private String streamType;
        //        取流协议
        private String protocol;
        //        传输模式，这里固定为TCP
        private final Integer transmode = 1;
    }

    @Data
    @Accessors(chain = true)
    public static class InitParam {
        //
        private String protocol;
        //
        private String host;
        //        key
        private String appkey;
        //        密码
        private String appsecret;
    }

    @Override
    public String generateImageByString(ImageGenerateParam param) throws IOException {
        int width = 207;
        int height = 260;

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics g = image.getGraphics();

        // 设置背景色
        g.setColor(new Color(0.0f, 0.0f, 0.0f, 0.5f));
        g.fillRect(0, 0, width, height);
        //设置边框
//        g.setColor(Color.WHITE);
//        g.drawRect(0, 0, width - 1, height - 1);

        //设置标题底色
        g.setColor(Color.red);
        g.fillRect(0, 0, width, 25);
        // 设置文本
        g.setColor(Color.white);

        g.setFont(new Font("宋体", Font.BOLD, 14));
        // 计算标题的x坐标，使其在给定的矩形内水平居中
        String title = "AI五维定位法";
        int titleX = (width - g.getFontMetrics().stringWidth(title)) / 2;
        g.drawString(title, titleX, 20);
        // 设置其他字段的文本，左对齐并自动换行
        String[] lines = {
                "事项名称：" + param.getItem(),
                "拍摄时间：" + param.getDateTime(),
                "拍摄地点：" + param.getLocation(),
                "经纬度：" + param.getLatitudeAndLongitude(),
                "拍摄人：" + param.getPhotographer()
        };
        int y = 45; // 下一行的y坐标
        for (String line : lines) {
            while (line.length() > 0) {
                int breakPoint = g.getFontMetrics().stringWidth(line) > width ? -1 : line.length();
                if (breakPoint == -1) {
                    breakPoint = findBreakingPoint(line, g.getFontMetrics(), width);
                }
                String subLine = line.substring(0, breakPoint);
                g.drawString(subLine, 10, y); // 留出左边距
                line = line.substring(breakPoint).trim();
                y += g.getFontMetrics().getHeight(); // 移动到下一行
            }
            y += 5;
        }

        // 释放此图形的上下文以及它使用的所有系统资源
        g.dispose();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "png", baos);
        byte[] imageData = baos.toByteArray();
        return Base64.getEncoder().encodeToString(imageData);
    }

    public int findBreakingPoint(String line, FontMetrics fm, int maxWidth) {
        int length = line.length();
        int breakingPoint = -1;
        for (int i = length; i > 0; i--) {
            if (line.charAt(i - 1) == ' ') {
                int substringLength = i;
                // Check if the substring fits within the maxWidth
                if (fm.stringWidth(line.substring(0, substringLength)) <= maxWidth) {
                    breakingPoint = i - 1;
                    break;
                }
            } else {
                // 如果当前字符没有空格，检查是否需要换行
                if (fm.stringWidth(line.substring(0, i)) > maxWidth) {
                    // 找到与图片宽度相同的那个字
                    for (int j = i - 1; j > 0; j--) {
                        if (fm.stringWidth(line.substring(0, j)) <= maxWidth) {
                            breakingPoint = j - 2;
                            break;
                        }
                    }
                    break;
                }
            }
        }
        return breakingPoint;
    }

    /**
     * 根据video_config_common_id查找设备在线数量
     */
    private Long getOnlineNumberByCommonId(Integer videoConfigCommonId) {
        Long onlineNumber = 0L;
        // 海康在线数量
        onlineNumber += videoConfigHaikangiscMapper.selectCount(
                new MyLambdaQueryWrapper<VideoConfigHaikangisc>()
                        .eq(VideoConfigHaikangisc::getVideoConfigCommonId, videoConfigCommonId)
                        .eq(VideoConfigHaikangisc::getOnline, 1)
        );
        // 大华
        onlineNumber += videoConfigDahua7016Mapper.selectCount(
                new MyLambdaQueryWrapper<VideoConfigDahua7016>()
                        .eq(VideoConfigDahua7016::getVideoConfigCommonId, videoConfigCommonId)
                        .eq(VideoConfigDahua7016::getOnline, 1)
        );
        // 天翼
        onlineNumber += videoConfigEsurfingMapper.selectCount(
                new MyLambdaQueryWrapper<VideoConfigEsurfing>()
                        .eq(VideoConfigEsurfing::getVideoConfigCommonId, videoConfigCommonId)
                        .eq(VideoConfigEsurfing::getOnlineStatus, 1)
        );
        //国标
        onlineNumber += videoConfigGuobiaoMapper.selectCount(
                new MyLambdaQueryWrapper<VideoConfigGuobiao>()
                        .eq(VideoConfigGuobiao::getVideoConfigCommonId, videoConfigCommonId)
                        .eq(VideoConfigGuobiao::getOnline, 1)
        );
        // ioa
        onlineNumber += videoConfigIoaMapper.selectCount(
                new MyLambdaQueryWrapper<VideoConfigIoa>()
                        .eq(VideoConfigIoa::getVideoConfigCommonId, videoConfigCommonId)
                        .eq(VideoConfigIoa::getOnline, 1)
        );
        // uniview
        onlineNumber += videoConfigUniviewMapper.selectCount(
                new MyLambdaQueryWrapper<VideoConfigUniview>()
                        .eq(VideoConfigUniview::getVideoConfigCommonId, videoConfigCommonId)
                        .eq(VideoConfigUniview::getOnline, 1)
        );
        return onlineNumber;
    }



    /**
     * 1、先判断登录用户的身份，根据用户类型是1，enterprise_user_type为-1时是承建单位，
     * 根据当前登录用户的美丽乡村建设单位表id取出他的社会信用代码，去匹配village_construction_project_details表里面的承建单位信用代码，
     * 晒选出来这条数据的项目编号，然后再比对视频配置表的项目编号
     * 2、先判断登录用户的身份，根据用户类型是1，enterprise_user_type为1,2,3,4时是市场主体，
     * 根据当前登录用户的市场主体表id去市场主体表取出社会信用代码，去匹配视频配置表里面的社会信用代码即可
     *
     * @return 项目编号/社会信用代码
     */
    @Override
    public List<String> judgeAuthority() {
        List<String> resultList = new ArrayList<>();
        Integer userType = UserUtils.get().getUserType();
        Integer enterpriseUserType = UserUtils.get().getEnterpriseUserType();
        if (Objects.equals(userType, 1)) {
            if (Objects.equals(enterpriseUserType, -1)) {
                Long villageConstructionUnitId = UserUtils.get().getVillageConstructionUnitId();
                VillageConstructionUnit villageConstructionUnit = villageConstructionUnitMapper.selectById(villageConstructionUnitId);
                if (ObjectUtils.isNotEmpty(villageConstructionUnit)) {
                    String socialCreditCode = villageConstructionUnit.getSocialCreditCode();
                    List<VillageConstructionProjectDetails> villageConstructionProjectDetailsList = villageConstructionProjectDetailsMapper.selectList(
                            new MyLambdaQueryWrapper<VillageConstructionProjectDetails>()
                                    .eq(ObjectUtils.isNotEmpty(socialCreditCode), VillageConstructionProjectDetails::getConstructionCreditCode, socialCreditCode)
                    );
                    if (CollectionUtils.isNotEmpty(villageConstructionProjectDetailsList)) {
                        villageConstructionProjectDetailsList.forEach(i -> {
                            String projectNumber = i.getProjectNumber();
                            if (ObjectUtils.isNotEmpty(projectNumber)) {
                                resultList.add(projectNumber);
                            }
                        });

                    }
                }
            } else if (Objects.equals(enterpriseUserType, 1) || Objects.equals(enterpriseUserType, 2) || Objects.equals(enterpriseUserType, 3) || Objects.equals(enterpriseUserType, 4)) {
                Long supEnterpriseId = UserUtils.get().getSupEnterpriseId();
                SupEnterprise supEnterprise = supEnterpriseMapper.selectById(supEnterpriseId);
                if (ObjectUtils.isNotEmpty(supEnterprise)) {
                    String socialCreditCode = supEnterprise.getSocialCreditCode();
                    if (ObjectUtils.isNotEmpty(socialCreditCode)) {
                        resultList.add(socialCreditCode);
                    }
                }
            }
        }
        return resultList;
    }
}
