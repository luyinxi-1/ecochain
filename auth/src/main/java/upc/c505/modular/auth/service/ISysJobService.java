package upc.c505.modular.auth.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import upc.c505.modular.auth.controller.param.JobPageSearchParam;
import upc.c505.modular.auth.entity.SysJob;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 职务表 服务类
 * </p>
 *
 * @author sxz
 * @since 2023-08-31
 */
public interface ISysJobService extends IService<SysJob> {

    Integer deleteById(Long id);

    Integer deleteByIdList(List<Long> idList);

    Page<SysJob> selectPage(JobPageSearchParam param);
}
