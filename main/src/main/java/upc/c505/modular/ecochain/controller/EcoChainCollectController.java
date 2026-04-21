package upc.c505.modular.ecochain.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import upc.c505.common.responseparam.R;
import upc.c505.modular.ecochain.controller.param.EcoChainCollectInsertParam;
import upc.c505.modular.ecochain.controller.param.EcoChainCollectSearchParam;
import upc.c505.modular.ecochain.controller.param.EcoChainProductViewNumberParam;
import upc.c505.modular.ecochain.service.IEcoChainCollectService;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author byh
 * @since 2025-01-20
 */
@RestController
@RequestMapping("/eco-chain-collect")
@Api(tags = "生态链收藏")
public class EcoChainCollectController {
    @Autowired
    private IEcoChainCollectService ecoChainCollectService;

    @PostMapping("/insertCollect")
    @ApiOperation("新增收藏")
    public R<Long> insertCollect(@RequestBody EcoChainCollectInsertParam param){
        return R.ok(ecoChainCollectService.insertCollect(param));
    }

    @PostMapping("/deleteCollect")
    @ApiOperation("删除收藏")
    public R<Boolean> deleteCollect(@RequestParam Long id){
        return R.ok(ecoChainCollectService.deleteCollect(id));
    }

    @PostMapping("/selectCollectNumber")
    @ApiOperation("查询收藏数量")
    public R<Long> selectCollectNumber(@RequestParam Integer type, @RequestParam String collectId){
        return R.ok(ecoChainCollectService.selectCollectNumber(type, collectId));
    }

    @PostMapping("/selectIsOrNoCollect")
    @ApiOperation("查询是否收藏")
    public R<List<Long>> selectIsOrNoCollect(@RequestParam Integer type, @RequestParam String collectId){
        return R.ok(ecoChainCollectService.selectIsOrNoCollect(type, collectId));
    }

    @PostMapping("/selectCollectInformation")
    @ApiOperation("查询收藏全部信息")
    public R<List<?>> selectCollectInformation(@RequestBody EcoChainCollectSearchParam param){
        return R.ok(ecoChainCollectService.selectCollectInformation(param));
    }

    @PostMapping("/selectProductViewNumber")
    @ApiOperation("产品id的浏览量")
    public R<EcoChainProductViewNumberParam> selectProductViewNumber(@RequestParam Long id){
        EcoChainProductViewNumberParam result = ecoChainCollectService.selectProductNumber(id);
        return R.ok(result);
    }

}
