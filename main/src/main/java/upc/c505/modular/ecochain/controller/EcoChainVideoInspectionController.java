package upc.c505.modular.ecochain.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import upc.c505.common.responseparam.R;
import upc.c505.modular.ecochain.service.IEcoChainVideoInspectionService;
import upc.c505.modular.villageiot.controller.param.VideoInspectionPointLocationInfoParam;

import java.util.List;

/**
 * @Description: 视频巡查
 * @Author: mjh
 * @CreateTime: 2024-10-18
 */
@RestController
@RequestMapping("/eco-chain-vedio-inspection")
@Api(tags = "视频巡查")
public class EcoChainVideoInspectionController {

    @Autowired
    private IEcoChainVideoInspectionService ecoChainVideoInspectionService;

    @ApiOperation("根据社会信用代码和是否公开")
    @GetMapping("/bySocialCreditCodeAndIsPublic")
    public R<List<VideoInspectionPointLocationInfoParam>> bySocialCreditCodeAndIsPublic(String socialCreditCode, Integer isPublic) {
        return R.ok(ecoChainVideoInspectionService.bySocialCreditCodeAndIsPublic(socialCreditCode, isPublic));
    }
    @ApiOperation("根据区域组、项目id和是否公开")
    @GetMapping("/byRegionIdAndRelatedProjectAndIsPublic")
    public R<List<VideoInspectionPointLocationInfoParam>> byRegionIdAndRelatedProject(Long regionId, String relatedProject, Integer isPublic) {
        return R.ok(ecoChainVideoInspectionService.byRegionIdAndRelatedProject(regionId, relatedProject, isPublic));
    }
}
