package upc.c505.modular.auth.service;

import upc.c505.common.responseparam.PageBaseReturnParam;
import upc.c505.modular.auth.controller.param.AreaParam;
import upc.c505.modular.auth.controller.param.GetAreaPageParam;
import upc.c505.modular.auth.controller.param.tree.AreaTreeNode;
import upc.c505.modular.auth.entity.SysArea;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 区域表 服务类
 * </p>
 *
 * @author sxz
 * @since 2023-07-30
 */
public interface ISysAreaService extends IService<SysArea> {

    void addArea(AreaParam areaParam);

    void deleteAreasByIdList(List<Integer> idList);

    void updateAreaById(AreaParam areaParam);

    PageBaseReturnParam<AreaTreeNode> getAreaPage(GetAreaPageParam param);

    /**
     * 获取传入id的子区域idList
     * @param id sysArea表的id
     * @return 子区域及本体的idList
     */
    List<Long> getChildAreaIdList(Long id);

    /**
     * 获取该区域节点下所有子区域节点
     */
    AreaTreeNode getAreaTreeNode(Long areaId);

    /**
     * 查找某区域根节点到它的某个子节点的路径
     */
    List<Long> getElders(Long searchId);
}
