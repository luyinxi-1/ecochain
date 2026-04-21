package upc.c505.modular.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import upc.c505.common.wrapper.MyLambdaQueryWrapper;
import upc.c505.common.wrapper.MyQueryWrapper;
import upc.c505.modular.auth.controller.param.DeptAreaParam;
import upc.c505.modular.auth.controller.param.DeptPageSearchParam;
import upc.c505.modular.auth.entity.SysArea;
import upc.c505.modular.auth.entity.SysDept;
import upc.c505.modular.auth.entity.SysDeptArea;
import upc.c505.modular.auth.entity.SysUser;
import upc.c505.modular.auth.mapper.NewSysAreaMapper;
import upc.c505.modular.auth.mapper.NewSysDeptMapper;
import upc.c505.modular.auth.mapper.NewSysUserMapper;
import upc.c505.modular.auth.mapper.SysDeptAreaMapper;
import upc.c505.modular.auth.service.ISysDeptService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 系统_部门表 服务实现类
 * </p>
 *
 * @author sxz
 * @since 2023-08-31
 */
@Service
public class SysDeptServiceImpl extends ServiceImpl<NewSysDeptMapper, SysDept> implements ISysDeptService {

    @Autowired
    private NewSysDeptMapper sysDeptMapper;

    @Autowired
    private NewSysAreaMapper sysAreaMapper;

    @Autowired
    private SysDeptAreaMapper sysDeptAreaMapper;

    @Autowired
    private NewSysUserMapper sysUserMapper;

    @Override
    public Integer deleteById(Long id) {
        int delete = sysDeptMapper.deleteById(id);
        sysUserMapper.update(
                new SysUser().setDeptId(null).setDeptName(null),
                new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getDeptId,id)
        );
        return delete;
    }

    @Override
    public Integer deleteByIdList(List<Long> idList) {
        int deleteBatchIds = sysDeptMapper.deleteBatchIds(idList);
        sysUserMapper.update(
                new SysUser().setDeptId(null).setDeptName(null),
                new LambdaQueryWrapper<SysUser>()
                        .in(SysUser::getDeptId,idList)
        );
        return deleteBatchIds;
    }

    @Override
    public Page<SysDept> selectPage(DeptPageSearchParam param) {
        Page<SysDept> sysDeptPage = sysDeptMapper.selectPage(
                new Page<>(param.getCurrent(), param.getSize()),
                new MyLambdaQueryWrapper<SysDept>()
                        .eq(SysDept::getAreaId, param.getAreaId())
                        .eq(SysDept::getId,param.getId())
                        .like(SysDept::getDeptName, param.getDeptName())
                        .like(SysDept::getDeptType, param.getDeptType())
        );
        return sysDeptPage;
    }

    @Override
    public Page<SysArea> listAreaByDeptId(DeptPageSearchParam param) {
        List<SysDeptArea> sysDeptAreaList = sysDeptAreaMapper.selectList(
                new LambdaQueryWrapper<SysDeptArea>()
                        .eq(SysDeptArea::getDeptId, param.getId())
        );
        // 获取areaIdList
        List<Long> areaIdList = new ArrayList<>();
        for (SysDeptArea sysDeptArea : sysDeptAreaList){
            areaIdList.add(sysDeptArea.getAreaId());
        }
        areaIdList.add(0L);
        // 获取areaList
        Page<SysArea> sysAreaList = sysAreaMapper.selectPage(
                new Page<>(param.getCurrent(), param.getSize()),
                new LambdaQueryWrapper<SysArea>()
                        .in(SysArea::getId, areaIdList)
        );
        return sysAreaList;
    }

    @Override
    public void updateAreaByDeptIdAndAreaList(DeptAreaParam deptAreaParam, List<Long> areaIdList) {
        // 先删除原先的关联表中的关联关系
        sysDeptAreaMapper.delete(
                new MyLambdaQueryWrapper<SysDeptArea>()
                        .eq(SysDeptArea::getDeptId, deptAreaParam.getDeptId())
        );
        // 插入到数据库中
        for (Long areaId : areaIdList){
            SysDeptArea sysDeptArea = new SysDeptArea();
            sysDeptArea.setAreaId(areaId);
            sysDeptArea.setDeptId(deptAreaParam.getDeptId());
            sysDeptAreaMapper.insert(sysDeptArea);
        }
    }
}
