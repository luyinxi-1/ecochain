package upc.c505.modular.ecochain.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import upc.c505.common.responseparam.PageBaseReturnParam;
import upc.c505.common.responseparam.R;
import upc.c505.exception.BusinessErrorEnum;
import upc.c505.exception.BusinessException;
import upc.c505.modular.ecochain.controller.param.EcoChainDictTypePageSearchParam;
import upc.c505.modular.ecochain.controller.param.EcoChainIdParam;
import upc.c505.modular.ecochain.entity.EcoChainDictType;
import upc.c505.modular.ecochain.service.IEcoChainDictTypeService;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author byh
 * @since 2024-09-23
 */
@Api(tags = {"生态链数据字典类型查询"})
@RestController
@RequestMapping("/eco-chain-dict-type")
public class EcoChainDictTypeController {
    @Autowired
    private IEcoChainDictTypeService ecoChainDictTypeService;

    @ApiOperation(value = "分页按条件查询字典类型")
    @PostMapping("/getPage")
    public R<PageBaseReturnParam<EcoChainDictType>> getPage(@RequestBody EcoChainDictTypePageSearchParam dictType) {
        return ecoChainDictTypeService.selectDictTypeList(dictType);
    }

    /**
     * 新增保存字典类型
     */
    @ApiOperation(value = "添加数据字典类型")
    @PostMapping("/insert")
    public R insert(@RequestBody EcoChainDictType dict) {
        if ("0".equals(ecoChainDictTypeService.checkDictTypeUnique(dict))) {
            throw new BusinessException(BusinessErrorEnum.PARAMETER_VALIDATION_ERROR, "新增字典'" + dict.getDictName() + "'失败，字典类型已存在");
        }
        ecoChainDictTypeService.insertDictType(dict);
        return R.commonReturn(200, "新增成功", "");
    }

    /**
     * 修改保存字典类型
     */
    @ApiOperation(value = "修改数据字典类型")
    @PostMapping("/update")
    public R update(@RequestBody EcoChainDictType dict) {
        ecoChainDictTypeService.updateDictType(dict);
        return R.commonReturn(200, "修改成功", "");
    }

    @ApiOperation(value = "删除数据字典内容")
    @PostMapping("/batchDelete")
    public R batchDelete(@RequestBody EcoChainIdParam idParam) {
        ecoChainDictTypeService.deleteDictTypeByIds(idParam);
        return R.commonReturn(200, "删除成功", "");
    }

    /**
     * 查询字典详细
     */
    @ApiOperation(value = "查询数据字典类型内容")
    @PostMapping("/getById")
    public R getById(@RequestParam("dictId") Integer dictId) {
        EcoChainDictType dictType = ecoChainDictTypeService.selectDictTypeById(dictId);
        return R.commonReturn(200, "查询成功", dictType);
    }

    /**
     * 校验字典类型
     */
    @ApiOperation(value = "检查数据字典类型是否已存在")
    @PostMapping("/checkDictTypeUnique")
    public R<String> checkDictTypeUnique(@RequestBody EcoChainDictType dictType) {
        return R.ok(ecoChainDictTypeService.checkDictTypeUnique(dictType));
    }

}
