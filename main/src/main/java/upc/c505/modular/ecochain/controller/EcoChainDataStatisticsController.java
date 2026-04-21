package upc.c505.modular.ecochain.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import upc.c505.common.responseparam.PageBaseReturnParam;
import upc.c505.common.responseparam.R;
import upc.c505.modular.ecochain.controller.param.*;
import upc.c505.modular.ecochain.entity.EcoChainDataStatistics;
import upc.c505.modular.ecochain.service.IEcoChainDataStatisticsService;
import lombok.extern.slf4j.Slf4j;
import upc.c505.modular.ecochain.util.GetUserInfo;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author la
 * @since 2024-09-26
 */

@RestController
@RequestMapping("/eco-chain-data-statistics")
@Api(tags = "数据分析模块")
@Slf4j
public class EcoChainDataStatisticsController {
    @Autowired
    IEcoChainDataStatisticsService ecoChainDataStatisticsService;
    @Autowired
    private GetUserInfo getUserInfo;

    @ApiOperation("新增数据统计信息")
    @PostMapping("/insertDataStatistics")
    public R<Boolean> insertDataStatistics(@RequestBody EcoChainDataStatistics ecoChainDataStatistics){
        return R.ok(ecoChainDataStatisticsService.insertDataStatistics(ecoChainDataStatistics));
    }

    @ApiOperation("删除数据统计信息")
    @PostMapping("/deleteDataStatistics")
    public R deleteDataStatistics(@RequestBody EcoChainDataStatistics ecoChainDataStatistics){
        return R.ok(ecoChainDataStatisticsService.removeById(ecoChainDataStatistics));
    }
    @ApiOperation("更新数据统计信息")
    @PostMapping("/updayeDataStatistics")
    public R updateDataStatistics(@RequestBody EcoChainDataStatistics ecoChainDataStatistics){
        return R.ok(ecoChainDataStatisticsService.updateById(ecoChainDataStatistics));
    }

    @ApiOperation("分析概览模块")
    @PostMapping("/selectAnalyticalOverview")
    public R<EcoChainAnalyticalOverviewReturnParamOne> selectAnalyticalOverview(@RequestBody EcoChainAnalyticalOverviewSearchParam param){
        return R.ok(ecoChainDataStatisticsService.selectAnalyticalOverview(param));
    }

    @ApiOperation("产品分析折线图")
    @PostMapping("/productAnalyticalChart")
    public R<EcoChainProductAnalysisReturnParam> productAnalyticalChart(@RequestBody EcoChainProductAnalyticalChartSearchParam param){
        EcoChainProductAnalysisReturnParam result = ecoChainDataStatisticsService.productAnalyticalChart(param);
        return R.ok(result);
    }

    @ApiOperation("产品分析分页查询")
    @PostMapping("/productAnalyticalPage")
    public R<PageBaseReturnParam<EcoChainProductAnalyticalPageReturnParam>> productAnalyticalPage(@RequestBody EcoChainProductAnalyticalPageSearchParam param){
        Page<EcoChainProductAnalyticalPageReturnParam> page = ecoChainDataStatisticsService.productAnalyticalPage(param);
        PageBaseReturnParam<EcoChainProductAnalyticalPageReturnParam> p = PageBaseReturnParam.ok(page);
        return R.ok(p);
    }

    @PostMapping("selectPotentialCustomer")
    @ApiOperation("查询潜在客户")
    public R<PageBaseReturnParam<EcoChainPotentialCustomerPageReturnParam>> selectPotentialCustomer(@RequestBody EcoChainPotentialCustomerPageSearchParam param){
        Page<EcoChainPotentialCustomerPageReturnParam> page = ecoChainDataStatisticsService.selectPotentialCustomer(param);
        PageBaseReturnParam<EcoChainPotentialCustomerPageReturnParam> p = PageBaseReturnParam.ok(page);
        return R.ok(p);
    }

    @ApiOperation("分析概览模块（小程序端）")
    @PostMapping("/selectAnalyticalOverviewMiniProgram")
        public R<EcoChainAnalyticalOverviewReturnParamMiniProgram> selectAnalyticalOverviewMiniProgram(@RequestBody EcoChainAnalyticalOverviewSearchParam param){
        return R.ok(ecoChainDataStatisticsService.selectAnalyticalOverviewMiniProgram(param));
    }
}
