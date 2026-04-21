package upc.c505.modular.auth.service;

import upc.c505.modular.auth.entity.SysUserRole;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 用户绑定角色表 服务类
 * </p>
 *
 * @author sxz
 * @since 2023-07-30
 */
public interface ISysUserRoleService extends IService<SysUserRole> {

    /**
     * 绑定用户角色
     * @param userId 用户id
     * @param roleId 角色id
     */
    void addRoleForUser(Long userId, Long roleId);

    /**
     * 删除用户角色
     * @param userId 用户id
     * @param roleId 角色id
     */
    void deleteRoleForUser(Long userId, Long roleId);

    /**
     * 根据用户id查询角色
     * @param userId 用户id
     * @return 角色列表
     */
    List<SysUserRole> getRolesForUser(Long userId);

    /**
     * 给用户分配角色
     * @param userCode 用户id
     * @param roleIdList 角色idList
     */
    void giveRoleForUser(Long userCode, List<Long> roleIdList);
}
