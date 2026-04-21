package upc.c505.modular.auth.service;


import upc.c505.modular.auth.controller.param.AuthModelParam;
import upc.c505.modular.auth.controller.param.tree.AuthModelTreeNode;
import upc.c505.modular.auth.entity.SysAuthModel;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 权限模块表 服务类
 * </p>
 *
 * @author sxz
 * @since 2023-07-30
 */
public interface ISysAuthModelService extends IService<SysAuthModel> {
    void addModel(AuthModelParam authModelParam);

    /**
     * 删除权限模块，其下的权限会一起删除
     */
    void deleteModelsByIdList(List<Integer> idList);

    /**
     * 更改权限模块信息
     */
    void updateModelById(AuthModelParam authModelParam);

    /**
     * 查询权限模块信息
     * @return 返回分页信息
     */
    List<AuthModelTreeNode> getModelPage(Long parentId);
}
