package upc.c505.modular.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import upc.c505.common.responseparam.PageBaseReturnParam;
import upc.c505.common.wrapper.MyLambdaQueryWrapper;
import upc.c505.exception.BusinessErrorEnum;
import upc.c505.exception.BusinessException;
import upc.c505.modular.auth.controller.param.AreaParam;
import upc.c505.modular.auth.controller.param.GetAreaPageParam;
import upc.c505.modular.auth.controller.param.tree.AreaTreeIteratorNode;
import upc.c505.modular.auth.controller.param.tree.AreaTreeNode;
import upc.c505.modular.auth.entity.SysArea;
import upc.c505.modular.auth.mapper.NewSysAreaMapper;
import upc.c505.modular.auth.service.ISysAreaService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import upc.c505.utils.MyBeanUtils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>
 * 区域表 服务实现类
 * </p>
 *
 * @author sxz
 * @since 2023-07-30
 */
@Service
public class SysAreaServiceImpl extends ServiceImpl<NewSysAreaMapper, SysArea> implements ISysAreaService {

    @Autowired
    private NewSysAreaMapper sysAreaMapper;

    @Autowired
    private ISysAreaService sysAreaService;

    @Override
    public void addArea(AreaParam areaParam) {
        SysArea sysArea = new SysArea();
        BeanUtils.copyProperties(areaParam,sysArea);
        sysArea.setId(null);
        sysAreaMapper.insert(sysArea);
    }

    @Override
    @Transactional(rollbackFor = SQLException.class)
    public void deleteAreasByIdList(List<Integer> idList) {
        sysAreaMapper.deleteBatchIds(idList);
    }

    @Override
    public void updateAreaById(AreaParam areaParam) {
        if(areaParam.getId() == null){
            throw new BusinessException(BusinessErrorEnum.PARAMETER_VALIDATION_ERROR, ":更新时id不能为空");
        }
        SysArea sysArea = new SysArea();
        BeanUtils.copyProperties(areaParam,sysArea);
        sysAreaMapper.updateById(sysArea);
    }

    @Override
    public PageBaseReturnParam<AreaTreeNode> getAreaPage(GetAreaPageParam param) {
        List<Long> childAreaIdList = sysAreaService.getChildAreaIdList(param.getAreaId());
        Page<SysArea> page = sysAreaMapper.selectPage(
                new Page<SysArea>(param.getCurrent(),param.getSize()),
                new MyLambdaQueryWrapper<SysArea>()
                .in(SysArea::getId, childAreaIdList)
                .like(SysArea::getAreaName,param.getAreaName())
                .eq(SysArea::getParentId,param.getParentId())
                .eq(SysArea::getSeq,param.getSeq())
                .eq(SysArea::getStatus,param.getStatus())
                .between(SysArea::getAddDatetime,param.getStartTime(),param.getEndTime())
        );
        // 取出当前页的数据
        List<SysArea> records = page.getRecords();
        // 获取全部地区
        List<SysArea> allSysAreas = sysAreaMapper.selectList(
                new LambdaQueryWrapper<SysArea>()
                        .orderBy(true, true, SysArea::getSeq)
        );
        List<AreaTreeNode> areaTreeNodeList = records.stream().map(item->{
            AreaTreeNode areaTreeNode = MyBeanUtils.copy(item, new AreaTreeNode());
            areaTreeNode.getChildren(allSysAreas);
            return areaTreeNode;
        }).collect(Collectors.toList());
        Page<AreaTreeNode> returnPage = new Page<>();
        returnPage.setCurrent(page.getCurrent())
                .setSize(page.getSize())
                .setRecords(areaTreeNodeList)
                .setTotal(page.getTotal());

        return PageBaseReturnParam.ok(returnPage);
    }

    @Override
    public List<Long> getChildAreaIdList(Long id) {
        return sysAreaMapper.getChildAreaIdList(id);
    }

    @Override
    public AreaTreeNode getAreaTreeNode(Long areaId) {
        SysArea sysArea = sysAreaMapper.selectById(areaId);
        List<SysArea> allSysAreas = sysAreaMapper.selectList(
                new LambdaQueryWrapper<SysArea>()
                        .orderBy(true, true, SysArea::getSeq)
        );
        AreaTreeNode areaTreeNode = MyBeanUtils.copy(sysArea, new AreaTreeNode());
        areaTreeNode.getChildren(allSysAreas);
        return areaTreeNode;
    }

    @Override
    public List<Long> getElders(Long searchId) {
        AreaTreeNode areaTreeNode = sysAreaService.getAreaTreeNode(searchId);
        List<Long> elderList = new ArrayList<>();
        if (areaTreeNode.getParentId() == 0){
            return elderList;
        }
        AreaTreeNode parentNode = sysAreaService.getAreaTreeNode(areaTreeNode.getParentId());
        if (parentNode == null) {
                 return elderList;
            } else {
                elderList.add(parentNode.getId());
                elderList.addAll(getElders(parentNode.getId()));
                return elderList;
            }
    }
}
