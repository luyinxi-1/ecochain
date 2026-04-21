package upc.c505.modular.ecochain.service;

import upc.c505.modular.villageiot.controller.param.VideoInspectionPointLocationInfoParam;

import java.util.List;

/**
 * @Description: 视频巡查
 * @Author: mjh
 * @CreateTime: 2024-10-18
 */
public interface IEcoChainVideoInspectionService {

    List<VideoInspectionPointLocationInfoParam> bySocialCreditCodeAndIsPublic(String socialCreditCode, Integer isPublic);

    List<VideoInspectionPointLocationInfoParam> byRegionIdAndRelatedProject(Long regionId, String relatedProject, Integer isPublic);
}
