package upc.c505.modular.ecochain.controller;


import com.sun.org.apache.xpath.internal.operations.Bool;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import upc.c505.modular.ecochain.controller.param.EcoChainPotentialCustomerAnalysisSearchParam;
import upc.c505.modular.ecochain.entity.EcoChainPotentialCustomerAnalysis;
import upc.c505.modular.ecochain.service.IEcoChainPotentialCustomerAnalysisService;
import upc.c505.common.responseparam.R;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author la
 * @since 2024-11-22
 */
@RestController
@RequestMapping("/eco-chain-potential-customer-analysis")
@Api(tags = "潜在用户分析")
@Slf4j
public class EcoChainPotentialCustomerAnalysisController {
    @Autowired
    IEcoChainPotentialCustomerAnalysisService ecoChainPotentialCustomerAnalysisService;

    @ApiOperation("新增潜在客户分析")
    @PostMapping("/insertPotentialCustomerAnalysis")
    public R<Boolean> insertPotentialCustomerAnalysis(@RequestBody EcoChainPotentialCustomerAnalysis ecoChainPotentialCustomerAnalysis){
        return R.ok(ecoChainPotentialCustomerAnalysisService.insertPotentialCustomerAnalysis(ecoChainPotentialCustomerAnalysis));
    }

    @ApiOperation("删除潜在客户分析")
    @PostMapping("/deletePotentialCustomerAnalysis")
    public R deletePotentialCustomerAnalysis(@RequestBody EcoChainPotentialCustomerAnalysis ecoChainPotentialCustomerAnalysis){
        return R.ok(ecoChainPotentialCustomerAnalysisService.removeById(ecoChainPotentialCustomerAnalysis));
    }

    @ApiOperation("修改潜在客户分析")
    @PostMapping("/updatePotentialCustomerAnalysis")
    public R updatePotentialCustomerAnalysis(@RequestBody EcoChainPotentialCustomerAnalysis ecoChainPotentialCustomerAnalysis){
        return R.ok(ecoChainPotentialCustomerAnalysisService.updateById(ecoChainPotentialCustomerAnalysis));
    }

    @ApiOperation("查询潜在客户分析")
    @PostMapping("/selectPotentialCustomerAnalysis")
    public R<List<EcoChainPotentialCustomerAnalysis>> selectPotentialCustomerAnalysis(@RequestBody EcoChainPotentialCustomerAnalysisSearchParam ecoChainPotentialCustomerAnalysis){
        return R.ok(ecoChainPotentialCustomerAnalysisService.selectPotentialCustomerAnalysis(ecoChainPotentialCustomerAnalysis));
    }

    @ApiOperation("查询openid对应数据是否存在")
    @PostMapping("/selectPotentialCustomerByOpenid")
    public R<Integer> selectPotentialCustomerByOpenid(@RequestBody EcoChainPotentialCustomerAnalysis ecoChainPotentialCustomerAnalysis){
        Integer result = ecoChainPotentialCustomerAnalysisService.selectPotentialCustomerByOpenid(ecoChainPotentialCustomerAnalysis);
        return R.ok(result);
    }
}
