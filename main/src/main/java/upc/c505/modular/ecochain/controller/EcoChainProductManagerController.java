package upc.c505.modular.ecochain.controller;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import upc.c505.common.responseparam.PageBaseReturnParam;
import upc.c505.common.responseparam.R;
import upc.c505.modular.ecochain.controller.param.*;
import upc.c505.modular.ecochain.entity.EcoChainProductManager;
import upc.c505.modular.ecochain.entity.EcoChainProductParameter;
import upc.c505.modular.ecochain.service.IEcoChainProductManagerService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author xth
 * @since 2024-09-25
 */
@RestController
@RequestMapping("/eco-chain-product-manager")
@Api(tags = "产品管理")
public class EcoChainProductManagerController {
    @Autowired
    private IEcoChainProductManagerService ecoChainProductManagerService;

    @PostMapping("/insertProduct")
    @ApiOperation("新增产品")
    public R<Boolean> insertProduct(@RequestBody EcoChainInsertAndUpdateProductParam param) {
        boolean result = ecoChainProductManagerService.insertProduct(param);
        return R.ok(result);
    }

    @PostMapping("/updateProduct")
    @ApiOperation("修改产品信息")
    public R<Boolean> updateProduct(@RequestBody EcoChainInsertAndUpdateProductParam param) {
        boolean result = ecoChainProductManagerService.updateProduct(param);
        return R.ok(result);
    }

    @PostMapping("/removeProductBatch")
    @ApiOperation("批量删除产品信息")
    public R<Boolean> removeProductBatch(@RequestBody List<Long> ids) {
        ecoChainProductManagerService.removeProductBatch(ids);
        return R.ok();
    }

    @PostMapping("/selectPageProduct")
    @ApiOperation("分页查询产品信息")
    public R<PageBaseReturnParam<EcoChainProductManager>> selectPageProduct(
            @RequestBody EcoChainProductManagerPageSearchParam param) {
        Page<EcoChainProductManager> page = ecoChainProductManagerService.selectPageProduct(param);
        PageBaseReturnParam<EcoChainProductManager> p = PageBaseReturnParam.ok(page);
        return R.page(p);
    }

    @PostMapping("/selectProductById")
    @ApiOperation("根据id查询产品信息")
    public R<EcoChainProductManager> selectProductById(@RequestParam Long id) {
        if (id != null && id != 0) {
            EcoChainProductManager ecoChainProductManager = ecoChainProductManagerService.getById(id);
            return R.ok(ecoChainProductManager);
        }
        return R.fail();
    }

    @PostMapping("/selectProductByClassificationId")
    @ApiOperation("根据产品分类id查询产品信息")
    public R<List<EcoChainProductManager>> selectProductByClassificationId(@RequestParam Long classificationId,
            @RequestParam String socialCreditCode, @RequestParam String productName) {
        List<EcoChainProductManager> list = ecoChainProductManagerService
                .selectProductByClassificationId(classificationId, socialCreditCode, productName, null, 1, 0);
        return R.ok(list);
    }

    @PostMapping("/selectProduct")
    @ApiOperation("查询产品（小程序端）")
    public R<List<EcoChainSelectProductReturnParam>> selectProduct(
            @RequestBody EcoChainSelectProductMapSearchParam param) {
        List<EcoChainSelectProductReturnParam> list = ecoChainProductManagerService.selectProduct(param);
        return R.ok(list);
    }

    @PostMapping("/updateAllProduct")
    @ApiOperation("更新某企业全部产品相关字段")
    public R<Boolean> updateAllProduct(@RequestBody EcoChainProductAllUpdate param) {
        Boolean result = ecoChainProductManagerService.updateAllProduct(param);
        return R.ok(result);
    }

}
