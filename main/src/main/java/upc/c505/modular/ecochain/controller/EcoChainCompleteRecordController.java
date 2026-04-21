package upc.c505.modular.ecochain.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import upc.c505.modular.ecochain.controller.param.EcoChainBuildWareHouseFinishParam;
import upc.c505.modular.ecochain.entity.EcoChainCompleteRecord;
import upc.c505.modular.ecochain.service.IEcoChainCompleteRecordService;
import upc.c505.common.responseparam.R;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author xth
 * @since 2024-09-21
 */
@RestController
@RequestMapping("/eco-chain-complete-record")
@Api(tags = "完成记录")
public class EcoChainCompleteRecordController {
    @Autowired
    private IEcoChainCompleteRecordService ecoChainCompleteRecordService;

    @PostMapping("/insertCompleteRecord")
    @ApiOperation("新增完成记录")
    public R<Void> insertCompleteRecord(@RequestBody EcoChainCompleteRecord param){
        ecoChainCompleteRecordService.insertCompleteRecord(param);
        return R.ok();
    }

    @PostMapping("/updateCompleteRecord")
    @ApiOperation("更新完成记录")
    public R<Boolean> updateCompleteRecord(@RequestBody EcoChainCompleteRecord param){
        boolean result = ecoChainCompleteRecordService.updateCompleteRecord(param);
        return R.ok(result);
    }

    @PostMapping("/selectCompleteRecord")
    @ApiOperation("查询完成记录（单个）")
    public R<EcoChainCompleteRecord> selectCompleteRecord(@RequestParam Long id){
        EcoChainCompleteRecord result = ecoChainCompleteRecordService.getById(id);
        return R.ok(result);
    }

    @PostMapping("/removeCompleteRecord")
    @ApiOperation("批量删除完成记录")
    public R<Void> removeCompleteRecord(@RequestParam List<Long> idList){
        ecoChainCompleteRecordService.removeCompleteRecord(idList);
        return R.ok();
    }

    @PostMapping("/listByWarehouseId")
    @ApiOperation("查询库下的完成记录")
    public R<List<EcoChainCompleteRecord>> listByWarehouseId(@RequestParam Long warehouseId){
        List<EcoChainCompleteRecord> list = ecoChainCompleteRecordService.listByWarehouseId(warehouseId);
        return R.ok(list);
    }

    @PostMapping("/finishWarehouse")
    @ApiOperation("全部完工")
    public R<EcoChainBuildWareHouseFinishParam> finishWarehouse(@RequestBody EcoChainBuildWareHouseFinishParam param){
        ecoChainCompleteRecordService.finishWarehouse(param);
        return R.ok(param);
    }
}
