package upc.c505.modular.ecochain.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import upc.c505.common.wrapper.MyLambdaQueryWrapper;
import upc.c505.modular.ecochain.entity.EcoChainVedioMonitorConfig;
import upc.c505.modular.ecochain.mapper.EcoChainVedioMonitorConfigMapper;
import upc.c505.modular.ecochain.service.IEcoChainVideoInspectionService;
import upc.c505.modular.villageiot.controller.param.VideoConst;
import upc.c505.modular.villageiot.controller.param.VideoInspectionPointLocationInfoParam;
import upc.c505.modular.villageiot.entity.VideoConfigCommon;
import upc.c505.modular.villageiot.entity.VideoConfigHaikangisc;
import upc.c505.modular.villageiot.mapper.*;
import upc.c505.modular.villageiot.service.IVideoConfigCommonService;
import upc.c505.modular.villageiot.service.impl.VideoRandomInspectionServiceImpl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @Description: 视频巡查
 * @Author: mjh
 * @CreateTime: 2024-10-18
 */
@Service
public class EcoChainVideoInspectionServiceImpl implements IEcoChainVideoInspectionService {

    @Autowired
    private IVideoConfigCommonService videoConfigCommonService;

    @Autowired
    private EcoChainVedioMonitorConfigMapper ecoChainVedioMonitorConfigMapper;

    @Autowired
    private VideoRandomInspectionServiceImpl videoRandomInspectionService;

    @Autowired
    private VideoConfigHaikangiscMapper haikangMapper;
    @Autowired
    private VideoConfigUniviewMapper univiewMapper;
    @Autowired
    private VideoConfigDahua7016Mapper dahuaMapper;
    @Autowired
    private VideoConfigZiguangMapper ziguangMapper;
    @Autowired
    private VideoConfigIoaMapper ioaMapper;
    @Autowired
    private VideoConfigEsurfingMapper esurfingMapper;
    @Autowired
    private VideoConfigGuobiaoMapper guobiaoMapper;
    @Autowired
    private VideoConfigOtherMapper otherMapper;


    @Override
    public List<VideoInspectionPointLocationInfoParam> bySocialCreditCodeAndIsPublic(String socialCreditCode, Integer isPublic) {
        //存放返回列表
        List<VideoInspectionPointLocationInfoParam> list = new ArrayList<>();

        if (!ObjectUtils.isNotEmpty(socialCreditCode)) return list;

        if (!ObjectUtils.isNotEmpty(socialCreditCode)) return list;
        // 一个社会信用代码对应一条数据
        VideoConfigCommon common = videoConfigCommonService.getOne(
                new LambdaQueryWrapper<VideoConfigCommon>()
                        .eq(VideoConfigCommon::getEntCreditCode, socialCreditCode));
        if (!ObjectUtils.isNotEmpty(common)) return list;

        // 获取配置id，设备类型，区域id
        Long id = Long.valueOf(common.getId());
        String deviceType = common.getDeviceType();
        String videoSign = common.getVideoSign();
        Long areaId = common.getAreaId();

        // 根据设备类型，调用不同的方法构造 返回信息 并加入列表list
        switch (deviceType) {
            case VideoConst.HAIKANG:
                //如果是海康的设备
                videoRandomInspectionService.haikang(id, areaId, list, videoSign);
                break;
            case VideoConst.UNIVIEW:
                //如果是宇视的设备
                videoRandomInspectionService.uniview(id, areaId, list, videoSign);
                break;
            case VideoConst.DAHUA:
                //如果是大华的设备
                videoRandomInspectionService.dahua(id, areaId, list, videoSign);
                break;
            case VideoConst.ZIGUANG:
                //如果是紫光的设备
                videoRandomInspectionService.ziguang(id, areaId, list, videoSign);
                break;
            case VideoConst.IOA:
                //如果是内蒙的设备
                videoRandomInspectionService.ioa(id, areaId, list, videoSign);
                break;
            case VideoConst.ESURFING:
                //如果是天翼的设备
                videoRandomInspectionService.esurfing(id, list, videoSign);
                break;
            case VideoConst.GUOBIAO:
                //如果是国标的设备
                videoRandomInspectionService.guobiao(id, list, videoSign);
                break;
            case VideoConst.OTHER:
                //如果是其他类型的设备
                videoRandomInspectionService.other(id, list, videoSign);
                break;
            default:
                break;
        }

        // 遍历list，删除不符合条件的数据
        Iterator<VideoInspectionPointLocationInfoParam> iterator = list.iterator();
        while (iterator.hasNext()) {
            VideoInspectionPointLocationInfoParam param = iterator.next();
            EcoChainVedioMonitorConfig ecoChainVedioMonitorConfig = ecoChainVedioMonitorConfigMapper.selectOne(
                    new MyLambdaQueryWrapper<EcoChainVedioMonitorConfig>()
                            .eq(EcoChainVedioMonitorConfig::getMonitorId, param.getDeviceId())
                            .eq(EcoChainVedioMonitorConfig::getDeviceType, param.getDeviceType())
            );
            if (ObjectUtils.isNotEmpty(ecoChainVedioMonitorConfig) && ObjectUtils.isNotEmpty(isPublic) && ecoChainVedioMonitorConfig.getIsPublic() != isPublic) {
                // 如果数据库中存在该配置，并且配置的 public 和 传入的 public 不一致，则删除list中此条数据
                iterator.remove();
            }
        }

        return list;
    }

    @Override
    public List<VideoInspectionPointLocationInfoParam> byRegionIdAndRelatedProject(Long regionId, String relatedProject, Integer isPublic) {
        //存放返回列表
        List<VideoInspectionPointLocationInfoParam> list = new ArrayList<>();
        // 参数都为空，则返回空列表
        if(!ObjectUtils.isNotEmpty(regionId) && !ObjectUtils.isNotEmpty(relatedProject) && !ObjectUtils.isNotEmpty(isPublic)) return list;

        // 根据区域id和项目名查询配置
        List<EcoChainVedioMonitorConfig> configList = ecoChainVedioMonitorConfigMapper.selectList(
                new MyLambdaQueryWrapper<EcoChainVedioMonitorConfig>()
                        .eq(EcoChainVedioMonitorConfig::getRegionId, regionId)
                        .eq(EcoChainVedioMonitorConfig::getRelatedProject, relatedProject));
        if (!ObjectUtils.isNotEmpty(configList)) return list;

        // 遍历配置列表，根据配置信息查询视频信息
        for (EcoChainVedioMonitorConfig item: configList) {
            VideoConfigCommon common = videoConfigCommonService.getOne(
                    new MyLambdaQueryWrapper<VideoConfigCommon>()
                            .eq(VideoConfigCommon::getId, item.getVideoConfigCommonId())
                            .eq(VideoConfigCommon::getDeviceType, item.getDeviceType()));
            if (!ObjectUtils.isNotEmpty(common)) continue;

            // 借助之前的videoInspectionBySocialCreditCodeAndIsPublic方法，获取信息
            List<VideoInspectionPointLocationInfoParam> tempList = bySocialCreditCodeAndIsPublic(common.getEntCreditCode(), isPublic);
            Optional<VideoInspectionPointLocationInfoParam> infoParam = tempList.stream()
                    .filter(
                            param -> param.getDeviceId().equals(item.getMonitorId())
                                    && param.getDeviceType().equals(item.getDeviceType()))
                    .findFirst();
            // 如果infoParam不为空，则添加到list中
            if(infoParam.isPresent()) {
                list.add(infoParam.get());
            }
        }

        return list;
    }
}
