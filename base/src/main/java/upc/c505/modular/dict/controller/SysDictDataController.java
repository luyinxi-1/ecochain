package upc.c505.modular.dict.controller;


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
import upc.c505.modular.param.SysDictDataTotalParam;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 字典数据表 前端控制器
 * </p>
 *
 * @author chenchen
 * @since 2022-06-16
 */
@Api(tags = {"数据字典内容查询"})
@RestController
@RequestMapping("/sys-dict-data")
public class SysDictDataController {

    @Autowired
    private ISysDictDataService dictDataService;

    @ApiOperation(value = "下拉框查询")
    @PostMapping("/getPage")
    public R<PageBaseReturnParam<SysDictData>> getPage(@RequestBody SysDictDataSearchParam dictData) {
        return dictDataService.selectDictDataList(dictData);
    }

    /**
     * 新增保存字典数据
     */
    @ApiOperation(value = "添加数据字典内容")
    @PostMapping("/insert")
    public R insert(@RequestBody SysDictData dict) {
//        List a = dictDataService.selectDictDataByDictType(dict.getDictType());
//        dict.setKey(a.size());
        dictDataService.insertDictData(dict);
        return R.commonReturn(200, "新增成功", "");
    }

    /**
     * 修改保存字典数据
     */
    @ApiOperation(value = "修改数据字典内容")
    @PostMapping("/update")
    public R update(@RequestBody SysDictData dict) {
        dictDataService.updateDictData(dict);
        return R.commonReturn(200, "修改成功", "");
    }

    @ApiOperation(value = "删除数据字典内容")
    @PostMapping("/batchDelete")
    public R batchDelete(@RequestBody IdParam idParam) {
        dictDataService.deleteDictDataByIds(idParam);
        return R.commonReturn(200, "删除成功", "");
    }

    @ApiOperation(value = "根据字典数据类型获取字典数据")
    @GetMapping("/selectDictDataByDictType")
    public R selectDictDataByDictType(@RequestParam("dictType") String dictType, @RequestParam(value = "areaId", required = false) Long areaId, @RequestParam(value = "name", required = false) String name) {
        List<SysDictData> list = dictDataService.selectDictDataByDictType(dictType, areaId, name);
        SysDictDataTotalParam param = new SysDictDataTotalParam();
        param.setSysDictDataList(list);
        param.setTotalNum(list.size());
        return R.commonReturn(200, "查询成功", param);
    }

}
