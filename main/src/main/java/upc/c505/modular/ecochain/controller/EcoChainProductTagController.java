package upc.c505.modular.ecochain.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import upc.c505.common.responseparam.R;
import upc.c505.modular.ecochain.controller.param.EcoChainProductTagSearchParam;
import upc.c505.modular.ecochain.entity.EcoChainProductTag;
import upc.c505.modular.ecochain.service.IEcoChainProductTagService;

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
@Api(tags = "产品标签")
@RequestMapping("/eco-chain-product-tag")
public class EcoChainProductTagController {
    @Autowired
    private IEcoChainProductTagService ecoChainProductTagService;
    @PostMapping("/insertProductTag")
    @ApiOperation("新增产品标签")
    public R<Void> insertProductTag(@RequestBody EcoChainProductTag param){
        ecoChainProductTagService.insertProductTag(param);
        return R.ok();
    }

    @PostMapping("/removeProductTag")
    @ApiOperation("批量删除产品标签")
    public R<Void> removeProductTag(@RequestParam List<Long> idList){
        ecoChainProductTagService.removeProductTag(idList);
        return R.ok();
    }

    @PostMapping("/updateProductTagStatus")
    @ApiOperation("更新产品标签启用状态")
    public R<Boolean> updateProductTagStatus(@RequestParam Long id, @RequestParam Integer status){
        boolean result = ecoChainProductTagService.updateProductTagStatus(id, status);
        return R.ok(result);
    }

    @PostMapping("/updateProductTag")
    @ApiOperation("更新产品标签")
    public R<Boolean> updateProductTag(@RequestBody EcoChainProductTag param){
        boolean result = ecoChainProductTagService.updateProductTag(param);
        return R.ok(result);
    }

    @PostMapping("/updateProductTagSortNumber")
    @ApiOperation("更改产品标签排序(0向上，1向下)")
    public R<Boolean> updateProductTagSortNumber(@RequestParam Long id, @RequestParam Integer param){
        // 0表示向上调整，1表示向下调整
        boolean result = ecoChainProductTagService.updateProductTagSortNumber(id, param);
        return R.ok(result);
    }

    @PostMapping("/selectProductTagList")
    @ApiOperation("查询产品标签列表")
    public R<List<EcoChainProductTag>> selectProductTagList(@RequestBody EcoChainProductTagSearchParam param){
        List<EcoChainProductTag> list = ecoChainProductTagService.selectProductTagList(param);
        return R.ok(list);
    }

    @PostMapping("/selectEnableProductTagList")
    @ApiOperation("获取启用的产品标签")
    public R<List<EcoChainProductTag>> selectEnableProductTagList(@RequestBody EcoChainProductTagSearchParam param){
        List<EcoChainProductTag> list = ecoChainProductTagService.selectEnableProductTagList(param);
        return R.ok(list);
    }
}
