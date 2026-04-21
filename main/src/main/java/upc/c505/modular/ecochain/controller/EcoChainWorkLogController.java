package upc.c505.modular.ecochain.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import upc.c505.common.responseparam.PageBaseReturnParam;
import upc.c505.common.responseparam.R;
import upc.c505.modular.ecochain.controller.param.EcoChainBuildWareHousePageSearchParam;
import upc.c505.modular.ecochain.controller.param.EcoChainBuildWarehouseReturnParam;
import upc.c505.modular.ecochain.controller.param.EcoChainWorkLogParam.*;
import upc.c505.modular.ecochain.entity.EcoChainWorkLog;
import upc.c505.modular.ecochain.service.IEcoChainWorkLogService;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

import upc.c505.modular.ecochain.controller.param.EcoChainWorkLogGetByIdParam;
import upc.c505.modular.ecochain.controller.param.EcoChainWorkLogGetByIdReturnParam;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author byh
 * @since 2025-04-16
 */
@RestController
@RequestMapping("/eco-chain-work-log")
@Api(tags = "生态链工作日志")
public class EcoChainWorkLogController {
    @Autowired
    private IEcoChainWorkLogService workLogService;

    @PostMapping("/selectWorkLog")
    @ApiOperation("查询工作日志")
    public R<Page<SelectWorkLogReturnParam>> selectWorkLog(@RequestBody SelectWorkLogSearchParam searchParam) {
        Page<SelectWorkLogReturnParam> returnParam = workLogService.selectWorkLog(searchParam);
        if (returnParam == null) {
            return R.fail("未找到指定的工作日志");
        }
        return R.ok(returnParam);
    }

    @PostMapping("/exportWorkLog")
    @ApiOperation("导出工作日志Excel")
    public void exportWorkLog(@RequestParam String prefixUrl, HttpServletResponse response, @RequestBody SelectWorkLogSearchParam searchParam) {
        workLogService.exportWorkLog(prefixUrl, response, searchParam);
    }

    @Autowired
    private IEcoChainWorkLogService ecoChainWorkLogService;

    @PostMapping("/update")
    @ApiOperation("修改评价")
    public R<Boolean> updateWorkLog(@RequestBody EcoChainWorkLog param){
        return R.ok(ecoChainWorkLogService.updateWorkLog(param));
    }

    /**
     * @description: 工作统计
     */
    @PostMapping("/work-statistics")
    @ApiOperation("工作统计")
    public R<List<EcoChainWorkStatisticsReturnParam>> workStatistics(@RequestBody EcoChainWorkStatisticsSearchParam param){
        return R.ok(ecoChainWorkLogService.workStatistics(param));
    }

    /**
     * @description: 工作统计-签单率
     */
    @PostMapping("/work-statistics-signContractRate")
    @ApiOperation("工作统计-签单率")
    public R<List<EcoChainWorkStatisticsSignContractRateReturnParam>> workStatisticsSignContractRate(@RequestBody EcoChainWorkStatisticsSearchParam param){
        return R.ok(ecoChainWorkLogService.workStatisticsSignContractRate(param));
    }

    @PostMapping("/exportWorkStatistics")
    @ApiOperation("导出工作统计")
    public void exportWorkStatistics(HttpServletResponse response, @RequestBody EcoChainWorkStatisticsSearchParam param){
        ecoChainWorkLogService.exportWorkStatistics(response, param);
    }


    @PostMapping("/getDetailsById")
    @ApiOperation("根据id查看详情")
    public R<EcoChainWorkLogGetByIdReturnParam> getDetailsById(@RequestBody EcoChainWorkLogGetByIdParam param){
        EcoChainWorkLogGetByIdReturnParam result = ecoChainWorkLogService.getDetailsById(param);
        return R.ok(result);
    }

    @PostMapping("/selectPageBuildWarehouseGroup")
    @ApiOperation("分组查询库")
    public R<PageBaseReturnParam<EcoChainBuildWarehouseReturnParam>> selectPageBuildWarehouseGroup(@RequestBody EcoChainBuildWareHousePageSearchParam param) {
        Page<EcoChainBuildWarehouseReturnParam> page = workLogService.selectPageBuildWarehouseGroup(param);
        PageBaseReturnParam<EcoChainBuildWarehouseReturnParam> p = PageBaseReturnParam.ok(page);
        return R.page(p);
    }

    @PostMapping("/selectPageBuildWarehouseGroupByLogTime")
    @ApiOperation("分组查询库-按日志时间")
    public R<PageBaseReturnParam<EcoChainBuildWarehouseReturnParam>> selectPageBuildWarehouseGroupByLogTime(@RequestBody EcoChainBuildWareHousePageSearchParam param) {
        Page<EcoChainBuildWarehouseReturnParam> page = workLogService.selectPageBuildWarehouseGroupByLogTime(param);
        PageBaseReturnParam<EcoChainBuildWarehouseReturnParam> p = PageBaseReturnParam.ok(page);
        return R.page(p);
    }
}
