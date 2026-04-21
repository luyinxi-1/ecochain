package upc.c505.modular.ecochain.controller;


import com.sun.org.apache.xpath.internal.operations.Bool;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import upc.c505.common.responseparam.R;
import upc.c505.modular.ecochain.controller.param.EcoChainExtendedTableUpdateParam;
import upc.c505.modular.ecochain.service.IEcoChainExtendedTableService;
import upc.c505.modular.ecochain.entity.EcoChainExtendedTable;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author la
 * @since 2024-09-24
 */
@RestController
@RequestMapping("/eco-chain-extended-table")
@Api(tags = "主体表扩充表")
public class EcoChainExtendedTableController {
    @Autowired
    IEcoChainExtendedTableService ecoChainExtendedTableService;

    @ApiOperation("新增主体表扩充表信息")
    @PostMapping("/insertExtendedTable")
    public R<Integer> insertExtendedTable(@RequestBody EcoChainExtendedTableUpdateParam ecoChainExtendedTable){
        return R.ok(ecoChainExtendedTableService.insertExtendedTable(ecoChainExtendedTable));
    }

    @ApiOperation("删除主体表扩充表信息")
    @PostMapping("/deleteExtendedTable")
    public R deleteExtendedTable(@RequestBody EcoChainExtendedTable ecoChainExtendedTable){
        Boolean result = ecoChainExtendedTableService.removeById(ecoChainExtendedTable);
        return R.ok(result);
    }

    @ApiOperation("修改主体表扩充表信息")
    @PostMapping("/updateExtendedTable")
    public R<Integer> updateExtendedTable(@RequestBody EcoChainExtendedTableUpdateParam param){
        return R.ok(ecoChainExtendedTableService.updateExtendedTable(param));
    }


    @ApiOperation("查询主体表扩充表信息")
    @PostMapping("/selectExtendedTable")
    public R<EcoChainExtendedTable> selectExtendedTable(@RequestBody EcoChainExtendedTable param){
        EcoChainExtendedTable ecoChainExtendedTable = ecoChainExtendedTableService.selectExtendedTable(param);
        return R.ok(ecoChainExtendedTable);
    }
}
