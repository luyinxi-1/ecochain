package upc.c505.modular.auth.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import upc.c505.common.responseparam.PageBaseReturnParam;
import upc.c505.common.responseparam.R;

import upc.c505.modular.auth.controller.param.user.AuthParam;
import upc.c505.modular.auth.controller.param.user.GetAuthPageParam;
import upc.c505.modular.auth.entity.SysAuth;
import upc.c505.modular.auth.service.ISysAuthService;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * <p>
 * 权限表 前端控制器
 * </p>
 *
 * @author sxz
 * @since 2023-07-30
 */
@RestController
@RequestMapping("/sys-auth")
@Api(tags = "权限操作")
public class SysAuthController {
    @Autowired
    private ISysAuthService sysAuthService;

    @PostMapping("/addAuth")
    @ApiOperation("添加权限")
    public R<String> addAuth(@RequestBody @Validated AuthParam authParam) {
        sysAuthService.addAuth(authParam);
        return R.ok();
    }

    @DeleteMapping("/deleteAuthsByAuthIdList")
    @ApiOperation("/删除权限")
    public R<String> deleteAuths(@RequestParam("idList")
                                 @NotEmpty(message = "数组不能为空")
                                         List<Integer> idList) {
        sysAuthService.deleteAuths(idList);
        return R.ok("删除成功");
    }


    @PostMapping("/getAuths")
    @ApiOperation("获取权限列表")
    public R<PageBaseReturnParam<SysAuth>> getAuths(@RequestBody GetAuthPageParam getAuthPageParam) {
        return R.page(sysAuthService.getAuths(getAuthPageParam));
    }

    @PostMapping("/updateAuthById")
    @ApiOperation("更改权限信息")
    public R<String> updateByAuthId(@RequestBody AuthParam authParam) {
        sysAuthService.updateByAuthId(authParam);
        return R.ok("更新成功");
    }
}
