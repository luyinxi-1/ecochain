package upc.c505.modular.auth.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import upc.c505.modular.auth.controller.param.UserLoginLogSearchParam;
import upc.c505.modular.auth.entity.SysUserLoginLog;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 登录日志 服务类
 * </p>
 *
 * @author sxz
 * @since 2023-07-30
 */
public interface ISysUserLoginLogService extends IService<SysUserLoginLog> {

    Page<SysUserLoginLog> selectPage(UserLoginLogSearchParam param);
}
