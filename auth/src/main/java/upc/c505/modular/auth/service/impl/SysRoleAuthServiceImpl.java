package upc.c505.modular.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import upc.c505.common.wrapper.MyLambdaQueryWrapper;

import upc.c505.modular.auth.controller.param.user.AuthParam;
import upc.c505.modular.auth.entity.SysRoleAuth;
import upc.c505.modular.auth.mapper.NewSysRoleAuthMapper;
import upc.c505.modular.auth.service.ISysRoleAuthService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 角色绑定权限表 服务实现类
 * </p>
 *
 * @author sxz
 * @since 2023-07-30
 */
@Service
public class SysRoleAuthServiceImpl extends ServiceImpl<NewSysRoleAuthMapper, SysRoleAuth> implements ISysRoleAuthService {

    @Autowired
    private NewSysRoleAuthMapper sysRoleAuthMapper;

    @Override
    public void addAuthForRole(Long roleId, List<Integer> authIdList) {
        //查出已有的权限
        List<Long> listExist = sysRoleAuthMapper.selectList(new MyLambdaQueryWrapper<SysRoleAuth>()
                .select(SysRoleAuth::getAuthId)
                .eq(SysRoleAuth::getRoleId, roleId))
                .stream()
                .map(SysRoleAuth::getAuthId)
                .collect(Collectors.toList());
        List<Long> collect = authIdList.stream().map(Integer::longValue).collect(Collectors.toList());
        //去除已有权限
        collect.removeAll(listExist);
        List<SysRoleAuth> sysRoleAuths = collect
                .stream()
                .map(item -> {
                    SysRoleAuth sysRoleAuth = new SysRoleAuth();
                    sysRoleAuth.setRoleId(roleId);
                    sysRoleAuth.setAuthId(item);
                    return sysRoleAuth;
                }).collect(Collectors.toList());
        this.saveBatch(sysRoleAuths);
    }

    @Override
    public void deleteAuthForRole(Long roleId, List<Integer> authIdList) {
        // 删除连表
        sysRoleAuthMapper.delete(
                new LambdaQueryWrapper<SysRoleAuth>()
                .eq(SysRoleAuth::getRoleId,roleId)
                .in(SysRoleAuth::getAuthId,authIdList)
        );
    }

    @Override
    public List<AuthParam> getRoleAuthsByRoleId(Long roleId) {
        List<SysRoleAuth> sysRoleAuths = sysRoleAuthMapper.selectList(
                new LambdaQueryWrapper<SysRoleAuth>()
                        .eq(SysRoleAuth::getRoleId, roleId)
        );
        List<AuthParam> roleParamList = sysRoleAuths
                .stream()
                .map(item -> {
                    AuthParam authParam = new AuthParam();
                    BeanUtils.copyProperties(item, authParam);
                    return authParam;
                })
                .collect(Collectors.toList());
        return roleParamList;
    }
}
