package upc.c505.modular.ecochain.controller;


import cn.hutool.core.lang.Dict;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import upc.c505.common.responseparam.R;
import upc.c505.modular.ecochain.controller.param.EcoChainProductClassificationSearchParam;
import upc.c505.modular.ecochain.controller.param.EcoChainProductTagSearchParam;
import upc.c505.modular.ecochain.controller.param.EcoChainTopLevelProductClassificationSearchParam;
import upc.c505.modular.ecochain.entity.EcoChainProductClassification;
import upc.c505.modular.ecochain.entity.EcoChainProductTag;
import upc.c505.modular.ecochain.service.IEcoChainProductClassificationService;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author byh
 * @since 2024-09-25
 */
@RestController
@RequestMapping("/eco-chain-product-classification")
@Api(tags = "产品分类")
public class EcoChainProductClassificationController {
    @Autowired
    private IEcoChainProductClassificationService ecoChainProductClassificationService;

    @PostMapping("/insertProductClassification")
    @ApiOperation("新增产品分类")
    public R<Void> insertProductClassification(@RequestBody EcoChainProductClassification param){
        ecoChainProductClassificationService.insertProductClassification(param);
        return R.ok();
    }

    @PostMapping("/removeProductClassification")
    @ApiOperation("批量删除产品分类")
    public R<Void> removeProductClassification(@RequestParam List<Long> idList){
        ecoChainProductClassificationService.removeProductClassification(idList);
        return R.ok();
    }


    @PostMapping("/updateProductClassification")
    @ApiOperation("更新产品分类")
    public R<Boolean> updateProductClassification(@RequestBody EcoChainProductClassification param){
        boolean result = ecoChainProductClassificationService.updateProductClassification(param);
        return R.ok(result);
    }

    @PostMapping("/selectProductClassificationParentIdList")
    @ApiOperation("获取产品分类的所有上级分类")
    public R<List<EcoChainProductClassification>> selectProductClassificationParentIdList(@RequestParam Integer classificationGrade){
        List<EcoChainProductClassification> list = ecoChainProductClassificationService.selectProductClassificationParentIdList(classificationGrade);
        return R.ok(list);
    }

    @PostMapping("/selectProductClassificationDownList")
    @ApiOperation("获取产品分类的下级分类")
    public R<List<EcoChainProductClassification>> selectProductClassificationDownList(@RequestParam Long id){
        List<EcoChainProductClassification> list = ecoChainProductClassificationService.selectProductClassificationDownList(id);
        return R.ok(list);
    }

    @PostMapping("/selectProductClassificationList")
    @ApiOperation("获取分类列表")
    public R<List<EcoChainProductClassification>> selectProductClassificationList(@RequestBody EcoChainProductClassificationSearchParam param){
        List<EcoChainProductClassification> list = ecoChainProductClassificationService.selectProductClassificationList(param);
        List<EcoChainProductClassification> ecoChainProductClassification = ecoChainProductClassificationService.buildDictTree(list);
        return R.ok(ecoChainProductClassification);
    }

    @PostMapping("/updateProductClassificationSortName")
    @ApiOperation("更改产品分类排序(0向上，1向下)")
    public R<Boolean> updateProductClassificationSortName(@RequestParam Long id, @RequestParam Integer param){
        boolean result = ecoChainProductClassificationService.updateProductClassificationSortName(id, param);
        return R.ok(result);
    }

    @PostMapping("/selectTopLevelProductClassification")
    @ApiOperation("查询顶级产品分类")
    public R<List<EcoChainProductClassification>> selectTopLevelProductClassification(@RequestBody EcoChainTopLevelProductClassificationSearchParam param){
        List<EcoChainProductClassification> list = ecoChainProductClassificationService.selectTopLevelProductClassification(param);
        return R.ok(list);
    }

}
