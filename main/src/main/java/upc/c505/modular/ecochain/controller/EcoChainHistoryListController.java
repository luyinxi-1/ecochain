package upc.c505.modular.ecochain.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import upc.c505.modular.ecochain.entity.EcoChainHistoryList;
import upc.c505.modular.ecochain.service.IEcoChainHistoryListService;
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
@Api(tags = {"历史记录"})
@RequestMapping("/eco-chain-history-list")
public class EcoChainHistoryListController {
    @Autowired
    private IEcoChainHistoryListService ecoChainHistoryListService;

    @PostMapping("/insertHistoryList")
    @ApiOperation("新增历史记录")
    public R<Void> insertHistoryList(@RequestBody EcoChainHistoryList param){
        ecoChainHistoryListService.insertHistoryList(param);
        return R.ok();
    }

    @PostMapping("/removeHistoryList")
    @ApiOperation("批量删除删除历史记录")
    public R<Void> removeHistoryList(@RequestParam List<Long> idList){
        ecoChainHistoryListService.removeBatchByIds(idList);
        return R.ok();
    }

    @PostMapping("/countCompleteRecord")
    @ApiOperation("统计完成记录数")
    public R<Integer> countCompleteRecord(@RequestParam Long warehouseId){
        Integer count1 = ecoChainHistoryListService.countCompleteRecord(warehouseId);
        return R.ok(count1);
    }

}
