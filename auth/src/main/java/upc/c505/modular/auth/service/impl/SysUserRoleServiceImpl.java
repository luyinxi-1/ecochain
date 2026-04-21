package upc.c505.modular.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import upc.c505.common.wrapper.MyLambdaQueryWrapper;
import upc.c505.exception.BusinessErrorEnum;
import upc.c505.exception.BusinessException;
import upc.c505.modular.auth.entity.SysUserRole;
import upc.c505.modular.auth.mapper.NewSysRoleMapper;
import upc.c505.modular.auth.mapper.NewSysUserRoleMapper;
import upc.c505.modular.auth.service.ISysUserRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 用户绑定角色表 服务实现类
 * </p>
 *
 * @author sxz
 * @since 2023-07-30
 */
@Service
public class SysUserRoleServiceImpl extends ServiceImpl<NewSysUserRoleMapper, SysUserRole> implements ISysUserRoleService {

    @Autowired
    private NewSysUserRoleMapper sysUserRoleMapper;

    @Autowired
    private NewSysRoleMapper sysRoleMapper;

    @Override
    public void addRoleForUser(Long userId, Long roleId) {
        List<SysUserRole> sysUserRoles = sysUserRoleMapper.selectList(
                new LambdaQueryWrapper<SysUserRole>()
                        .eq(SysUserRole::getUserId, userId)
                        .eq(SysUserRole::getRoleId, roleId)
        );
        if (!CollectionUtils.isEmpty(sysUserRoles)) {
            throw new BusinessException(BusinessErrorEnum.PARAMETER_VALIDATION_ERROR, "用户已有该权限");
        }
        sysUserRoleMapper.insert(
                new SysUserRole()
                        .setRoleId(roleId)
                        .setUserId(userId)
        );
    }

    @Override
    public void deleteRoleForUser(Long userId, Long roleId) {
        int delete = sysUserRoleMapper.delete(new MyLambdaQueryWrapper<SysUserRole>()
                .eq(SysUserRole::getUserId, userId)
                .eq(SysUserRole::getRoleId, roleId)
        );
        if (delete == 0) {
            throw new BusinessException(BusinessErrorEnum.PARAMETER_VALIDATION_ERROR, "该用户没有此角色，删除失败");
        }
    }

    @Override
    public List<SysUserRole> getRolesForUser(Long userId) {
        List<SysUserRole> sysUserRoles = sysUserRoleMapper.selectList(
                new LambdaQueryWrapper<SysUserRole>()
                        .eq(SysUserRole::getUserId, userId)
        );
        if (CollectionUtils.isEmpty(sysUserRoles)) {
            return null;
        }
        return sysUserRoles;
    }

    @Override
    public void giveRoleForUser(Long userCode, List<Long> roleIdList) {
        // 先删除全部的
        sysUserRoleMapper.delete(new MyLambdaQueryWrapper<SysUserRole>()
                .eq(SysUserRole::getUserId, userCode));
        List<SysUserRole> sysUserRoleList = new ArrayList<>();
        roleIdList.forEach(item ->
                sysUserRoleList.add(new SysUserRole()
                        .setUserId(userCode)
                        .setRoleId(item)));
        //插入
        this.saveBatch(sysUserRoleList);
    }

}
