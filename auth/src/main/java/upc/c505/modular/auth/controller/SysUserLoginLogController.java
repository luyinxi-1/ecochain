package upc.c505.modular.auth.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import upc.c505.common.responseparam.PageBaseReturnParam;
import upc.c505.common.responseparam.R;
import upc.c505.modular.auth.controller.param.UserLoginLogSearchParam;
import upc.c505.modular.auth.entity.SysUserLoginLog;
import upc.c505.modular.auth.service.ISysUserLoginLogService;

import java.util.List;

/**
 * <p>
 * 登录日志 前端控制器
 * </p>
 *
 * @author sxz
 * @since 2023-07-30
 */
@RestController
@RequestMapping("/sys-user-login-log")
@Api(tags = {"登录日志"})
public class SysUserLoginLogController {

    @Autowired
    private ISysUserLoginLogService loginLogService;

    @PostMapping("/selectPage")
    @ApiOperation("分页查询登录日志")
    public R<PageBaseReturnParam<SysUserLoginLog>> selectPage(@RequestBody UserLoginLogSearchParam param) {
        Page<SysUserLoginLog> sysUserLoginLogPage = loginLogService.selectPage(param);
        PageBaseReturnParam<SysUserLoginLog> p = PageBaseReturnParam.ok(sysUserLoginLogPage);
        return R.page(p);
    }

    @PostMapping("/deleteByIdList")
    @ApiOperation("批量删除")
    public R<PageBaseReturnParam<SysUserLoginLog>> deleteByIdList(@RequestBody List<Long> idList) {
        loginLogService.removeBatchByIds(idList);
        return R.ok();
    }

}
