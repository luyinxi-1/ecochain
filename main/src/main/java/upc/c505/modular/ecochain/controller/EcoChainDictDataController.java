package upc.c505.modular.ecochain.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import upc.c505.common.responseparam.PageBaseReturnParam;
import upc.c505.common.responseparam.R;
import upc.c505.modular.dict.controller.searchParam.IdParam;
import upc.c505.modular.dict.controller.searchParam.SysDictDataSearchParam;
import upc.c505.modular.dict.entity.SysDictData;
import upc.c505.modular.dict.service.ISysDictDataService;
import upc.c505.modular.ecochain.controller.param.EcoChainDictDataPageSearchParam;
import upc.c505.modular.ecochain.controller.param.EcoChainDictDataTotalParam;
import upc.c505.modular.ecochain.entity.EcoChainDictData;
import upc.c505.modular.ecochain.mapper.EcoChainDictDataMapper;
import upc.c505.modular.ecochain.service.IEcoChainDictDataService;
import upc.c505.modular.param.SysDictDataTotalParam;
import upc.c505.modular.ecochain.controller.param.EcoChainIdParam;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author byh
 * @since 2024-09-23
 */
@Api(tags = {"生态链数据字典内容查询"})
@RestController
@RequestMapping("/eco-chain-dict-data")
public class EcoChainDictDataController {
    @Autowired
    private IEcoChainDictDataService ecoChainDictDataService;

    @ApiOperation(value = "下拉框查询")
    @PostMapping("/getPage")
    public R<PageBaseReturnParam<EcoChainDictData>> getPage(@RequestBody EcoChainDictDataPageSearchParam dictData) {
        return ecoChainDictDataService.selectDictDataList(dictData);
    }

    /**
     * 新增保存字典数据
     */
    @ApiOperation(value = "添加数据字典内容")
    @PostMapping("/insert")
    public R insert(@RequestBody EcoChainDictData dict) {
//        List a = dictDataService.selectDictDataByDictType(dict.getDictType());
//        dict.setKey(a.size());
        ecoChainDictDataService.insertDictData(dict);
        return R.commonReturn(200, "新增成功", "");
    }

    /**
     * 修改保存字典数据
     */
    @ApiOperation(value = "修改数据字典内容")
    @PostMapping("/update")
    public R update(@RequestBody EcoChainDictData dict) {
        ecoChainDictDataService.updateDictData(dict);
        return R.commonReturn(200, "修改成功", "");
    }

    @ApiOperation(value = "删除数据字典内容")
    @PostMapping("/batchDelete")
    public R batchDelete(@RequestBody EcoChainIdParam idParam) {
        ecoChainDictDataService.deleteDictDataByIds(idParam);
        return R.commonReturn(200, "删除成功", "");
    }

    @ApiOperation(value = "根据字典数据类型获取字典数据")
    @GetMapping("/selectDictDataByDictType")
    public R selectDictDataByDictType(@RequestParam("dictType") String dictType, @RequestParam(value = "areaId", required = false) Long areaId, @RequestParam(value = "name", required = false) String name) {
        List<EcoChainDictData> list = ecoChainDictDataService.selectDictDataByDictType(dictType, areaId, name);
        EcoChainDictDataTotalParam param = new EcoChainDictDataTotalParam();
        param.setEcoChainDictDataList(list);
        param.setTotalNum(list.size());
        return R.commonReturn(200, "查询成功", param);
    }
}
