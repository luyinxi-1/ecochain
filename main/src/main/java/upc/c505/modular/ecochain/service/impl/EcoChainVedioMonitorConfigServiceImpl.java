package upc.c505.modular.ecochain.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import upc.c505.common.wrapper.MyLambdaQueryWrapper;
import upc.c505.modular.ecochain.controller.param.EcoChainVedioMonitorConfigPageSearchParam;
import upc.c505.modular.ecochain.entity.EcoChainVedioMonitorConfig;
import upc.c505.modular.ecochain.mapper.EcoChainVedioMonitorConfigMapper;
import upc.c505.modular.ecochain.service.IEcoChainVedioMonitorConfigService;
import upc.c505.modular.villageiot.entity.*;
import upc.c505.modular.villageiot.mapper.*;
import upc.c505.utils.GetPageInfoUtil;

import java.util.ArrayList;
import java.util.List;

import static upc.c505.modular.villageiot.controller.param.VideoConst.*;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author mjh
 * @since 2024-09-23
 */
@Service
public class EcoChainVedioMonitorConfigServiceImpl extends ServiceImpl<EcoChainVedioMonitorConfigMapper, EcoChainVedioMonitorConfig> implements IEcoChainVedioMonitorConfigService {

    @Autowired
    private EcoChainVedioMonitorConfigMapper ecoChainVedioMonitorConfigMapper;

    @Autowired
    private VideoConfigCommonMapper videoConfigCommonMapper;


    //----------------------- 设备类型对应的配置表Mapper begin -----------------------
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
//----------------------- 设备类型对应的配置表Mapper end -----------------------

    @Override
    public String insertOrUpdateConfig(EcoChainVedioMonitorConfig param) {
        if (ObjectUtils.isNotEmpty(param.getId())) {
            EcoChainVedioMonitorConfig monitorConfig = this.getById(param.getId());
            if(param.getPointName() != monitorConfig.getPointName()) {
                String deviceType = monitorConfig.getDeviceType();
                if (HAIKANG.equals(deviceType)) {
                    videoConfigHaikangiscMapper.updateById(
                            videoConfigHaikangiscMapper.selectById(monitorConfig.getMonitorId())
                                    .setPointName(param.getPointName()));
                }
                else if (DAHUA.equals(deviceType)) {
                    videoConfigDahua7016Mapper.updateById(
                            videoConfigDahua7016Mapper.selectById(monitorConfig.getMonitorId())
                                    .setPointName(param.getPointName()));
                }
                else if (ESURFING.equals(deviceType)) {
                    videoConfigEsurfingMapper.updateById(
                            videoConfigEsurfingMapper.selectById(monitorConfig.getMonitorId())
                                    .setDeviceName(param.getPointName()));
                }
                else if (GUOBIAO.equals(deviceType)) {
                    videoConfigGuobiaoMapper.updateById(
                            videoConfigGuobiaoMapper.selectById(monitorConfig.getMonitorId())
                                    .setName(param.getPointName()));
                }
                else if (IOA.equals(deviceType)) {
                    videoConfigIoaMapper.updateById(
                            videoConfigIoaMapper.selectById(monitorConfig.getMonitorId())
                                    .setPointName(param.getPointName()));
                }
                else if (UNIVIEW.equals(deviceType)) {
                    videoConfigUniviewMapper.updateById(
                            videoConfigUniviewMapper.selectById(monitorConfig.getMonitorId())
                                    .setPointName(param.getPointName()));
                }
                else if (ZIGUANG.equals(deviceType)) {
                    videoConfigZiguangMapper.updateById(
                            videoConfigZiguangMapper.selectById(monitorConfig.getMonitorId())
                                   .setPointName(param.getPointName()));
                }
                else if (OTHER.equals(deviceType)) {
                    videoConfigOtherMapper.updateById(
                            videoConfigOtherMapper.selectById(monitorConfig.getMonitorId())
                                    .setPointName(param.getPointName()));
                }
            }
            this.updateById(param);
            return "更新配置成功";
        }
        MyLambdaQueryWrapper<EcoChainVedioMonitorConfig> queryWrapper = new MyLambdaQueryWrapper<>();
        queryWrapper.select(EcoChainVedioMonitorConfig::getId, EcoChainVedioMonitorConfig::getMonitorId, EcoChainVedioMonitorConfig::getDeviceType);
        queryWrapper.eq(EcoChainVedioMonitorConfig::getMonitorId, param.getMonitorId())
                .eq(EcoChainVedioMonitorConfig::getDeviceType, param.getDeviceType());
        List<EcoChainVedioMonitorConfig> list = this.list(queryWrapper);
        if (list.isEmpty()) {
            if (this.save(param)) {
                return "新增配置成功";
            }
        } else if (list.size() == 1) {
            param.setId(list.get(0).getId());
            EcoChainVedioMonitorConfig monitorConfig = list.get(0);
            if(param.getPointName() != monitorConfig.getPointName()) {
                String deviceType = monitorConfig.getDeviceType();
                if (HAIKANG.equals(deviceType)) {
                    videoConfigHaikangiscMapper.updateById(
                            videoConfigHaikangiscMapper.selectById(monitorConfig.getMonitorId())
                                    .setPointName(param.getPointName()));
                }
                else if (DAHUA.equals(deviceType)) {
                    videoConfigDahua7016Mapper.updateById(
                            videoConfigDahua7016Mapper.selectById(monitorConfig.getMonitorId())
                                    .setPointName(param.getPointName()));
                }
                else if (ESURFING.equals(deviceType)) {
                    videoConfigEsurfingMapper.updateById(
                            videoConfigEsurfingMapper.selectById(monitorConfig.getMonitorId())
                                    .setDeviceName(param.getPointName()));
                }
                else if (GUOBIAO.equals(deviceType)) {
                    videoConfigGuobiaoMapper.updateById(
                            videoConfigGuobiaoMapper.selectById(monitorConfig.getMonitorId())
                                    .setName(param.getPointName()));
                }
                else if (IOA.equals(deviceType)) {
                    videoConfigIoaMapper.updateById(
                            videoConfigIoaMapper.selectById(monitorConfig.getMonitorId())
                                    .setPointName(param.getPointName()));
                }
                else if (UNIVIEW.equals(deviceType)) {
                    videoConfigUniviewMapper.updateById(
                            videoConfigUniviewMapper.selectById(monitorConfig.getMonitorId())
                                    .setPointName(param.getPointName()));
                }
                else if (ZIGUANG.equals(deviceType)) {
                    videoConfigZiguangMapper.updateById(
                            videoConfigZiguangMapper.selectById(monitorConfig.getMonitorId())
                                    .setPointName(param.getPointName()));
                }
                else if (OTHER.equals(deviceType)) {
                    videoConfigOtherMapper.updateById(
                            videoConfigOtherMapper.selectById(monitorConfig.getMonitorId())
                                    .setPointName(param.getPointName()));
                }
            }
            this.updateById(param);
            return "更新配置成功";
        } else return "存在冲突配置，请联系管理";
        return "";
    }

    @Override
    public IPage<EcoChainVedioMonitorConfig> selectPage(EcoChainVedioMonitorConfigPageSearchParam param) {
// -------------------- 更新数据库 视频配置 begin --------------------
//      查看VideoConfigCommon所有配置
        List<VideoConfigCommon> videoConfigCommonList = videoConfigCommonMapper.selectList(
                new LambdaQueryWrapper<VideoConfigCommon>()
                        .select(VideoConfigCommon::getId, VideoConfigCommon::getDeviceType));
//      查看生态链监控配置表里所有的配置
        List<EcoChainVedioMonitorConfig> configList = ecoChainVedioMonitorConfigMapper.selectList(
                new LambdaQueryWrapper<EcoChainVedioMonitorConfig>()
                        .select(EcoChainVedioMonitorConfig::getId, EcoChainVedioMonitorConfig::getMonitorId, EcoChainVedioMonitorConfig::getDeviceType));
//      定义一个用于存储新增配置的集合
        List<EcoChainVedioMonitorConfig> configAddList = new ArrayList<>();
        for (VideoConfigCommon common : videoConfigCommonList) {

            if (ObjectUtils.isNotEmpty(common.getDeviceType())) {
                String deviceType = common.getDeviceType();
                Integer videoConfigCommonId = common.getId();

                if (HAIKANG.equals(deviceType)) {
                    List<VideoConfigHaikangisc> tempList = videoConfigHaikangiscMapper.selectList(new MyLambdaQueryWrapper<VideoConfigHaikangisc>()
                            .eq(VideoConfigHaikangisc::getVideoConfigCommonId, videoConfigCommonId));
                    for (VideoConfigHaikangisc haikangisc : tempList) {
                        //判断是否已经存在
                        boolean flag = configList.stream().anyMatch(item -> item.getMonitorId().equals(haikangisc.getId()) && item.getDeviceType().equals(HAIKANG));
                        if (!flag) {
                            //不存在，则新增
                            EcoChainVedioMonitorConfig config = new EcoChainVedioMonitorConfig();
                            config.setVideoConfigCommonId(haikangisc.getVideoConfigCommonId());
                            config.setMonitorId(haikangisc.getId());
                            config.setDeviceType(HAIKANG);
                            config.setPointName(haikangisc.getPointName());
                            config.setIsPublic(1);
                            configAddList.add(config);
                        }
                    }
                }
                else if (DAHUA.equals(deviceType)) {
                    List<VideoConfigDahua7016> tempList = videoConfigDahua7016Mapper.selectList(new MyLambdaQueryWrapper<VideoConfigDahua7016>()
                            .eq(VideoConfigDahua7016::getVideoConfigCommonId, videoConfigCommonId));
                    for (VideoConfigDahua7016 dahua7016 : tempList) {
                        //判断是否已经存在
                        boolean flag = configList.stream().anyMatch(item -> item.getMonitorId().equals(dahua7016.getId()) && item.getDeviceType().equals(DAHUA));
                        if (!flag) {
                            //不存在，则新增
                            EcoChainVedioMonitorConfig config = new EcoChainVedioMonitorConfig();
                            config.setVideoConfigCommonId(dahua7016.getVideoConfigCommonId());
                            config.setMonitorId(dahua7016.getId());
                            config.setDeviceType(DAHUA);
                            config.setPointName(dahua7016.getPointName());
                            config.setIsPublic(1);
                            configAddList.add(config);
                        }
                    }
                }
                else if (ESURFING.equals(deviceType)) {
                    List<VideoConfigEsurfing> tempList = videoConfigEsurfingMapper.selectList(new MyLambdaQueryWrapper<VideoConfigEsurfing>()
                            .eq(VideoConfigEsurfing::getVideoConfigCommonId, videoConfigCommonId));
                    for (VideoConfigEsurfing esurfing : tempList) {
                        //判断是否已经存在
                        boolean flag = configList.stream().anyMatch(item -> item.getMonitorId().equals(esurfing.getId()) && item.getDeviceType().equals(ESURFING));
                        if (!flag) {
                            //不存在，则新增
                            EcoChainVedioMonitorConfig config = new EcoChainVedioMonitorConfig();
                            config.setVideoConfigCommonId(esurfing.getVideoConfigCommonId());
                            config.setMonitorId(esurfing.getId().intValue());
                            config.setDeviceType(ESURFING);
                            config.setPointName(esurfing.getDeviceName());
                            config.setIsPublic(1);
                            configAddList.add(config);
                        }
                    }
                }
                else if (GUOBIAO.equals(deviceType)) {
                    List<VideoConfigGuobiao> tempList = videoConfigGuobiaoMapper.selectList(new MyLambdaQueryWrapper<VideoConfigGuobiao>()
                            .eq(VideoConfigGuobiao::getVideoConfigCommonId, videoConfigCommonId));
                    for (VideoConfigGuobiao guobiao : tempList) {
                        //判断是否已经存在
                        boolean flag = configList.stream().anyMatch(item -> item.getMonitorId().equals(guobiao.getId()) && item.getDeviceType().equals(GUOBIAO));
                        if (!flag) {
                            //不存在，则新增
                            EcoChainVedioMonitorConfig config = new EcoChainVedioMonitorConfig();
                            config.setVideoConfigCommonId(guobiao.getVideoConfigCommonId());
                            config.setMonitorId(guobiao.getId());
                            config.setDeviceType(GUOBIAO);
                            config.setPointName(guobiao.getName());
                            config.setIsPublic(1);
                            configAddList.add(config);
                        }
                    }

                }
                else if (IOA.equals(deviceType)) {
                    List<VideoConfigIoa> tempList = videoConfigIoaMapper.selectList(new MyLambdaQueryWrapper<VideoConfigIoa>()
                            .eq(VideoConfigIoa::getVideoConfigCommonId, videoConfigCommonId));
                    for (VideoConfigIoa ioa : tempList) {
                        //判断是否已经存在
                        boolean flag = configList.stream().anyMatch(item -> item.getMonitorId().equals(ioa.getId()) && item.getDeviceType().equals(IOA));
                        if (!flag) {
                            EcoChainVedioMonitorConfig config = new EcoChainVedioMonitorConfig();
                            config.setVideoConfigCommonId(ioa.getVideoConfigCommonId());
                            config.setMonitorId(ioa.getId());
                            config.setDeviceType(IOA);
                            config.setPointName(ioa.getPointName());
                            config.setIsPublic(1);
                            configAddList.add(config);
                        }
                    }
                }
                else if (UNIVIEW.equals(deviceType)) {
                    List<VideoConfigUniview> tempList = videoConfigUniviewMapper.selectList(new MyLambdaQueryWrapper<VideoConfigUniview>()
                            .eq(VideoConfigUniview::getVideoConfigCommonId, videoConfigCommonId));
                    for (VideoConfigUniview uniview : tempList) {
                        //判断是否已经存在
                        boolean flag = configList.stream().anyMatch(item -> item.getMonitorId().equals(uniview.getId()) && item.getDeviceType().equals(UNIVIEW));
                        if (!flag) {
                            //不存在，则新增
                            EcoChainVedioMonitorConfig config = new EcoChainVedioMonitorConfig();
                            config.setVideoConfigCommonId(uniview.getVideoConfigCommonId());
                            config.setMonitorId(uniview.getId());
                            config.setDeviceType(UNIVIEW);
                            config.setPointName(uniview.getPointName());
                            config.setIsPublic(1);
                            configAddList.add(config);
                        }
                    }
                }
                else if (ZIGUANG.equals(deviceType)) {
                    List<VideoConfigZiguang> tempList = videoConfigZiguangMapper.selectList(new MyLambdaQueryWrapper<VideoConfigZiguang>()
                            .eq(VideoConfigZiguang::getVideoConfigCommonId, videoConfigCommonId));
                    for (VideoConfigZiguang ziguang : tempList) {
                        //判断是否已经存在
                        boolean flag = configList.stream().anyMatch(item -> item.getMonitorId().equals(ziguang.getId()) && item.getDeviceType().equals(ZIGUANG));
                        if (!flag) {
                            //不存在，则新增
                            EcoChainVedioMonitorConfig config = new EcoChainVedioMonitorConfig();
                            config.setVideoConfigCommonId(ziguang.getVideoConfigCommonId());
                            config.setMonitorId(ziguang.getId());
                            config.setDeviceType(ZIGUANG);
                            config.setPointName(ziguang.getPointName());
                            config.setIsPublic(1);
                            configAddList.add(config);
                        }
                    }
                }
                else if (OTHER.equals(deviceType)){
                    List<VideoConfigOther> tempList = videoConfigOtherMapper.selectList(new MyLambdaQueryWrapper<VideoConfigOther>()
                            .eq(VideoConfigOther::getVideoConfigCommonId, videoConfigCommonId));
                    for (VideoConfigOther other : tempList) {
                        //判断是否已经存在
                        boolean flag = configList.stream().anyMatch(item -> item.getMonitorId().equals(other.getId()));
                        if (!flag) {
                            //不存在，则新增
                            EcoChainVedioMonitorConfig config = new EcoChainVedioMonitorConfig();
                            config.setVideoConfigCommonId(other.getVideoConfigCommonId());
                            config.setMonitorId(other.getId());
                            config.setDeviceType(deviceType);
                            config.setPointName(other.getPointName());
                            config.setIsPublic(1);
                            configAddList.add(config);
                        }
                    }
                }
            }
        }
        this.saveBatch(configAddList);
// -------------------- 更新数据库 视频配置 end --------------------

// -------------------- 查询 begin --------------------
        List<EcoChainVedioMonitorConfig> returnList = new ArrayList<>();

//        QueryWrapper<EcoChainVedioMonitorConfig> queryWrapper = QueryWrapperUtil.buildQueryWrapper(
//                EcoChainVedioMonitorConfigPageSearchParam.class, param);

        List<EcoChainVedioMonitorConfig> selectConfigList = this.list(new MyLambdaQueryWrapper<EcoChainVedioMonitorConfig>()
                .eq(EcoChainVedioMonitorConfig::getIsPublic, param.getIsPublic())
                .like(EcoChainVedioMonitorConfig::getPointName, param.getPointName()));

        if (ObjectUtils.isNotEmpty(param.getSocialCreditCode()) && ObjectUtils.isNotEmpty(selectConfigList)) {
            for (EcoChainVedioMonitorConfig config : selectConfigList) {
                VideoConfigCommon tempCommon = videoConfigCommonMapper.selectById(config.getVideoConfigCommonId());
                if (ObjectUtils.isNotEmpty(tempCommon) && ObjectUtils.isNotEmpty(tempCommon.getEntCreditCode()) && tempCommon.getEntCreditCode().equals(param.getSocialCreditCode())) {
                    returnList.add(config);
                }
            }
        }
// -------------------- 查询 end --------------------
        // 使用工具类构造分页
        IPage<EcoChainVedioMonitorConfig> returnPage = GetPageInfoUtil.getPageInfo(
                param.getCurrent().intValue(), param.getSize().intValue(), returnList);

        return returnPage;
    }
}
