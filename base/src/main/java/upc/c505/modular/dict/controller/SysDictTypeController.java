package upc.c505.modular.dict.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import upc.c505.common.responseparam.PageBaseReturnParam;
import upc.c505.common.responseparam.R;
import upc.c505.exception.BusinessErrorEnum;
import upc.c505.exception.BusinessException;
import upc.c505.modular.dict.controller.searchParam.IdParam;
import upc.c505.modular.dict.controller.searchParam.SysDictTypeSearchParam;
import upc.c505.modular.dict.entity.SysDictType;
import upc.c505.modular.dict.service.ISysDictTypeService;

/**
 * <p>
 * 字典类型表 前端控制器
 * </p>
 *
 * @author chenchen
 * @since 2022-06-16
 */
@Api(tags = {"数据字典类型查询"})
@RestController
@RequestMapping("/sys-dict-type")
public class SysDictTypeController {
    @Autowired
    private ISysDictTypeService dictTypeService;

    @ApiOperation(value = "分页按条件查询字典类型")
    @PostMapping("/getPage")
    public R<PageBaseReturnParam<SysDictType>> getPage(@RequestBody SysDictTypeSearchParam dictType) {
        return dictTypeService.selectDictTypeList(dictType);
    }

    /**
     * 新增保存字典类型
     */
    @ApiOperation(value = "添加数据字典类型")
    @PostMapping("/insert")
    public R insert(@RequestBody SysDictType dict) {
        if ("0".equals(dictTypeService.checkDictTypeUnique(dict))) {
            throw new BusinessException(BusinessErrorEnum.PARAMETER_VALIDATION_ERROR, "新增字典'" + dict.getDictName() + "'失败，字典类型已存在");
        }
        dictTypeService.insertDictType(dict);
        return R.commonReturn(200, "新增成功", "");
    }

    /**
     * 修改保存字典类型
     */
    @ApiOperation(value = "修改数据字典类型")
    @PostMapping("/update")
    public R update(@RequestBody SysDictType dict) {
        dictTypeService.updateDictType(dict);
        return R.commonReturn(200, "修改成功", "");
    }

    @ApiOperation(value = "删除数据字典内容")
    @PostMapping("/batchDelete")
    public R batchDelete(@RequestBody IdParam idParam) {
        dictTypeService.deleteDictTypeByIds(idParam);
        return R.commonReturn(200, "删除成功", "");
    }

    /**
     * 查询字典详细
     */
    @ApiOperation(value = "查询数据字典类型内容")
    @PostMapping("/getById")
    public R getById(@RequestParam("dictId") Integer dictId) {
        SysDictType dictType = dictTypeService.selectDictTypeById(dictId);
        return R.commonReturn(200, "查询成功", dictType);
    }

    /**
     * 校验字典类型
     */
    @ApiOperation(value = "检查数据字典类型是否已存在")
    @PostMapping("/checkDictTypeUnique")
    public String checkDictTypeUnique(SysDictType dictType) {
        return dictTypeService.checkDictTypeUnique(dictType);
    }
}
