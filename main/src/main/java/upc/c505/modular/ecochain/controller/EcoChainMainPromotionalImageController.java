package upc.c505.modular.ecochain.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import upc.c505.common.responseparam.R;
import upc.c505.modular.ecochain.controller.param.EcoChainMainPromotionalImageSearchParam;
import upc.c505.modular.ecochain.entity.EcoChainMainPromotionalImage;
import upc.c505.modular.ecochain.service.IEcoChainMainPromotionalImageService;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author byh
 * @since 2024-09-27
 */
@RestController
@RequestMapping("/eco-chain-main-promotional-image")
@Api(tags = "宣传主图")
public class EcoChainMainPromotionalImageController {
    @Autowired
    private IEcoChainMainPromotionalImageService ecoChainMainPromotionalImageService;

    @PostMapping("/insertMainPromotionalImage")
    @ApiOperation("新增宣传主图")
    public R<Void> insertMainPromotionalImage(@RequestBody EcoChainMainPromotionalImage param){
        ecoChainMainPromotionalImageService.insertMainPromotionalImage(param);
        return R.ok();
    }

    @PostMapping("/removeMainPromotionalImage")
    @ApiOperation("删除宣传主图")
    public R<Boolean> removeMainPromotionalImage(@RequestParam List<Long> idList){
        boolean result = ecoChainMainPromotionalImageService.removeMainPromotionalImage(idList);
//        boolean result = ecoChainMainPromotionalImageService.removeBatchByIds(idList);
        return R.ok(result);
    }

    @PostMapping("/updateMainPromotionalImage")
    @ApiOperation("更新宣传主图")
    public R<Boolean> updateMainPromotionalImage(@RequestBody EcoChainMainPromotionalImage param){
        boolean result = ecoChainMainPromotionalImageService.updateMainPromotionalImage(param);
        return R.ok(result);
    }

    @PostMapping("/selectMainPromotionalImage")
    @ApiOperation("查询宣传主图")
    public R<List<EcoChainMainPromotionalImage>> selectMainPromotionalImage(@RequestBody EcoChainMainPromotionalImageSearchParam param){
        List<EcoChainMainPromotionalImage> list = ecoChainMainPromotionalImageService.selectMainPromotionalImage(param);
        return R.ok(list);
    }

    @PostMapping("/selectEnableMainPromotionalImage")
    @ApiOperation("查询启用宣传主图")
    public R<List<EcoChainMainPromotionalImage>> selectEnableMainPromotionalImage(@RequestBody EcoChainMainPromotionalImageSearchParam param){
        List<EcoChainMainPromotionalImage> list = ecoChainMainPromotionalImageService.selectEnableMainPromotionalImage(param);
        return R.ok(list);
    }

    @PostMapping("/updateMainPromotionalImageStatus")
    @ApiOperation("更新宣传主图启用状态")
    public R<Boolean> updateMainPromotionalImageStatus(@RequestParam Long id, @RequestParam Integer status){
        boolean result = ecoChainMainPromotionalImageService.updateMainPromotionalImageStatus(id, status);
        return R.ok(result);
    }
}
