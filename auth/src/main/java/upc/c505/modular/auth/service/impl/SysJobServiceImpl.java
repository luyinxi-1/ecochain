package upc.c505.modular.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import upc.c505.common.wrapper.MyLambdaQueryWrapper;
import upc.c505.modular.auth.controller.param.JobPageSearchParam;
import upc.c505.modular.auth.entity.SysJob;
import upc.c505.modular.auth.entity.SysUser;
import upc.c505.modular.auth.mapper.NewSysJobMapper;
import upc.c505.modular.auth.mapper.NewSysUserMapper;
import upc.c505.modular.auth.service.ISysJobService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 职务表 服务实现类
 * </p>
 *
 * @author sxz
 * @since 2023-08-31
 */
@Service
public class SysJobServiceImpl extends ServiceImpl<NewSysJobMapper, SysJob> implements ISysJobService {

    @Autowired
    private NewSysJobMapper sysJobMapper;

    @Autowired
    private NewSysUserMapper sysUserMapper;

    @Override
    public Integer deleteById(Long id) {
        int delete = sysJobMapper.deleteById(id);
        int update = sysUserMapper.update(
                new SysUser().setJobId(null).setJobName(null),
                new LambdaUpdateWrapper<SysUser>()
                        .in(SysUser::getJobId, id)
        );
        return delete;
    }

    @Override
    public Integer deleteByIdList(List<Long> idList) {
        int deleteBatchIds = sysJobMapper.deleteBatchIds(idList);
        int update = sysUserMapper.update(
                new SysUser().setJobId(null).setJobName(null),
                new LambdaUpdateWrapper<SysUser>()
                        .in(SysUser::getJobId, idList)
        );
        return deleteBatchIds;
    }

    @Override
    public Page<SysJob> selectPage(JobPageSearchParam param) {
        Page<SysJob> sysJobPage = sysJobMapper.selectPage(
                new Page<>(param.getCurrent(), param.getSize()),
                new MyLambdaQueryWrapper<SysJob>()
                        .select(SysJob::getAreaName,SysJob::getJobName,SysJob::getIsMain,SysJob::getAddDatetime,
                                SysJob::getSeq,SysJob::getId,SysJob::getAreaId)
                        .like(SysJob::getJobName, param.getJobName())
                        .eq(SysJob::getAreaId, param.getAreaId())
                        .eq(SysJob::getIsMain, param.getIsMain())
        );
        return sysJobPage;
    }
}
