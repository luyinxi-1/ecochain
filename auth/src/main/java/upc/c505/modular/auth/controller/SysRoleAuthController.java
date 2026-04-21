package upc.c505.modular.auth.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import upc.c505.common.responseparam.R;
import upc.c505.modular.auth.controller.param.user.AuthParam;
import upc.c505.modular.auth.service.ISysRoleAuthService;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * <p>
 * 角色绑定权限表 前端控制器
 * </p>
 *
 * @author sxz
 * @since 2023-07-30
 */
@RestController
@RequestMapping("/sys-role-auth")
@Api(tags = "角色权限操作")
public class SysRoleAuthController {

    @Autowired
    private ISysRoleAuthService sysRoleAuthService;

    @GetMapping("/addAuthForRole")
    @ApiOperation("给角色添加权限")
    public R<String> addAuthForRole(@RequestParam("roleId") @NotBlank(message = "角色id不能为空") Long roleId,
                                    @RequestParam("authIdList") @NotBlank(message = "权限idList不能为空") List<Integer> authIdList) {
        sysRoleAuthService.addAuthForRole(roleId, authIdList);
        return R.ok();
    }

    @PostMapping("/deleteAuthForRole")
    @ApiOperation("给角色删除权限")
    public R<String> deleteAuthForRole(@RequestParam("roleId") @NotBlank(message = "角色id不能为空") Long roleId,
                                       @RequestParam("authIdList") @NotBlank(message = "权限idList不能为空") List<Integer> authIdList) {
        sysRoleAuthService.deleteAuthForRole(roleId, authIdList);
        return R.ok();
    }

    @GetMapping("/getRoleAuthsByRoleId")
    @ApiOperation("获取角色权限")
    public R<List<AuthParam>> getRoleAuthsByRoleId(@RequestParam("roleId") @NotBlank(message = "角色id不能为空")
                                                               Long roleId) {
        List<AuthParam> roleParamList = sysRoleAuthService.getRoleAuthsByRoleId(roleId);
        return R.ok(roleParamList);
    }

}
