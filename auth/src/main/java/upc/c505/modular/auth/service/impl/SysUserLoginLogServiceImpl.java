package upc.c505.modular.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import upc.c505.common.wrapper.MyLambdaQueryWrapper;
import upc.c505.modular.auth.controller.param.UserLoginLogSearchParam;
import upc.c505.modular.auth.entity.SysUserLoginLog;
import upc.c505.modular.auth.mapper.NewSysUserLoginLogMapper;
import upc.c505.modular.auth.service.ISysUserLoginLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * <p>
 * 登录日志 服务实现类
 * </p>
 *
 * @author sxz
 * @since 2023-07-30
 */
@Service
public class SysUserLoginLogServiceImpl extends ServiceImpl<NewSysUserLoginLogMapper, SysUserLoginLog> implements ISysUserLoginLogService {

    @Autowired
    private NewSysUserLoginLogMapper sysUserLoginLogMapper;

    @Override
    public Page<SysUserLoginLog> selectPage(UserLoginLogSearchParam param) {
        Page<SysUserLoginLog> sysUserLoginLogPage = sysUserLoginLogMapper.selectPage(
                new Page<>(param.getCurrent(), param.getSize()),
                new MyLambdaQueryWrapper<SysUserLoginLog>()
                        .like(SysUserLoginLog::getUserCode, param.getUsercode())
                        .like(SysUserLoginLog::getUsername, param.getUsername())
                        .like(SysUserLoginLog::getLoginIp, param.getLoginIp())
                        .orderBy(true, Objects.equals(1, param.getIsAsc()), SysUserLoginLog::getLoginDatetime)
        );
        return sysUserLoginLogPage;
    }
}
