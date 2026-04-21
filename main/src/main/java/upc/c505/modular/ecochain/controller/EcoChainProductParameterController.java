package upc.c505.modular.ecochain.controller;


import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import upc.c505.modular.ecochain.controller.param.EcoChainProductParameterSearchParam;
import upc.c505.modular.ecochain.entity.EcoChainProductParameter;
import upc.c505.modular.ecochain.entity.EcoChainProductParameterValueContent;
import upc.c505.modular.ecochain.service.IEcoChainProductParameterService;
import upc.c505.common.responseparam.R;
import upc.c505.modular.ecochain.service.IEcoChainProductParameterValueContentService;

import java.util.ArrayList;
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
@Api(tags = "产品参数")
@RequestMapping("/eco-chain-product-parameter")
public class EcoChainProductParameterController {

    @Autowired
    private IEcoChainProductParameterService ecoChainProductParameterService;

    @Autowired
    private IEcoChainProductParameterValueContentService ecoChainProductParameterValueContentService;

    @PostMapping("/insertProductParameter")
    @ApiOperation("新增产品参数")
    public R<Void> insertProductParameter(@RequestBody EcoChainProductParameter param){
        ecoChainProductParameterService.insertProductParameter(param);
        return R.ok();
    }

    @PostMapping("/removeProductParameter")
    @ApiOperation("删除产品参数")
    public R<Void> removeProductParameter(@RequestParam List<Long> idList){
        ecoChainProductParameterService.removeProductParameter(idList);
        return R.ok();
    }

    @PostMapping("/updateProductParameter")
    @ApiOperation("更新产品参数")
    public R<Boolean> updateProductParameter(@RequestBody EcoChainProductParameter param){
        return R.ok(ecoChainProductParameterService.updateProductParameter(param));
    }

    @PostMapping("/updateProductParameterStatus")
    @ApiOperation("更新产品参数状态")
    public R<Boolean> updateProductParameterStatus(@RequestParam Long id, @RequestParam Integer status){
        boolean result = ecoChainProductParameterService.updateProductParameterStatus(id, status);
        return R.ok(result);
    }

    @PostMapping("/updateProductParameterSortNumber")
    @ApiOperation("更新产品参数排序(0向上，1向下)")
    public R<Boolean> updateProductParameterSortNumber(@RequestParam Long id, @RequestParam Integer param){
        boolean result = ecoChainProductParameterService.updateProductParameterSortNumber(id, param);
        return R.ok(result);
    }

    @PostMapping("/selectProductParameterList")
    @ApiOperation("查询产品参数列表")
    public R<List<EcoChainProductParameter>> selectProductParameterList(@RequestBody EcoChainProductParameterSearchParam param){
        List<EcoChainProductParameter> resultList = ecoChainProductParameterService.selectProductParameterList(param);
        return R.ok(resultList);
    }

    @PostMapping("/selectProductParameterById")
    @ApiOperation("根据产品参数id查")
    public R<String> selectProductParameterById(@RequestParam Long id){
        if (ObjectUtils.isNotEmpty(ecoChainProductParameterService.selectProductParameterById(id))) {
            String name = ecoChainProductParameterService.selectProductParameterById(id);
            return R.ok(name);
        }
        return R.ok("");
    }

    @PostMapping("/selectEnableProductParameter")
    @ApiOperation("获取启用产品参数列表")
    public R<List<EcoChainProductParameter>> selectEnableProductParameter(@RequestBody EcoChainProductParameterSearchParam param){
        List<EcoChainProductParameter> list = ecoChainProductParameterService.selectEnableProductParameter(param);
        return R.ok(list);
    }

}
