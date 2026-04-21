package upc.c505.modular.villageiot.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hikvision.artemis.sdk.ArtemisHttpUtil;
import com.hikvision.artemis.sdk.config.ArtemisConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import upc.c505.common.UserUtils;
import upc.c505.common.requestparam.PageBaseSearchParam;
import upc.c505.common.wrapper.MyLambdaQueryWrapper;
import upc.c505.exception.BusinessException;
import upc.c505.modular.auth.service.ISysAreaService;
import upc.c505.modular.employeetrain.exception.EmBusinessError;
import upc.c505.modular.partybuilding.entity.PartyBuildingOrganizationThird;
import upc.c505.modular.partybuilding.mapper.PartyBuildingOrganizationThirdMapper;
import upc.c505.modular.people.service.IPeopleGovernmentService;
import upc.c505.modular.supenterprise.entity.SupEnterprise;
import upc.c505.modular.supenterprise.mapper.SupEnterpriseMapper;
import upc.c505.modular.village.entity.VillageConstructionProjectDetails;
import upc.c505.modular.village.mapper.VillageConstructionProjectDetailsMapper;
import upc.c505.modular.villageiot.controller.param.*;
import upc.c505.modular.villageiot.entity.*;
import upc.c505.modular.villageiot.mapper.*;
import upc.c505.modular.villageiot.service.IVideoConfigCommonService;
import upc.c505.modular.villageiot.service.IVideoConfigDahua7016Service;
import upc.c505.modular.villageiot.service.IVideoConfigHaikangiscService;

import java.util.*;

import static upc.c505.modular.villageiot.controller.param.VideoConst.*;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author frd
 * @since 2024-01-19
 */
@Service
public class VideoConfigCommonServiceImpl extends ServiceImpl<VideoConfigCommonMapper, VideoConfigCommon> implements IVideoConfigCommonService {

    private static final String ARTEMIS_PATH = "/artemis";
    @Autowired
    private VideoConfigCommonMapper videoConfigCommonMapper;

    @Autowired
    private IVideoConfigCommonService videoConfigCommonService;

    @Autowired
    private IVideoConfigHaikangiscService videoConfigHaikangiscService;

    @Autowired
    private IVideoConfigDahua7016Service videoConfigDahua7016Service;

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
    private VideoConfigUniviewMapper videoConfigUniviewMapper;

    @Autowired
    private VideoConfigZiguangMapper videoConfigZiguangMapper;

    @Autowired
    private VideoConfigOtherMapper videoConfigOtherMapper;
    @Autowired
    private VideoHostConfigMapper videoHostConfigMapper;

    @Autowired
    private SupEnterpriseMapper supEnterpriseMapper;

    @Autowired
    private VillageConstructionProjectDetailsMapper villageConstructionProjectDetailsMapper;

    @Autowired
    private PartyBuildingOrganizationThirdMapper partyBuildingOrganizationThirdMapper;

    @Autowired
    private IPeopleGovernmentService peopleGovernmentService;

    @Autowired
    private ISysAreaService sysAreaService;

    @Override
    @Transactional
    public Boolean addOneVideoCommon(VideoConfigAddParam videoConfigAddParam) {
        //先保存设备配置标的基本信息
        VideoConfigCommon videoConfigCommon = videoConfigAddParam.getVideoConfigCommon();
        videoConfigCommonService.saveOrUpdate(videoConfigCommon);
        Integer id = videoConfigCommon.getId();
        if (ObjectUtils.isEmpty(id)) {
            throw new BusinessException(EmBusinessError.COUREE_ERROR, "保存失败");
        }
        Long currentDeviceNumber = 0L;
        //然后保存不同设备表里面的设备（海康表、大华表）
        //当设备类型是海康时
        if (ObjectUtils.isNotEmpty(videoConfigAddParam.getHaikangiscList()) && Objects.equals(HAIKANG, videoConfigCommon.getDeviceType())) {
            List<VideoConfigHaikangisc> haikangiscList = videoConfigAddParam.getHaikangiscList();
            haikangiscList.forEach(i -> {
                i.setVideoConfigCommonId(id);
                i.setId(null);
            });
            videoConfigHaikangiscService.saveBatch(haikangiscList);
            currentDeviceNumber += videoConfigHaikangiscMapper.selectCount(new MyLambdaQueryWrapper<VideoConfigHaikangisc>()
                    .eq(VideoConfigHaikangisc::getVideoConfigCommonId, id));
        }
        //当设备类型是大华时
        else if (ObjectUtils.isNotEmpty(videoConfigAddParam.getDahua7016List()) && Objects.equals(DAHUA, videoConfigCommon.getDeviceType())) {
            List<VideoConfigDahua7016> dahua7016List = videoConfigAddParam.getDahua7016List();
            dahua7016List.forEach(i -> {
                i.setVideoConfigCommonId(id);
                i.setId(null);
            });
            videoConfigDahua7016Service.saveBatch(dahua7016List);
            currentDeviceNumber += videoConfigDahua7016Mapper.selectCount(new MyLambdaQueryWrapper<VideoConfigDahua7016>()
                    .eq(VideoConfigDahua7016::getVideoConfigCommonId, id));
        }
        //当设备类型是天翼时
        else if (ObjectUtils.isNotEmpty(videoConfigAddParam.getEsurfingList()) && Objects.equals(VideoConst.ESURFING, videoConfigCommon.getDeviceType())) {
            List<VideoConfigEsurfing> esurfingList = videoConfigAddParam.getEsurfingList();
            esurfingList.forEach(i -> i.setVideoConfigCommonId(id).setId(null));
            currentDeviceNumber += videoConfigEsurfingMapper.selectCount(new MyLambdaQueryWrapper<VideoConfigEsurfing>()
                    .eq(VideoConfigEsurfing::getVideoConfigCommonId, id));
        }
        //当设备类型是国标时
        else if (ObjectUtils.isNotEmpty(videoConfigAddParam.getGuobiaoList()) && Objects.equals(VideoConst.GUOBIAO, videoConfigCommon.getDeviceType())) {
            List<VideoConfigGuobiao> guobiaoList = videoConfigAddParam.getGuobiaoList();
            guobiaoList.forEach(i -> i.setVideoConfigCommonId(id).setId(null));
            currentDeviceNumber += videoConfigGuobiaoMapper.selectCount(new MyLambdaQueryWrapper<VideoConfigGuobiao>()
                    .eq(VideoConfigGuobiao::getVideoConfigCommonId, id));
        }
        //当设备类型是内蒙时
        else if (ObjectUtils.isNotEmpty(videoConfigAddParam.getIoaList()) && Objects.equals(VideoConst.IOA, videoConfigCommon.getDeviceType())) {
            List<VideoConfigIoa> ioaList = videoConfigAddParam.getIoaList();
            ioaList.forEach(i -> i.setVideoConfigCommonId(id).setId(null));
            currentDeviceNumber += videoConfigIoaMapper.selectCount(new MyLambdaQueryWrapper<VideoConfigIoa>()
                    .eq(VideoConfigIoa::getVideoConfigCommonId, id));
        }
        //当设备类型是宇视时
        else if (ObjectUtils.isNotEmpty(videoConfigAddParam.getUniviewList()) && Objects.equals(UNIVIEW, videoConfigCommon.getDeviceType())) {
            List<VideoConfigUniview> univiewList = videoConfigAddParam.getUniviewList();
            univiewList.forEach(i -> i.setVideoConfigCommonId(id).setId(null));
            currentDeviceNumber += videoConfigUniviewMapper.selectCount(new MyLambdaQueryWrapper<VideoConfigUniview>()
                    .eq(VideoConfigUniview::getVideoConfigCommonId, id));
        }
        //当设备类型是紫光时
        else if (ObjectUtils.isNotEmpty(videoConfigAddParam.getZiguangList()) && Objects.equals(VideoConst.ZIGUANG, videoConfigCommon.getDeviceType())) {
            List<VideoConfigZiguang> ziguangList = videoConfigAddParam.getZiguangList();
            ziguangList.forEach(i -> i.setVideoConfigCommonId(id).setId(null));
            currentDeviceNumber += videoConfigZiguangMapper.selectCount(new MyLambdaQueryWrapper<VideoConfigZiguang>()
                    .eq(VideoConfigZiguang::getVideoConfigCommonId, id));
        }
        //当设备类型是其他时
        else {
            List<VideoConfigOther> otherList = videoConfigAddParam.getOtherList();
            otherList.forEach(i -> i.setVideoConfigCommonId(id).setId(null));
            currentDeviceNumber += videoConfigOtherMapper.selectCount(new MyLambdaQueryWrapper<VideoConfigOther>()
                    .eq(VideoConfigOther::getVideoConfigCommonId, id));
        }
        //然后保存当前设备数量
        videoConfigCommon.setCurrentDeviceNumber(Math.toIntExact(currentDeviceNumber));
        videoConfigCommonMapper.updateById(videoConfigCommon);
        return true;
    }

    @Override
    @Transactional
    public Boolean updateOneVideoCommon(VideoConfigUpdateParam videoConfigUpdateParam) {
        Integer id = videoConfigUpdateParam.getId();
        VideoConfigCommon videoConfigCommon = videoConfigCommonMapper.selectById(id);
        String oldDeviceType = videoConfigCommon.getDeviceType();
        String newDeviceType = videoConfigUpdateParam.getDeviceType();
        //比较两次的设备类型，如果不同那么就在设备表中删除原来所有的设备，并将设备类型更改为新的设备类型
        if (!Objects.equals(oldDeviceType, newDeviceType)) {
            deletePointByIdAndDeviceType(id, oldDeviceType);
            videoConfigCommon.setDeviceType(newDeviceType);
        }
        if (HAIKANG.equals(newDeviceType)) {
            //先删除所有数据
            videoConfigHaikangiscMapper.delete(new MyLambdaQueryWrapper<VideoConfigHaikangisc>()
                    .eq(VideoConfigHaikangisc::getVideoConfigCommonId, id));
            VideoConfigAddParam videoConfigAddParam = new VideoConfigAddParam();
            videoConfigAddParam.setVideoConfigCommon(videoConfigCommon)
                    .setHaikangiscList(videoConfigUpdateParam.getHaikangiscList());
            this.addOneVideoCommon(videoConfigAddParam);
        } else if (DAHUA.equals(newDeviceType)) {
            //先删除所有数据
            videoConfigDahua7016Mapper.delete(new MyLambdaQueryWrapper<VideoConfigDahua7016>()
                    .eq(VideoConfigDahua7016::getVideoConfigCommonId, id));
            VideoConfigAddParam videoConfigAddParam = new VideoConfigAddParam();
            videoConfigAddParam.setVideoConfigCommon(videoConfigCommon)
                    .setDahua7016List(videoConfigUpdateParam.getDahua7016List());
            this.addOneVideoCommon(videoConfigAddParam);
        } else if (ESURFING.equals(newDeviceType)) {
            //先删除所有数据
            videoConfigEsurfingMapper.delete(new MyLambdaQueryWrapper<VideoConfigEsurfing>()
                    .eq(VideoConfigEsurfing::getVideoConfigCommonId, id));
            VideoConfigAddParam videoConfigAddParam = new VideoConfigAddParam();
            videoConfigAddParam.setVideoConfigCommon(videoConfigCommon)
                    .setEsurfingList(videoConfigUpdateParam.getEsurfingList());
            this.addOneVideoCommon(videoConfigAddParam);
        } else if (GUOBIAO.equals(newDeviceType)) {
            //先删除所有数据
            videoConfigGuobiaoMapper.delete(new MyLambdaQueryWrapper<VideoConfigGuobiao>()
                    .eq(VideoConfigGuobiao::getVideoConfigCommonId, id));
            VideoConfigAddParam videoConfigAddParam = new VideoConfigAddParam();
            videoConfigAddParam.setVideoConfigCommon(videoConfigCommon)
                    .setGuobiaoList(videoConfigUpdateParam.getGuobiaoList());
            this.addOneVideoCommon(videoConfigAddParam);
        } else if (IOA.equals(newDeviceType)) {
            //先删除所有数据
            videoConfigIoaMapper.delete(new MyLambdaQueryWrapper<VideoConfigIoa>()
                    .eq(VideoConfigIoa::getVideoConfigCommonId, id));
            VideoConfigAddParam videoConfigAddParam = new VideoConfigAddParam();
            videoConfigAddParam.setVideoConfigCommon(videoConfigCommon)
                    .setIoaList(videoConfigUpdateParam.getIoaList());
            this.addOneVideoCommon(videoConfigAddParam);
        } else if (UNIVIEW.equals(newDeviceType)) {
            //先删除所有数据
            videoConfigUniviewMapper.delete(new MyLambdaQueryWrapper<VideoConfigUniview>()
                    .eq(VideoConfigUniview::getVideoConfigCommonId, id));
            VideoConfigAddParam videoConfigAddParam = new VideoConfigAddParam();
            videoConfigAddParam.setVideoConfigCommon(videoConfigCommon)
                    .setIoaList(videoConfigUpdateParam.getIoaList());
            this.addOneVideoCommon(videoConfigAddParam);
        } else if (ZIGUANG.equals(newDeviceType)) {
            //先删除所有数据
            videoConfigZiguangMapper.delete(new MyLambdaQueryWrapper<VideoConfigZiguang>()
                    .eq(VideoConfigZiguang::getVideoConfigCommonId, id));
            VideoConfigAddParam videoConfigAddParam = new VideoConfigAddParam();
            videoConfigAddParam.setVideoConfigCommon(videoConfigCommon)
                    .setZiguangList(videoConfigUpdateParam.getZiguangList());
            this.addOneVideoCommon(videoConfigAddParam);
        } else {
            //先删除所有数据
            videoConfigOtherMapper.delete(new MyLambdaQueryWrapper<VideoConfigOther>()
                    .eq(VideoConfigOther::getVideoConfigCommonId, id));
            VideoConfigAddParam videoConfigAddParam = new VideoConfigAddParam();
            videoConfigAddParam.setVideoConfigCommon(videoConfigCommon)
                    .setOtherList(videoConfigUpdateParam.getOtherList());
            this.addOneVideoCommon(videoConfigAddParam);
        }
        return true;
    }

    private void deletePointByIdAndDeviceType(Integer id, String deviceType) {
        if (Objects.equals(deviceType, HAIKANG)) {
            videoConfigHaikangiscMapper.delete(new MyLambdaQueryWrapper<VideoConfigHaikangisc>().eq(VideoConfigHaikangisc::getVideoConfigCommonId, id));
        } else if (Objects.equals(deviceType, DAHUA)) {
            videoConfigDahua7016Mapper.delete(new MyLambdaQueryWrapper<VideoConfigDahua7016>().eq(VideoConfigDahua7016::getVideoConfigCommonId, id));
        } else if (Objects.equals(deviceType, ESURFING)) {
            videoConfigEsurfingMapper.delete(new MyLambdaQueryWrapper<VideoConfigEsurfing>().eq(VideoConfigEsurfing::getVideoConfigCommonId, id));
        } else if (Objects.equals(deviceType, GUOBIAO)) {
            videoConfigGuobiaoMapper.delete(new MyLambdaQueryWrapper<VideoConfigGuobiao>().eq(VideoConfigGuobiao::getVideoConfigCommonId, id));
        } else if (Objects.equals(deviceType, IOA)) {
            videoConfigIoaMapper.delete(new MyLambdaQueryWrapper<VideoConfigIoa>().eq(VideoConfigIoa::getVideoConfigCommonId, id));
        } else if (Objects.equals(deviceType, UNIVIEW)) {
            videoConfigUniviewMapper.delete(new MyLambdaQueryWrapper<VideoConfigUniview>().eq(VideoConfigUniview::getVideoConfigCommonId, id));
        } else if (Objects.equals(deviceType, ZIGUANG)) {
            videoConfigZiguangMapper.delete(new MyLambdaQueryWrapper<VideoConfigZiguang>().eq(VideoConfigZiguang::getVideoConfigCommonId, id));
        } else {
            videoConfigOtherMapper.delete(new MyLambdaQueryWrapper<VideoConfigOther>().eq(VideoConfigOther::getVideoConfigCommonId, id));
        }
    }

    @Override
    public JSONObject getHikVedioList(PageBaseSearchParam pageQuery) {
        MyLambdaQueryWrapper<VideoHostConfig> lambdaQueryWrapper = new MyLambdaQueryWrapper<>();
        /**
         * 如果查询的是管辖区域
         *         1、如果传入areaId不为空：判断传入的areaId是不是在当前用户的管辖区域内，
         *         如果传入的areaId合法，当flag为0（或者flag为空）时只查数据库中和传入areaId相等的数据，
         *         当flag为1时先查当前用户的管辖区域，然后再查sys_area表里面传入areaId的子区域，
         *         最后把两者的交集里面的areaId拿出来，到数据库里面查areaId在交集里面的数据。
         *         2、如果传入的areaId为空，那就查询该用户管辖区域的数据。
         */
        if (ObjectUtils.isNotEmpty(pageQuery.getIsApplicableArea()) && pageQuery.getIsApplicableArea() == 0) {
            //获取当前用户的管辖区域列表
            List<Long> list = peopleGovernmentService.getManageAreaIdList();
            if (ObjectUtils.isNotEmpty(pageQuery.getAreaId())) {
                //如果传入的areaId不合法，直接返回空的页面
                if (!list.contains(pageQuery.getAreaId())) {
                    return new JSONObject();
                }
                //当传入的areaId合法时
                // 当flag为0（或者flag为空）时只查数据库中和传入areaId相等的数据
                if (ObjectUtils.isEmpty(pageQuery.getFlag()) || pageQuery.getFlag() == 0) {
                    lambdaQueryWrapper.eq(VideoHostConfig::getAreaId, pageQuery.getAreaId());
                }
                //当flag为1时先查当前用户的管辖区域，然后再查sys_area表里面传入areaId的子区域，
                // 最后把两者的交集里面的areaId拿出来，到数据库里面查areaId在交集里面的数据。
                if (ObjectUtils.isNotEmpty(pageQuery.getFlag()) && pageQuery.getFlag() == 1) {
                    List<Long> areaIdList = sysAreaService.getChildAreaIdList(pageQuery.getAreaId());
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
        if (ObjectUtils.isNotEmpty(pageQuery.getIsApplicableArea()) && pageQuery.getIsApplicableArea() == 1) {
            List<Long> areaIdList = sysAreaService.getChildAreaIdList(UserUtils.get().getAreaId());
            if (ObjectUtils.isNotEmpty(pageQuery.getAreaId())) {
                //如果传入的areaId不合法，直接返回空的页面
                if (!areaIdList.contains(pageQuery.getAreaId())) {
                    return new JSONObject();
                }
                //如果传入的areaId合法，当flag为0（或者flag为空）时只查数据库中和传入areaId相等的数据，当flag为1时查询传入areaId及其子区域的数据。
                if (ObjectUtils.isEmpty(pageQuery.getFlag()) || pageQuery.getFlag() == 0) {
                    lambdaQueryWrapper.eq(VideoHostConfig::getAreaId, pageQuery.getAreaId());
                }
                if (ObjectUtils.isNotEmpty(pageQuery.getFlag()) && pageQuery.getFlag() == 1) {
                    areaIdList = sysAreaService.getChildAreaIdList(pageQuery.getAreaId());
                    lambdaQueryWrapper.in(VideoHostConfig::getAreaId, areaIdList);
                }
            } else {
                lambdaQueryWrapper.eq(VideoHostConfig::getAreaId, UserUtils.get().getAreaId());
            }
        }
        VideoHostConfig haikangHostConfig = videoHostConfigMapper.selectOne(
                lambdaQueryWrapper
                        .eq(VideoHostConfig::getDeviceType, HAIKANG)
        );
        init(haikangHostConfig.getHostAddress(), haikangHostConfig.getAppkey(), haikangHostConfig.getAppSecret());
//        init("112.6.36.78:1443","28025362","HXMiKA7yeOmCmC4IOCPa");
        String http = haikangHostConfig.getProtocol() + "://";
        String getCamsApi = ARTEMIS_PATH + "/api/resource/v1/regions";
        Map<String, String> path = new HashMap<String, String>(2) {
            {
                put(http, getCamsApi);//根据现场环境部署确认是http还是https
            }
        };
        JSONObject jsonBody = new JSONObject();
        jsonBody.put("pageNo", pageQuery.getCurrent());
        jsonBody.put("pageSize", pageQuery.getSize());
        String body = jsonBody.toJSONString();

        String result = ArtemisHttpUtil.doPostStringArtemis(path, body, null, null, "application/json", null);// post请求application/json类型参数
        System.out.println("请求路径：" + getCamsApi + ",请求参数：" + body + ",返回结果：" + result);
        //result.replaceAll("//","");
        JSONObject json = JSONObject.parseObject(result);
        return json;
    }

    @Override
    public Page<VideoUnitReturnParam> selectVideoUnitPage(VideoUnitSearchParam param) {
        MyLambdaQueryWrapper<SupEnterprise> lambdaQueryWrapper1 = new MyLambdaQueryWrapper<>();
        MyLambdaQueryWrapper<VillageConstructionProjectDetails> lambdaQueryWrapper2 = new MyLambdaQueryWrapper<>();
        MyLambdaQueryWrapper<PartyBuildingOrganizationThird> lambdaQueryWrapper3 = new MyLambdaQueryWrapper<>();
        //权限
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
                    lambdaQueryWrapper1.eq(SupEnterprise::getAreaId, param.getAreaId());
                    lambdaQueryWrapper2.eq(VillageConstructionProjectDetails::getAreaId, param.getAreaId());
                    lambdaQueryWrapper3.eq(PartyBuildingOrganizationThird::getAreaId, param.getAreaId());
                }
                //当flag为1时先查当前用户的管辖区域，然后再查sys_area表里面传入areaId的子区域，
                // 最后把两者的交集里面的areaId拿出来，到数据库里面查areaId在交集里面的数据。
                if (ObjectUtils.isNotEmpty(param.getFlag()) && param.getFlag() == 1) {
                    List<Long> areaIdList = sysAreaService.getChildAreaIdList(param.getAreaId());
                    list.retainAll(areaIdList);
                    lambdaQueryWrapper1.in(SupEnterprise::getAreaId, list);
                    lambdaQueryWrapper2.in(VillageConstructionProjectDetails::getAreaId, list);
                    lambdaQueryWrapper3.in(PartyBuildingOrganizationThird::getAreaId, list);
                }
            } else {
                lambdaQueryWrapper1.in(SupEnterprise::getAreaId, list);
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
                    lambdaQueryWrapper1.eq(SupEnterprise::getAreaId, param.getAreaId());
                    lambdaQueryWrapper2.eq(VillageConstructionProjectDetails::getAreaId, param.getAreaId());
                    lambdaQueryWrapper3.eq(PartyBuildingOrganizationThird::getAreaId, param.getAreaId());
                }
                if (ObjectUtils.isNotEmpty(param.getFlag()) && param.getFlag() == 1) {
                    areaIdList = sysAreaService.getChildAreaIdList(param.getAreaId());
                    lambdaQueryWrapper1.in(SupEnterprise::getAreaId, areaIdList);
                    lambdaQueryWrapper2.in(VillageConstructionProjectDetails::getAreaId, areaIdList);
                    lambdaQueryWrapper3.in(PartyBuildingOrganizationThird::getAreaId, areaIdList);
                }
            } else {
                lambdaQueryWrapper1.eq(SupEnterprise::getAreaId, UserUtils.get().getAreaId());
                lambdaQueryWrapper2.eq(VillageConstructionProjectDetails::getAreaId, UserUtils.get().getAreaId());
                lambdaQueryWrapper3.eq(PartyBuildingOrganizationThird::getAreaId, UserUtils.get().getAreaId());
            }
        }

        String monitorAttributes = param.getMonitorAttributes();
        List<VideoUnitReturnParam> videoUnitReturnParamList = new ArrayList<>();
        //如果查询的是重点单位，从市场主体表查询
        if (Objects.equals(monitorAttributes, VideoConst.UNIT)) {
            if (ObjectUtils.isNotEmpty(param.getProjectType())) {
                if (Objects.equals(param.getProjectType(), "newFarming")) {
                    lambdaQueryWrapper1.eq(SupEnterprise::getEnterpriseType, "1");
                } else if (Objects.equals(param.getProjectType(), "nine")) {
                    lambdaQueryWrapper1.eq(SupEnterprise::getEnterpriseType, "2");
                } else if (Objects.equals(param.getProjectType(), "ordinary")) {
                    lambdaQueryWrapper1.eq(SupEnterprise::getEnterpriseType, "3");
                } else if (Objects.equals(param.getProjectType(), "property")) {
                    lambdaQueryWrapper1.eq(SupEnterprise::getEnterpriseType, "4");
                }
            }
            List<SupEnterprise> supEnterpriseList = supEnterpriseMapper.selectList(
                    lambdaQueryWrapper1
                            .select(SupEnterprise::getId, SupEnterprise::getSupEnterpriseName, SupEnterprise::getSocialCreditCode, SupEnterprise::getAreaId)
                            .like(ObjectUtils.isNotEmpty(param.getSearchParam()), SupEnterprise::getSupEnterpriseName, param.getSearchParam()));
            supEnterpriseList.forEach(i -> {
                VideoUnitReturnParam videoUnitReturnParam = new VideoUnitReturnParam();
                videoUnitReturnParam.setVideoUnitId(i.getId())
                        .setVideoUnitNumber(i.getSocialCreditCode())
                        .setVideoUnitName(i.getSupEnterpriseName())
                        .setAreaId(i.getAreaId());
                videoUnitReturnParamList.add(videoUnitReturnParam);
            });
            Page<VideoUnitReturnParam> page = new Page<>(param.getCurrent(), param.getSize(), videoUnitReturnParamList.size());
            page.setRecords(videoUnitReturnParamList);
            return page;
        } else if (Objects.equals(monitorAttributes, VideoConst.PROJECT)) {
            List<VillageConstructionProjectDetails> villageConstructionProjectDetailsList = villageConstructionProjectDetailsMapper.selectList(
                    lambdaQueryWrapper2
                            .select(VillageConstructionProjectDetails::getId, VillageConstructionProjectDetails::getProjectNumber, VillageConstructionProjectDetails::getProjectName, VillageConstructionProjectDetails::getAreaId)
                            .eq(ObjectUtils.isNotEmpty(param.getProjectType()), VillageConstructionProjectDetails::getProjectType, param.getProjectType())
                            .like(ObjectUtils.isNotEmpty(param.getSearchParam()), VillageConstructionProjectDetails::getProjectName, param.getSearchParam()));
            villageConstructionProjectDetailsList.forEach(i -> {
                VideoUnitReturnParam videoUnitReturnParam = new VideoUnitReturnParam();
                videoUnitReturnParam.setVideoUnitId(i.getId())
                        .setVideoUnitNumber(i.getProjectNumber())
                        .setVideoUnitName(i.getProjectName())
                        .setAreaId(i.getAreaId());
                videoUnitReturnParamList.add(videoUnitReturnParam);
            });
            Page<VideoUnitReturnParam> page = new Page<>(param.getCurrent(), param.getSize(), videoUnitReturnParamList.size());
            page.setRecords(videoUnitReturnParamList);
            return page;
        } else if (Objects.equals(monitorAttributes, VideoConst.PARTY)) {
            List<PartyBuildingOrganizationThird> partyBuildingOrganizationThirdList = partyBuildingOrganizationThirdMapper.selectList(
                    lambdaQueryWrapper3
                            .select(PartyBuildingOrganizationThird::getId, PartyBuildingOrganizationThird::getThirdName, PartyBuildingOrganizationThird::getAreaId)
                            .like(ObjectUtils.isNotEmpty(param.getSearchParam()), PartyBuildingOrganizationThird::getThirdName, param.getSearchParam()));
            partyBuildingOrganizationThirdList.forEach(i -> {
                VideoUnitReturnParam videoUnitReturnParam = new VideoUnitReturnParam();
                videoUnitReturnParam.setVideoUnitId(i.getId())
                        .setVideoUnitNumber(String.valueOf(i.getId()))
                        .setVideoUnitName(i.getThirdName())
                        .setAreaId(i.getAreaId());
                videoUnitReturnParamList.add(videoUnitReturnParam);
            });
            Page<VideoUnitReturnParam> page = new Page<>(param.getCurrent(), param.getSize(), videoUnitReturnParamList.size());
            page.setRecords(videoUnitReturnParamList);
            return page;
        }
        return new Page<>();
    }

    @Override
    public void deletePointById(Integer id, List<Integer> pointIds) {
        VideoConfigCommon common = videoConfigCommonMapper.selectById(id);
        if (ObjectUtils.isEmpty(common)) {
            throw new BusinessException(EmBusinessError.ID_ERROR);
        }
        if (HAIKANG.equals(common.getDeviceType())) {
            videoConfigHaikangiscMapper.deleteBatchIds(pointIds);
        } else if (UNIVIEW.equals(common.getDeviceType())) {
            videoConfigUniviewMapper.deleteBatchIds(pointIds);
        } else if (DAHUA.equals(common.getDeviceType())) {
            videoConfigDahua7016Mapper.deleteBatchIds(pointIds);
        } else if (ESURFING.equals(common.getDeviceType())) {
            videoConfigEsurfingMapper.deleteBatchIds(pointIds);
        } else if (GUOBIAO.equals(common.getDeviceType())) {
            videoConfigGuobiaoMapper.deleteBatchIds(pointIds);
        } else if (IOA.equals(common.getDeviceType())) {
            videoConfigIoaMapper.deleteBatchIds(pointIds);
        } else if (ZIGUANG.equals(common.getDeviceType())) {
            videoConfigZiguangMapper.deleteBatchIds(pointIds);
        } else {
            videoConfigOtherMapper.deleteBatchIds(pointIds);
        }
    }

    @Override
    public void deleteVideoConfigById(Integer id) {
        //根据id获取设备类型，然后获取对应的摄像头id列表，删除
        VideoConfigCommon common = videoConfigCommonMapper.selectById(id);
        if (ObjectUtils.isEmpty(common)) {
            throw new BusinessException(EmBusinessError.ID_ERROR);
        }
        //先删除关联摄像头
        if (HAIKANG.equals(common.getDeviceType())) {
            videoConfigHaikangiscMapper.delete(new MyLambdaQueryWrapper<VideoConfigHaikangisc>()
                    .eq(VideoConfigHaikangisc::getVideoConfigCommonId, id));
        } else if (UNIVIEW.equals(common.getDeviceType())) {
            videoConfigUniviewMapper.delete(new MyLambdaQueryWrapper<VideoConfigUniview>()
                    .eq(VideoConfigUniview::getVideoConfigCommonId, id));
        } else if (DAHUA.equals(common.getDeviceType())) {
            videoConfigDahua7016Mapper.delete(new MyLambdaQueryWrapper<VideoConfigDahua7016>()
                    .eq(VideoConfigDahua7016::getVideoConfigCommonId, id));
        } else if (ESURFING.equals(common.getDeviceType())) {
            videoConfigEsurfingMapper.delete(new MyLambdaQueryWrapper<VideoConfigEsurfing>()
                    .eq(VideoConfigEsurfing::getVideoConfigCommonId, id));
        } else if (GUOBIAO.equals(common.getDeviceType())) {
            videoConfigGuobiaoMapper.delete(new MyLambdaQueryWrapper<VideoConfigGuobiao>()
                    .eq(VideoConfigGuobiao::getVideoConfigCommonId, id));
        } else if (IOA.equals(common.getDeviceType())) {
            videoConfigIoaMapper.delete(new MyLambdaQueryWrapper<VideoConfigIoa>()
                    .eq(VideoConfigIoa::getVideoConfigCommonId, id));
        } else if (ZIGUANG.equals(common.getDeviceType())) {
            videoConfigZiguangMapper.delete(new MyLambdaQueryWrapper<VideoConfigZiguang>()
                    .eq(VideoConfigZiguang::getVideoConfigCommonId, id));
        } else {
            videoConfigOtherMapper.delete(new MyLambdaQueryWrapper<VideoConfigOther>()
                    .eq(VideoConfigOther::getVideoConfigCommonId, id));
        }
        //再删除视频配置项
        videoConfigCommonMapper.deleteById(id);
    }

    @Override
    public Page<VideoConfigCommon> selectVideoConfigPage(VideoConfigPageSearchParam param) {
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
        //因为searchParam判断的这两个条件是”或“的关系，因此要单独放在一个and里面再进行“或”操作
        lambdaQueryWrapper
                .eq(ObjectUtils.isNotEmpty(param.getMonitorAttributes()), VideoConfigCommon::getMonitorAttributes, param.getMonitorAttributes())
                .eq(ObjectUtils.isNotEmpty(param.getDeviceType()), VideoConfigCommon::getDeviceType, param.getDeviceType())
                .and(ObjectUtils.isNotEmpty(param.getSearchParam()), wrapper -> {
                    wrapper.like(VideoConfigCommon::getMonitorGroupName, param.getSearchParam())
                            .or()
                            .like(VideoConfigCommon::getEntCreditCode, param.getSearchParam());
                })
                .orderBy(true, Objects.equals(param.getIsAsc(), 1), VideoConfigCommon::getAddDatetime);
        return videoConfigCommonMapper.selectPage(new Page<>(param.getCurrent(), param.getSize()), lambdaQueryWrapper);
    }

    @Override
    public VideoConfigReturnParam selectVideoConfigById(Long id) {
        VideoConfigCommon common = videoConfigCommonMapper.selectById(id);
        VideoConfigReturnParam returnParam = new VideoConfigReturnParam();
        returnParam.setVideoConfigCommon(common);
        List<Object> videoList = new ArrayList<>();
        if (HAIKANG.equals(common.getDeviceType())) {
            List<VideoConfigHaikangisc> haikangiscList = videoConfigHaikangiscMapper.selectList(new MyLambdaQueryWrapper<VideoConfigHaikangisc>()
                    .eq(VideoConfigHaikangisc::getVideoConfigCommonId, id));
            videoList.addAll(haikangiscList);
            returnParam.setVideoList(videoList);
        } else if (UNIVIEW.equals(common.getDeviceType())) {
            List<VideoConfigUniview> univiewList = videoConfigUniviewMapper.selectList(new MyLambdaQueryWrapper<VideoConfigUniview>()
                    .eq(VideoConfigUniview::getVideoConfigCommonId, id));
            videoList.addAll(univiewList);
            returnParam.setVideoList(videoList);
        } else if (DAHUA.equals(common.getDeviceType())) {
            List<VideoConfigDahua7016> dahua7016List = videoConfigDahua7016Mapper.selectList(new MyLambdaQueryWrapper<VideoConfigDahua7016>()
                    .eq(VideoConfigDahua7016::getVideoConfigCommonId, id));
            videoList.addAll(dahua7016List);
            returnParam.setVideoList(videoList);
        } else if (ESURFING.equals(common.getDeviceType())) {
            List<VideoConfigEsurfing> videoConfigEsurfings = videoConfigEsurfingMapper.selectList(new MyLambdaQueryWrapper<VideoConfigEsurfing>()
                    .eq(VideoConfigEsurfing::getVideoConfigCommonId, id));
            videoList.addAll(videoConfigEsurfings);
            returnParam.setVideoList(videoList);
        } else if (GUOBIAO.equals(common.getDeviceType())) {
            List<VideoConfigGuobiao> guobiaoList = videoConfigGuobiaoMapper.selectList(new MyLambdaQueryWrapper<VideoConfigGuobiao>()
                    .eq(VideoConfigGuobiao::getVideoConfigCommonId, id));
            videoList.addAll(guobiaoList);
            returnParam.setVideoList(videoList);
        } else if (IOA.equals(common.getDeviceType())) {
            List<VideoConfigIoa> ioaList = videoConfigIoaMapper.selectList(new MyLambdaQueryWrapper<VideoConfigIoa>()
                    .eq(VideoConfigIoa::getVideoConfigCommonId, id));
            videoList.addAll(ioaList);
            returnParam.setVideoList(videoList);
        } else if (ZIGUANG.equals(common.getDeviceType())) {
            List<VideoConfigZiguang> ziguangList = videoConfigZiguangMapper.selectList(new MyLambdaQueryWrapper<VideoConfigZiguang>()
                    .eq(VideoConfigZiguang::getVideoConfigCommonId, id));
            videoList.addAll(ziguangList);
            returnParam.setVideoList(videoList);
        } else {
            List<VideoConfigOther> otherList = videoConfigOtherMapper.selectList(new MyLambdaQueryWrapper<VideoConfigOther>()
                    .eq(VideoConfigOther::getVideoConfigCommonId, id));
            videoList.addAll(otherList);
            returnParam.setVideoList(videoList);
        }
        return returnParam;
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
}
