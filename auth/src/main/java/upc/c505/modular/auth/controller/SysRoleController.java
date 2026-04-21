package upc.c505.modular.auth.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import upc.c505.common.responseparam.PageBaseReturnParam;
import upc.c505.common.responseparam.R;
import upc.c505.modular.auth.controller.param.tree.AuthNode;
import upc.c505.modular.auth.controller.param.user.GetRolePageParam;
import upc.c505.modular.auth.entity.SysRole;
import upc.c505.modular.auth.entity.SysUser;
import upc.c505.modular.auth.service.ISysRoleService;
import upc.c505.modular.auth.service.ISysUserService;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * <p>
 * 角色表 前端控制器
 * </p>
 *
 * @author sxz
 * @since 2023-07-30
 */
@RestController
@RequestMapping("/sys-role")
@Api(tags = "角色操作")
public class SysRoleController {
    @Autowired
    private ISysRoleService sysRoleService;
    @PostMapping("/addRole")
    @ApiOperation("添加角色")
    public R<String> addRole(@RequestBody @Validated SysRole sysRole) {
        sysRoleService.addRole(sysRole);
        return R.ok();
    }

    @PostMapping("/getRoles")
    @ApiOperation("获取角色列表")
    public R<PageBaseReturnParam<SysRole>> getRoles(@RequestBody GetRolePageParam param) {
        Page<SysRole> roles = sysRoleService.getRoles(param);
        PageBaseReturnParam<SysRole> p = PageBaseReturnParam.ok(roles);
        return R.page(p);
    }

    @PostMapping("/updateRoleByID")
    @ApiOperation("更改角色信息")
    public R<String> updateRoleById(@RequestBody SysRole sysRole) {
        sysRoleService.updateRoleById(sysRole);
        return R.ok("更新成功");
    }

    @PostMapping("/deleteRolesByRoleIdList")
    @ApiOperation("/根据idList批量删除角色")
    public R<String> deleteRoles(@RequestParam("IdList")
                                 @NotEmpty(message = "数组不能为空")
                                         List<Integer> idList) {
        sysRoleService.deleteRoles(idList);
        return R.ok();
    }

    @GetMapping("/getRoleAuthTree/{roleId}")
    @ApiOperation("获取角色的权限树")
    public R<List<AuthNode>> getRoleAuths(@PathVariable Long roleId) {
        return R.ok(sysRoleService.getRoleAuths(roleId));
    }

    @GetMapping("/getRoleAuthTreeTest/{roleId}")
    @ApiOperation("获取角色的权限树2")
    public R<List<AuthNode>> getRoleAuths2(@PathVariable Long roleId) {
        return R.ok(sysRoleService.getRoleAuths2(roleId));
    }

    @PostMapping("/updateRoleAuthTree/{roleId}")
    @ApiOperation("更新角色的权限树")
    public R<String> updateRoleAuthTree(@PathVariable Long roleId,
                                        @RequestParam("idList")
                                        @NotEmpty(message = "数组不能为空")
                                                List<Long> idList) {
        sysRoleService.updateRoleAuthTree(roleId, idList);
        return R.ok();
    }
}
