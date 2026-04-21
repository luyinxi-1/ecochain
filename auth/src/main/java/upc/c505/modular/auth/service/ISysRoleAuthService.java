package upc.c505.modular.auth.service;

import upc.c505.modular.auth.controller.param.user.AuthParam;
import upc.c505.modular.auth.entity.SysRoleAuth;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 角色绑定权限表 服务类
 * </p>
 *
 * @author sxz
 * @since 2023-07-30
 */
public interface ISysRoleAuthService extends IService<SysRoleAuth> {
    /**
     * 给角色添加权限，一对多
     * @param roleId 角色id
     * @param authIdList 权限列表id
     */
    void addAuthForRole(Long roleId, List<Integer> authIdList);

    /**
     * 给角色删除权限，一对多
     * @param roleId 角色id
     * @param authIdList 权限列表id
     */
    void deleteAuthForRole(Long roleId, List<Integer> authIdList);

    /**
     * 获取角色权限列表
     * @param roleId 角色id
     * @return 权限列表
     */
    List<AuthParam> getRoleAuthsByRoleId(Long roleId);

}
