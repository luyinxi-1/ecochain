package upc.c505.modular.auth.service;

import upc.c505.common.responseparam.PageBaseReturnParam;

import upc.c505.modular.auth.controller.param.user.AuthParam;
import upc.c505.modular.auth.controller.param.user.GetAuthPageParam;
import upc.c505.modular.auth.entity.SysAuth;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 权限表 服务类
 * </p>
 *
 * @author sxz
 * @since 2023-07-30
 */
public interface ISysAuthService extends IService<SysAuth> {

    /**
     * 添加权限
     * @param authParam 权限参数
     */
    void addAuth(AuthParam authParam);

    /**
     * 获取权限列表
     * @return 返回的分页
     */
    PageBaseReturnParam<SysAuth> getAuths(GetAuthPageParam getAuthPageParam);

    /**
     * 删除权限
     * @param idList 权限的idList
     */
    void deleteAuths(List<Integer> idList);

    /**
     * 根据id更新权限信息
     * @param authParam 更新的信息
     */
    void updateByAuthId(AuthParam authParam);
}
