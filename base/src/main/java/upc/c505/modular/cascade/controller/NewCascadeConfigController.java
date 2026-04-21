package upc.c505.modular.cascade.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import upc.c505.common.responseparam.R;
import upc.c505.modular.cascade.entity.NewCascadeConfig;
import upc.c505.modular.cascade.service.INewCascadeConfigService;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author frd
 * @since 2024-07-15
 */
@RestController
@RequestMapping("/cascade-config")
@Api(tags = {"多级级联表管理"})
public class NewCascadeConfigController {
    //注意：cascade_config这一套在万善乡模块已经存在了，因此在基础端我们使用New作为前缀防止重复报错
    @Autowired
    private INewCascadeConfigService cascadeConfigService;

    @ApiOperation("新增多级级联表数据")
    @PostMapping("/insertCascadeConfig")
    public R<Boolean> insertCascadeConfig(@RequestBody NewCascadeConfig cascadeConfig) {
        return R.ok(cascadeConfigService.save(cascadeConfig));
    }

    @ApiOperation("根据id删除多级级联表数据")
    @GetMapping("/deleteCascadeConfigById")
    public R<Boolean> deleteCascadeConfigById(@RequestParam Long id) {
        return R.ok(cascadeConfigService.removeById(id));
    }

    @ApiOperation("修改多级级联表数据")
    @PostMapping("/updateCascadeConfig")
    public R<Boolean> updateCascadeConfig(@RequestBody NewCascadeConfig cascadeConfig) {
        return R.ok(cascadeConfigService.updateById(cascadeConfig));
    }
}
