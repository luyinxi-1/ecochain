package upc.c505.modular.auth.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import upc.c505.common.responseparam.R;

import upc.c505.modular.auth.controller.param.AuthModelParam;
import upc.c505.modular.auth.controller.param.tree.AuthModelTreeNode;
import upc.c505.modular.auth.service.ISysAuthModelService;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * <p>
 * 权限模块表 前端控制器
 * </p>
 *
 * @author sxz
 * @since 2023-07-30
 */
@RestController
@RequestMapping("/sys-auth-model")
@Api(tags = "权限模块操作")
public class SysAuthModelController {

    @Autowired
    private ISysAuthModelService sysAuthModelService;

    @PostMapping("/addModel")
    @ApiOperation("添加权限模块")
    public R<String> addModel(@RequestBody @Validated AuthModelParam authModelParam) {sysAuthModelService.addModel(authModelParam);
        return R.ok();
    }

    @DeleteMapping("/deleteModelsByIdList")
    @ApiOperation("根据list删除权限模块，其下的权限会一起删除")
    public R<String> deleteModelsByIdList(@RequestParam("idList")
                                          @NotEmpty(message = "数组不能为空")
                                                  List<Integer> idList) {
        sysAuthModelService.deleteModelsByIdList(idList);
        return R.ok();
    }

    @PostMapping("/updateModelById")
    @ApiOperation("根据id更改权限模块信息")
    public R<String> updateModelById(@RequestBody AuthModelParam authModelParam) {
        sysAuthModelService.updateModelById(authModelParam);
        return R.ok();
    }

    @GetMapping("/getModelPage")
    @ApiOperation("查询权限模块列表")
    public R<List<AuthModelTreeNode>> getModelPage(@RequestParam("parentId") @ApiParam("上级模块id（最上级传0）") Long parentId) {
        return R.ok(sysAuthModelService.getModelPage(parentId));
    }
}
