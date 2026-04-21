package upc.c505.modular.auth.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import upc.c505.common.UserInfoToRedis;
import upc.c505.common.UserUtils;
import upc.c505.common.responseparam.PageBaseReturnParam;
import upc.c505.common.responseparam.R;
import upc.c505.modular.auth.controller.param.UpdateUserPasswordParam;
import upc.c505.modular.auth.controller.param.UserAuthTree;
import upc.c505.modular.auth.controller.param.WechatLoginParam;
import upc.c505.modular.auth.controller.param.user.SysUserPageSearchParam;
import upc.c505.modular.auth.controller.param.user.UserLoginParam;
import upc.c505.modular.auth.entity.SysUser;
import upc.c505.modular.auth.service.ISysRoleService;
import upc.c505.modular.auth.service.ISysUserService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author sxz
 * @since 2023-07-30
 */
@RestController
@RequestMapping("/sys-user")
@Api(tags = "用户管理")
public class SysUserController {

    @Autowired
    private ISysUserService sysUserService;

    @Autowired
    private ISysRoleService sysRoleService;

    @Value("${spring.application.name}")
    String activeModel;

    @PostMapping("/insertUser")
    @ApiOperation("新增用户")
    public R<Long> insertUser(@RequestBody SysUser sysUser) {
        return R.ok(sysUserService.insertUser(sysUser));
    }

    @PostMapping("/updateUserByCode")
    @ApiOperation("更新用户资料")
    public R<String> updateUserByCode(@RequestBody SysUser sysUser) {
        sysUserService.updateUserByCode(sysUser);
        return R.ok();
    }

    @GetMapping("/deleteUserById")
    @ApiOperation("根据id删除用户")
    public R<Integer> deleteUserById(@RequestParam Long id) {
        Integer integer = sysUserService.deleteUserById(id);
        return R.ok(integer);
    }

    @GetMapping("/deleteUserByIdList")
    @ApiOperation("根据idList批量删除用户")
    public R<Integer> deleteUserByIdList(@RequestParam List<Long> idList) {
        Integer integer = sysUserService.deleteUserByIdList(idList);
        return R.ok(integer);
    }

    @PostMapping("/selectUserPage")
    @ApiOperation("分页查询用户列表")
    public R<PageBaseReturnParam<SysUser>> updateUserByCode(@RequestBody SysUserPageSearchParam param) {
        Page<SysUser> sysUserPage = sysUserService.selectPage(param);
        PageBaseReturnParam<SysUser> p = PageBaseReturnParam.ok(sysUserPage);
        return R.ok(p);
    }

    @PostMapping("/login")
    @ApiOperation("登录")
    public R<String> login(@RequestBody @Validated UserLoginParam userLogin, HttpServletRequest request) {
        return R.ok(sysUserService.login(userLogin, request));
    }

    @ApiOperation("获取openid")
    @GetMapping("/getUserOpenId")
    public R<String> getWeChatId(@RequestParam String code, @RequestParam(required = false) String appId) {
        return R.ok(sysUserService.getUserOpenId(code, appId));
    }

    // 如果需要微信用户的隐私数据则用这个接口
    // @ApiOperation("openid绑定自定义登录态")
    // @PostMapping("/authLogin")
    // public R authLogin(@RequestBody WechatLoginParam wechatLoginParam) {
    // return sysUserService.authLogin(wechatLoginParam);
    // }
    @ApiOperation("小程序登录")
    @PostMapping("/wechatLogin")
    public R<UserInfoToRedis> authLogin(@RequestBody WechatLoginParam wechatLoginParam, HttpServletRequest request) {
        return R.ok(sysUserService.wechatLogin(wechatLoginParam, request));
    }

    @PostMapping("/logout")
    @ApiOperation("退出登录")
    public R<String> logout(HttpServletRequest httpServletRequest) {
        sysUserService.logout(httpServletRequest);
        return R.ok();
    }

    @GetMapping("/getUserInfo")
    @ApiOperation("获取当前登录用户信息")
    public R<UserInfoToRedis> getUserInfo() {
        return R.ok(UserUtils.get());
    }

    @PostMapping("/updateMyPassword")
    @ApiOperation("修改用户自己的密码")
    public R<String> updateMyPassword(@RequestBody UpdateUserPasswordParam updateUserPasswordParam) {
        sysUserService.updateMyPassword(updateUserPasswordParam);
        return R.ok("修改成功");
    }

    @GetMapping("/getUserAuthTree")
    @ApiOperation("获取用户权限")
    public R<UserAuthTree> getUserAuthTree() {
        return R.ok(sysRoleService.getUserAuthTree());
    }

    @GetMapping("/getRemarkByUserCode")
    @ApiOperation("根据登录账号查询")
    public R<String> getRemarkByUserCode(String userCode) {
        String remarkByUserCode = sysUserService.getRemarkByUserCode(userCode);
        return R.ok(remarkByUserCode);
    }
}
