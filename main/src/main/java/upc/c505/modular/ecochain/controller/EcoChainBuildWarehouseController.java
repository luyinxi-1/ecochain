package upc.c505.modular.ecochain.controller;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import upc.c505.common.responseparam.PageBaseReturnParam;
import upc.c505.common.responseparam.R;
import upc.c505.modular.ecochain.controller.param.EcoChainBuildWareHousePageSearchParam;
import upc.c505.modular.ecochain.controller.param.EcoChainBuildWarehouseReturnParam;
import upc.c505.modular.ecochain.controller.param.EcoChainBuildWarehouseVisiblePeopleUpdateParam;
import upc.c505.modular.ecochain.controller.param.EcoChainRegionalConfigurationPageSearchParam;
import upc.c505.modular.ecochain.entity.EcoChainBuildWarehouse;
import upc.c505.modular.ecochain.entity.EcoChainRegionalConfiguration;
import upc.c505.modular.ecochain.service.IEcoChainBuildWarehouseService;

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
@RequestMapping("/eco-chain-build-warehouse")
@Api(tags = "生态链建库")
public class EcoChainBuildWarehouseController {
    @Autowired
    private IEcoChainBuildWarehouseService ecoChainBuildWarehouseService;

    @PostMapping("/insertBuildWarehouse")
    @ApiOperation("新增库")
    @Transactional
    public R<Void> insertBuildWarehouse(@RequestBody EcoChainBuildWarehouse param){
        ecoChainBuildWarehouseService.insertBuildWarehouse(param);
        return R.ok();
    }

    @PostMapping("/updateBuildWarehouse")
    @ApiOperation("更新库")
    public R<Boolean> updateBuildWarehouse(@RequestBody EcoChainBuildWarehouse param){
        boolean result = ecoChainBuildWarehouseService.updateByWarehouseId(param);
        return R.ok(result);
    }

    @PostMapping("/selectBuildWarehouse")
    @ApiOperation("查询库（单个）")
    public R<EcoChainBuildWarehouseReturnParam> selectBuildWarehouse(@RequestParam Long id) {
        EcoChainBuildWarehouseReturnParam returnParam = ecoChainBuildWarehouseService.selectBuildWarehouse(id);
        if (returnParam == null) {
            return R.fail("未找到指定的库");
        }

        return R.ok(returnParam);

   }

    @PostMapping("/removeBuildWarehouse")
    @ApiOperation("批量删除库")
    public R<Void> removeBuildWarehouse(@RequestParam List<Long> idList){
        ecoChainBuildWarehouseService.removeBatchByIdList(idList);
        return R.ok();
    }

    @PostMapping("/selectPageBuildWarehouse")
    @ApiOperation("分页查询库（田负责）")
    public R<PageBaseReturnParam<EcoChainBuildWarehouseReturnParam>> selectPageBuildWarehouse(@RequestBody EcoChainBuildWareHousePageSearchParam param) {
        Page<EcoChainBuildWarehouseReturnParam> page = ecoChainBuildWarehouseService.selectPageBuildWarehouse(param);
        PageBaseReturnParam<EcoChainBuildWarehouseReturnParam> p = PageBaseReturnParam.ok(page);
        return R.page(p);
    }

    @PostMapping("/getPeriodByBuildWarehouseId")
    @ApiOperation("根据库 id 查询周期")
    public R<Integer> getPeriodByBuildWarehouseId(@RequestParam Long buildWarehouseId){
        Integer period = ecoChainBuildWarehouseService.getPeriodByBuildWarehouseId(buildWarehouseId);
        return R.ok();
    }

    @PostMapping("/importBuildWarehouseData")
    @ApiOperation("导入生态链建库数据")
    public R<Integer> importBuildWarehouseData(@RequestParam("file") MultipartFile file) {
        try {
            Integer importCount = ecoChainBuildWarehouseService.importBuildWarehouseData(file);
            return R.commonReturn(200, "导入成功，共导入 " + importCount + " 条记录", importCount);
        } catch (Exception e) {
            e.printStackTrace();
            return R.fail("导入失败：" + e.getMessage());
        }
    }

    @PostMapping("/batchUpdateVisiblePeople")
    @ApiOperation("批量修改库的可见人")
    public R<Boolean> batchUpdateVisiblePeople(@RequestBody EcoChainBuildWarehouseVisiblePeopleUpdateParam param) {
        boolean result = ecoChainBuildWarehouseService.batchUpdateVisiblePeople(
                param.getWarehouseIds(), 
                param.getVisiblePeopleIds()
        );
        return R.ok(result);
    }
}
