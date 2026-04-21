package upc.c505.modular.ecochain.service.impl;

import cn.hutool.core.lang.Dict;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import upc.c505.common.UserUtils;
import upc.c505.common.wrapper.MyLambdaQueryWrapper;
import upc.c505.exception.BusinessErrorEnum;
import upc.c505.exception.BusinessException;
import upc.c505.modular.auth.service.ISysAreaService;
import upc.c505.modular.ecochain.controller.param.EcoChainProductClassificationSearchParam;
import upc.c505.modular.ecochain.controller.param.EcoChainTopLevelProductClassificationSearchParam;
import upc.c505.modular.ecochain.entity.EcoChainProductClassification;
import upc.c505.modular.ecochain.entity.EcoChainProductTag;
import upc.c505.modular.ecochain.mapper.EcoChainProductClassificationMapper;
import upc.c505.modular.ecochain.service.IEcoChainProductClassificationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import upc.c505.modular.ecochain.util.GetUserInfo;
import upc.c505.modular.people.service.IPeopleGovernmentService;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author byh
 * @since 2024-09-25
 */
@Service
public class EcoChainProductClassificationServiceImpl
        extends ServiceImpl<EcoChainProductClassificationMapper, EcoChainProductClassification>
        implements IEcoChainProductClassificationService {

    @Autowired
    private GetUserInfo getUserInfo;

    @Autowired
    private EcoChainProductClassificationMapper ecoChainProductClassificationMapper;

    @Autowired
    private ISysAreaService sysAreaService;

    @Autowired
    private IPeopleGovernmentService peopleGovernmentService;

    @Override
    public void insertProductClassification(EcoChainProductClassification param) {
        if (ObjectUtils.isEmpty(param)) {
            throw new BusinessException(BusinessErrorEnum.PARAMETER_VALIDATION_ERROR, "前端传参为空");
        }
        if (getUserInfo.getSocialCreditCode() != null && getUserInfo.getEnterpriseName() != null) {
            param.setSocialCreditCode(getUserInfo.getSocialCreditCode());
            param.setEnterpriseName(getUserInfo.getEnterpriseName());
            if (ObjectUtils.isEmpty(param.getAreaId()) && ObjectUtils.isNotEmpty(UserUtils.get().getAreaId())) {
                param.setAreaId(UserUtils.get().getAreaId());
            }
            if (ObjectUtils.isEmpty(param.getParentId())) {
                param.setParentId(0L);
                param.setClassificationGrade(1);
            } else {
                EcoChainProductClassification ecoChainProductClassification = ecoChainProductClassificationMapper
                        .selectById(param.getParentId());
                if (ObjectUtils.isNotEmpty(ecoChainProductClassification)
                        && ecoChainProductClassification.getClassificationGrade() != 3) {
                    if (ecoChainProductClassification.getClassificationGrade() == 1) {
                        param.setClassificationGrade(2);
                    } else {
                        param.setClassificationGrade(3);
                    }
                } else {
                    throw new BusinessException(BusinessErrorEnum.PARAMETER_VALIDATION_ERROR, "父id不存在或不能在三级分类下再插入子分类");
                }
            }
            Integer sortNumber = ecoChainProductClassificationMapper.selectMaxSortNumber(param);
            if (ObjectUtils.isEmpty(param.getSortNumber())) {
                if (ObjectUtils.isNotEmpty(sortNumber)) {
                    sortNumber = sortNumber + 1;
                } else {
                    sortNumber = 1;
                }
                param.setSortNumber(sortNumber);
            }
            this.save(param);
        } else {
            throw new BusinessException(BusinessErrorEnum.PARAMETER_VALIDATION_ERROR, "未查询到社会信用代码");
        }
    }

    @Override
    public boolean updateProductClassification(EcoChainProductClassification param) {
        if (ObjectUtils.isEmpty(param.getOperator()) && ObjectUtils.isNotEmpty(UserUtils.get().getUsername())) {
            param.setOperator(UserUtils.get().getUsername());
        }
        if (ObjectUtils.isEmpty(param.getOperationDatetime())) {
            param.setOperationDatetime(LocalDateTime.now());
        }
        return ecoChainProductClassificationMapper.updateProductClassification(param);
    }

    @Override
    public List<EcoChainProductClassification> selectProductClassificationParentIdList(Integer classificationGrade) {
        if (ObjectUtils.isNotEmpty(classificationGrade)) {
            MyLambdaQueryWrapper<EcoChainProductClassification> lambdaQueryWrapper = new MyLambdaQueryWrapper<>();
            // 假如为3级分类，则获取了所有的一二级分类
            lambdaQueryWrapper.lt(EcoChainProductClassification::getClassificationGrade, classificationGrade);
            lambdaQueryWrapper.eq(ObjectUtils.isNotEmpty(getUserInfo.getSocialCreditCode()),
                    EcoChainProductClassification::getSocialCreditCode, getUserInfo.getSocialCreditCode());
            List<EcoChainProductClassification> list = ecoChainProductClassificationMapper
                    .selectList(lambdaQueryWrapper);
            if (ObjectUtils.isNotEmpty(list)) {
                return list;
            }
        }

        return Collections.emptyList();
    }

    @Override
    public void removeProductClassification(List<Long> idList) {
        for (Long id : idList) {
            // 查询一级分类下的所有二级分类
            MyLambdaQueryWrapper<EcoChainProductClassification> lambdaQueryWrapper = new MyLambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(EcoChainProductClassification::getParentId, id);
            List<EcoChainProductClassification> secondLevelList = ecoChainProductClassificationMapper
                    .selectList(lambdaQueryWrapper);

            if (ObjectUtils.isNotEmpty(secondLevelList)) {
                // 遍历二级分类
                for (EcoChainProductClassification secondLevel : secondLevelList) {
                    // 查询二级分类下的所有三级分类
                    MyLambdaQueryWrapper<EcoChainProductClassification> lambdaQueryWrapper1 = new MyLambdaQueryWrapper<>();
                    lambdaQueryWrapper1.eq(EcoChainProductClassification::getParentId, secondLevel.getId());
                    List<EcoChainProductClassification> thirdLevelList = ecoChainProductClassificationMapper
                            .selectList(lambdaQueryWrapper1);
                    if (ObjectUtils.isNotEmpty(thirdLevelList)) {
                        // 删除所有三级分类
                        for (EcoChainProductClassification thirdLevel : thirdLevelList) {
                            ecoChainProductClassificationMapper.deleteById(thirdLevel.getId());
                        }
                    }
                    // 删除二级分类
                    ecoChainProductClassificationMapper.deleteById(secondLevel.getId());
                }
            }
            // 删除一级分类
            ecoChainProductClassificationMapper.deleteById(id);
        }
    }

    @Override
    public List<EcoChainProductClassification> selectProductClassificationDownList(Long id) {
        if (ObjectUtils.isNotEmpty(id)) {
            MyLambdaQueryWrapper<EcoChainProductClassification> lambdaQueryWrapper = new MyLambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(EcoChainProductClassification::getParentId, id);
            lambdaQueryWrapper.eq(ObjectUtils.isNotEmpty(getUserInfo.getSocialCreditCode()),
                    EcoChainProductClassification::getSocialCreditCode, getUserInfo.getSocialCreditCode());
            List<EcoChainProductClassification> list = ecoChainProductClassificationMapper
                    .selectList(lambdaQueryWrapper);
            if (ObjectUtils.isNotEmpty(list)) {
                return list;
            }
        }
        return Collections.emptyList();
    }

    @Override
    public List<EcoChainProductClassification> selectProductClassificationList(
            EcoChainProductClassificationSearchParam param) {
        // 一、超级管理员用户登录，查询全部
        if ("-1".equals(getUserInfo.getUserType())) {
            MyLambdaQueryWrapper<EcoChainProductClassification> lambdaQueryWrapper = new MyLambdaQueryWrapper<>();

            lambdaQueryWrapper.like(ObjectUtils.isNotEmpty(param.getProductClassificationName()),
                    EcoChainProductClassification::getProductClassificationName, param.getProductClassificationName());
            lambdaQueryWrapper.eq(ObjectUtils.isNotEmpty(param.getType()), EcoChainProductClassification::getType,
                    param.getType());
            lambdaQueryWrapper.orderBy(true, Objects.equals(param.getIsAsc(), 1),
                    EcoChainProductClassification::getSortNumber);
            if (ObjectUtils.isNotEmpty(param.getSocialCreditCode())
                    && ObjectUtils.isNotNull(param.getSocialCreditCode())) {
                lambdaQueryWrapper.eq(EcoChainProductClassification::getSocialCreditCode, param.getSocialCreditCode());
            }
            return ecoChainProductClassificationMapper.selectList(lambdaQueryWrapper);
        }

        // 二、政府人员或二级管理员用户登录，根据管辖区域查询全部
        if ("0".equals(getUserInfo.getUserType())) {
            MyLambdaQueryWrapper<EcoChainProductClassification> lambdaQueryWrapper = new MyLambdaQueryWrapper<>();

            /**
             * 管辖区域
             * 1、如果传入areaId不为空：判断传入的areaId是不是在当前用户的管辖区域内，
             * 如果传入的areaId合法，当flag为0（或者flag为空）时只查数据库中和传入areaId相等的数据，
             * 当flag为1时先查当前用户的管辖区域，然后再查sys_area表里面传入areaId的子区域，
             * 最后把两者的交集里面的areaId拿出来，到数据库里面查areaId在交集里面的数据。
             * 2、如果传入的areaId为空，那就查询该用户管辖区域的数据。
             */
            if (ObjectUtils.isNotEmpty(param.getIsApplicableArea()) && param.getIsApplicableArea() == 0) {
                // 获取当前用户的管辖区域列表
                List<Long> list = peopleGovernmentService.getManageAreaIdList();
                if (ObjectUtils.isNotEmpty(param.getAreaId())) {
                    // 如果传入的areaId不合法，直接返回空的页面
                    if (!list.contains(param.getAreaId())) {
                        return Collections.emptyList();
                    }
                    // 当传入的areaId合法时
                    // 当flag为0（或者flag为空）时只查数据库中和传入areaId相等的数据
                    if (ObjectUtils.isEmpty(param.getFlag()) || param.getFlag() == 0) {
                        lambdaQueryWrapper.eq(EcoChainProductClassification::getAreaId, param.getAreaId());
                    }
                    // 当flag为1时先查当前用户的管辖区域，然后再查sys_area表里面传入areaId的子区域，
                    // 最后把两者的交集里面的areaId拿出来，到数据库里面查areaId在交集里面的数据。
                    if (ObjectUtils.isNotEmpty(param.getFlag()) && param.getFlag() == 1) {
                        List<Long> areaIdList = sysAreaService.getChildAreaIdList(param.getAreaId());
                        list.retainAll(areaIdList);
                        lambdaQueryWrapper.in(EcoChainProductClassification::getAreaId, list);
                    }
                } else {
                    lambdaQueryWrapper.in(EcoChainProductClassification::getAreaId, list);
                }
            }

            lambdaQueryWrapper.like(ObjectUtils.isNotEmpty(param.getProductClassificationName()),
                    EcoChainProductClassification::getProductClassificationName, param.getProductClassificationName());
            lambdaQueryWrapper.eq(ObjectUtils.isNotEmpty(param.getType()), EcoChainProductClassification::getType,
                    param.getType());
            lambdaQueryWrapper.orderBy(true, Objects.equals(param.getIsAsc(), 1),
                    EcoChainProductClassification::getSortNumber);
            if (ObjectUtils.isNotEmpty(param.getSocialCreditCode())
                    && ObjectUtils.isNotNull(param.getSocialCreditCode())) {
                lambdaQueryWrapper.eq(EcoChainProductClassification::getSocialCreditCode, param.getSocialCreditCode());
            }
            return ecoChainProductClassificationMapper.selectList(lambdaQueryWrapper);
        }

        // 三、本企业人员登录
        if ("1".equals(getUserInfo.getUserType())) {
            MyLambdaQueryWrapper<EcoChainProductClassification> lambdaQueryWrapper = new MyLambdaQueryWrapper<>();

            lambdaQueryWrapper.like(ObjectUtils.isNotEmpty(param.getProductClassificationName()),
                    EcoChainProductClassification::getProductClassificationName, param.getProductClassificationName());
            lambdaQueryWrapper.eq(ObjectUtils.isNotEmpty(param.getType()), EcoChainProductClassification::getType,
                    param.getType());

            if (ObjectUtils.isNotEmpty(param.getSocialCreditCode())) {
                lambdaQueryWrapper.eq(EcoChainProductClassification::getSocialCreditCode, param.getSocialCreditCode());
            } else {
                lambdaQueryWrapper.eq(ObjectUtils.isNotEmpty(getUserInfo.getSocialCreditCode()),
                        EcoChainProductClassification::getSocialCreditCode, getUserInfo.getSocialCreditCode());
            }

            lambdaQueryWrapper.orderBy(true, Objects.equals(param.getIsAsc(), 1),
                    EcoChainProductClassification::getSortNumber);

            return ecoChainProductClassificationMapper.selectList(lambdaQueryWrapper);
        }
        return Collections.emptyList();
    }

    @Override
    public List<EcoChainProductClassification> buildDictTree(List<EcoChainProductClassification> list) {
        // 将分类按照父ID分组
        Map<Long, List<EcoChainProductClassification>> map = list.stream()
                .collect(Collectors.groupingBy(EcoChainProductClassification::getParentId));

        // 为每个分类设置子分类
        list.forEach(item -> item.setChildren(map.get(item.getId())));

        // 找到所有根节点（假设根节点的父ID为0）
        List<EcoChainProductClassification> roots = list.stream()
                .filter(item -> item.getParentId() == 0)
                .collect(Collectors.toList()); // 将所有根节点收集到一个列表中

        return roots;
    }

    @Override
    public boolean updateProductClassificationSortName(Long id, Integer param) {
        EcoChainProductClassification currentTag = ecoChainProductClassificationMapper.selectById(id);
        // 根据param决定是向上还是向下调整
        boolean isNext = param != 0;
        EcoChainProductClassification adjacentTag = getAdjacentClassifiaction(currentTag, isNext);
        if (adjacentTag == null) {
            return false;
        }
        // 交换sortNumber
        Integer tempSortNumber = currentTag.getSortNumber();
        currentTag.setSortNumber(adjacentTag.getSortNumber());
        adjacentTag.setSortNumber(tempSortNumber);
        // 更新数据库
        ecoChainProductClassificationMapper.updateById(currentTag);
        ecoChainProductClassificationMapper.updateById(adjacentTag);
        return true;
    }

    @Override
    public List<EcoChainProductClassification> selectTopLevelProductClassification(
            EcoChainTopLevelProductClassificationSearchParam param) {
        MyLambdaQueryWrapper<EcoChainProductClassification> lambdaQueryWrapper = new MyLambdaQueryWrapper<>();

        if ("-1".equals(getUserInfo.getUserType())) {

        }

        if ("0".equals(getUserInfo.getUserType())) {
            /**
             * 管辖区域
             * 1、如果传入areaId不为空：判断传入的areaId是不是在当前用户的管辖区域内，
             * 如果传入的areaId合法，当flag为0（或者flag为空）时只查数据库中和传入areaId相等的数据，
             * 当flag为1时先查当前用户的管辖区域，然后再查sys_area表里面传入areaId的子区域，
             * 最后把两者的交集里面的areaId拿出来，到数据库里面查areaId在交集里面的数据。
             * 2、如果传入的areaId为空，那就查询该用户管辖区域的数据。
             */
            if (ObjectUtils.isNotEmpty(param.getIsApplicableArea()) && param.getIsApplicableArea() == 0) {
                // 获取当前用户的管辖区域列表
                List<Long> list = peopleGovernmentService.getManageAreaIdList();
                if (ObjectUtils.isNotEmpty(param.getAreaId())) {
                    // 如果传入的areaId不合法，直接返回空的页面
                    if (!list.contains(param.getAreaId())) {
                        return Collections.emptyList();
                    }
                    // 当传入的areaId合法时
                    // 当flag为0（或者flag为空）时只查数据库中和传入areaId相等的数据
                    if (ObjectUtils.isEmpty(param.getFlag()) || param.getFlag() == 0) {
                        lambdaQueryWrapper.eq(EcoChainProductClassification::getAreaId, param.getAreaId());
                    }
                    // 当flag为1时先查当前用户的管辖区域，然后再查sys_area表里面传入areaId的子区域，
                    // 最后把两者的交集里面的areaId拿出来，到数据库里面查areaId在交集里面的数据。
                    if (ObjectUtils.isNotEmpty(param.getFlag()) && param.getFlag() == 1) {
                        List<Long> areaIdList = sysAreaService.getChildAreaIdList(param.getAreaId());
                        list.retainAll(areaIdList);
                        lambdaQueryWrapper.in(EcoChainProductClassification::getAreaId, list);
                    }
                } else {
                    lambdaQueryWrapper.in(EcoChainProductClassification::getAreaId, list);
                }
            }
        }

        if ("1".equals(getUserInfo.getUserType())) {
            lambdaQueryWrapper.eq(ObjectUtils.isNotEmpty(getUserInfo.getSocialCreditCode()),
                    EcoChainProductClassification::getSocialCreditCode, getUserInfo.getSocialCreditCode());
        }

        lambdaQueryWrapper.eq(EcoChainProductClassification::getClassificationGrade, 1);

        return ecoChainProductClassificationMapper.selectList(lambdaQueryWrapper);
    }

    public EcoChainProductClassification getAdjacentClassifiaction(EcoChainProductClassification currentTag,
            boolean isNext) {
        LambdaQueryWrapper<EcoChainProductClassification> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(EcoChainProductClassification::getParentId, currentTag.getParentId());
        queryWrapper.eq(ObjectUtils.isNotEmpty(getUserInfo.getSocialCreditCode()),
                EcoChainProductClassification::getSocialCreditCode, getUserInfo.getSocialCreditCode());
        // 根据 isNext 决定查询前一条还是后一条记录
        if (isNext) {
            queryWrapper.gt(EcoChainProductClassification::getSortNumber, currentTag.getSortNumber())
                    .orderByAsc(EcoChainProductClassification::getSortNumber);
        } else {
            queryWrapper.lt(EcoChainProductClassification::getSortNumber, currentTag.getSortNumber())
                    .orderByDesc(EcoChainProductClassification::getSortNumber);
        }

        // 使用 Page 对象限制查询结果为 1 条
        Page<EcoChainProductClassification> page = new Page<>(1, 1);
        page = ecoChainProductClassificationMapper.selectPage(page, queryWrapper);

        // 获取查询结果
        List<EcoChainProductClassification> records = page.getRecords();
        if (records.isEmpty()) {
            return null; // 如果没有找到相邻记录，则返回 null
        }
        return records.get(0); // 返回查询到的相邻记录
    }

}
