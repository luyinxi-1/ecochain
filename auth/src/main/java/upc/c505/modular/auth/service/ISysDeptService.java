package upc.c505.modular.auth.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import upc.c505.modular.auth.controller.param.DeptAreaParam;
import upc.c505.modular.auth.controller.param.DeptPageSearchParam;
import upc.c505.modular.auth.entity.SysArea;
import upc.c505.modular.auth.entity.SysDept;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 系统_部门表 服务类
 * </p>
 *
 * @author sxz
 * @since 2023-08-31
 */
public interface ISysDeptService extends IService<SysDept> {

    Integer deleteById(Long id);

    Integer deleteByIdList(List<Long> idList);

    Page<SysDept> selectPage(DeptPageSearchParam param);

    Page<SysArea> listAreaByDeptId(DeptPageSearchParam param);

    void updateAreaByDeptIdAndAreaList(DeptAreaParam deptAreaParam, List<Long> areaIdList);
}
