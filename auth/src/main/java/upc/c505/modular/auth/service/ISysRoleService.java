package upc.c505.modular.auth.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.PathVariable;
import upc.c505.modular.auth.controller.param.UserAuthTree;
import upc.c505.modular.auth.controller.param.tree.AuthNode;
import upc.c505.modular.auth.controller.param.user.GetRolePageParam;
import upc.c505.modular.auth.entity.SysRole;
import com.baomidou.mybatisplus.extension.service.IService;
import upc.c505.modular.auth.entity.SysUser;

import java.util.List;

/**
 * <p>
 * 角色表 服务类
 * </p>
 *
 * @author sxz
 * @since 2023-07-30
 */
public interface ISysRoleService extends IService<SysRole> {

    /**
     * 添加新角色
     * @param sysRole
     */
    void addRole(SysRole sysRole);

    /**
     * 查询角色列表
     */
    Page<SysRole> getRoles(GetRolePageParam getRolePageParam);

    /**
     * 根据id更新角色信息
     */
    void updateRoleById(SysRole sysRole);

    /**
     * 删除角色
     * @param idList
     */
    void deleteRoles(List<Integer> idList);

    /**
     * 获取角色的功能权限
     */
    List<AuthNode> getRoleAuths(Long roleId);

    List<AuthNode> getRoleAuths2(@PathVariable Long roleId);

    /**
     * 更新角色权限树
     */
    void updateRoleAuthTree(Long roleId, List<Long> idList);

    /**
     * 查询登录用户的所有权限信息
     * @return
     */
    UserAuthTree getUserAuthTree();
}
