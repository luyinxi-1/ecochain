package upc.c505.modular.auth.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import upc.c505.common.responseparam.R;
import upc.c505.modular.auth.entity.SysUserRole;
import upc.c505.modular.auth.service.ISysUserRoleService;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * <p>
 * 用户绑定角色表 前端控制器
 * </p>
 *
 * @author sxz
 * @since 2023-07-30
 */
@RestController
@RequestMapping("/sys-user-role")
@Api(tags = "用户角色操作")
public class SysUserRoleController {
    @Autowired
    private ISysUserRoleService sysUserRoleService;


    @PostMapping("/addRoleForUser")
    @ApiOperation("给用户添加角色")
    public R<String> addRoleForUser(@RequestParam("userId") @NotBlank(message = "用户id不能为空") Long userId,
                                    @RequestParam("roleId") @NotBlank(message = "角色id不能为空") Long roleId) {
        sysUserRoleService.addRoleForUser(userId, roleId);
        return R.ok();
    }

    @PostMapping("/deleteRoleForUser")
    @ApiOperation("给用户删除角色")
    public R<String> deleteRoleForUser(@RequestParam("userId") @NotBlank(message = "用户id不能为空") Long userId,
                                       @RequestParam("roleId") @NotBlank(message = "角色id不能为空") Long roleId) {
        sysUserRoleService.deleteRoleForUser(userId, roleId);
        return R.ok();
    }

    @PostMapping("/giveRoleForUser")
    @ApiOperation("给用户分配角色")
    public R<String> giveRoleForUser(@RequestParam("userId") @NotBlank(message = "用户id不能为空") Long userId,
                                     @RequestParam("roleId") @NotBlank(message = "角色id不能为空") List<Long> roleIdList) {
        sysUserRoleService.giveRoleForUser(userId, roleIdList);
        return R.ok();
    }


    @PostMapping("/getRolesForUser")
    @ApiOperation("查询用户的角色")
    public R<List<SysUserRole>> getRolesForUser(@RequestParam("userId") @NotBlank(message = "用户id不能为空") Long userId) {
        return R.ok(sysUserRoleService.getRolesForUser(userId));
    }
}
