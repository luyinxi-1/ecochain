package upc.c505.modular.ecochain.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import upc.c505.modular.ecochain.controller.param.EcoChainProductParameterValueContentReturnParam;
import upc.c505.modular.ecochain.entity.EcoChainProductParameterValueContent;
import upc.c505.modular.ecochain.service.IEcoChainProductParameterService;
import upc.c505.common.responseparam.R;
import upc.c505.modular.ecochain.service.IEcoChainProductParameterValueContentService;

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
@Api(tags = "产品参数值关联内容")
@RequestMapping("/eco-chain-product-parameter-value-content")
public class EcoChainProductParameterValueContentController {
    @Autowired
    private IEcoChainProductParameterValueContentService ecoChainProductParameterValueContentService;

    @PostMapping("/insertProductParameterValueContent")
    @ApiOperation("新增产品参数值内容")
    public R<Void> insertProductParameterValueContent(@RequestBody EcoChainProductParameterValueContent param){
        ecoChainProductParameterValueContentService.insertProductParameterValueContent(param);
        return R.ok();
    }

    @PostMapping("/removeProductParameterValueContent")
    @ApiOperation("删除产品参数值内容")
    public R<Void> removeProductParameterValueContent(@RequestParam List<Long> idList){
        ecoChainProductParameterValueContentService.removeProductParameterValueContent(idList);
        return R.ok();
    }

    @PostMapping("/updateProductParameterValueContent")
    @ApiOperation("更新产品参数值内容")
    public R<Boolean> updateProductParameterValueContent(@RequestBody EcoChainProductParameterValueContent param){
        boolean result = ecoChainProductParameterValueContentService.updateProductParameterValueContent(param);
        return R.ok(result);
    }

    @PostMapping("/selectProductParameterValueContentList")
    @ApiOperation("查看产品参数值内容")
    public R<EcoChainProductParameterValueContentReturnParam> selectProductParameterValueContentList(@RequestParam Long productParameterId){
        List<EcoChainProductParameterValueContent> resultList = ecoChainProductParameterValueContentService.selectProductParameterValueContentList(productParameterId);
        EcoChainProductParameterValueContentReturnParam result = new EcoChainProductParameterValueContentReturnParam();
        result.setValueList(resultList);
        result.setTotal(resultList.size());
        return R.ok(result);
    }

    @PostMapping("/updateProductParameterSortNumber")
    @ApiOperation("更新产品参数值内容排序(0向上，1向下)")
    public R<Boolean> updateProductParameterSortNumber(@RequestParam Long id, @RequestParam Integer param){
        boolean result = ecoChainProductParameterValueContentService.updateProductParameterSortNumber(id,param);
        return R.ok(result);
    }
}
