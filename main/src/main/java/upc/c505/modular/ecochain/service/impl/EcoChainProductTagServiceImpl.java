package upc.c505.modular.ecochain.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import upc.c505.common.UserUtils;
import upc.c505.common.wrapper.MyLambdaQueryWrapper;
import upc.c505.exception.BusinessErrorEnum;
import upc.c505.exception.BusinessException;
import upc.c505.modular.auth.service.ISysAreaService;
import upc.c505.modular.ecochain.controller.param.EcoChainProductTagSearchParam;
import upc.c505.modular.ecochain.entity.EcoChainProductClassification;
import upc.c505.modular.ecochain.entity.EcoChainProductTag;
import upc.c505.modular.ecochain.mapper.EcoChainProductTagMapper;
import upc.c505.modular.ecochain.service.IEcoChainProductTagService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import upc.c505.modular.ecochain.util.GetUserInfo;
import upc.c505.modular.people.service.IPeopleGovernmentService;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author byh
 * @since 2024-09-25
 */
@Service
public class EcoChainProductTagServiceImpl extends ServiceImpl<EcoChainProductTagMapper, EcoChainProductTag>
        implements IEcoChainProductTagService {
    @Autowired
    private GetUserInfo getUserInfo;

    @Autowired
    private EcoChainProductTagMapper ecoChainProductTagMapper;

    @Autowired
    private ISysAreaService sysAreaService;

    @Autowired
    private IPeopleGovernmentService peopleGovernmentService;

    @Override
    public void insertProductTag(EcoChainProductTag param) {
        if (ObjectUtils.isEmpty(param)) {
            throw new BusinessException(BusinessErrorEnum.PARAMETER_VALIDATION_ERROR, "前端传参为空");
        }
        if (getUserInfo.getSocialCreditCode() != null && getUserInfo.getEnterpriseName() != null) {
            param.setSocialCreditCode(getUserInfo.getSocialCreditCode());
            param.setEnterpriseName(getUserInfo.getEnterpriseName());
            if (ObjectUtils.isEmpty(param.getStatus())) {
                param.setStatus("1");
            }
            if (ObjectUtils.isEmpty(param.getAreaId()) && ObjectUtils.isNotEmpty(UserUtils.get().getAreaId())) {
                param.setAreaId(UserUtils.get().getAreaId());
            }
            Integer sortNumber = ecoChainProductTagMapper.selectMaxSortNumber(param);
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
    public boolean updateProductTagStatus(Long id, Integer status) {
        LambdaUpdateWrapper<EcoChainProductTag> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(EcoChainProductTag::getId, id);
        // 设置要更新的字段
        lambdaUpdateWrapper.set(EcoChainProductTag::getStatus, status);
        // 执行更新操作，返回更新结果
        return this.update(lambdaUpdateWrapper);
    }

    @Override
    public boolean updateProductTag(EcoChainProductTag param) {
        if (ObjectUtils.isEmpty(param.getOperator()) && ObjectUtils.isNotEmpty(UserUtils.get().getUsername())) {
            param.setOperator(UserUtils.get().getUsername());
        }
        if (ObjectUtils.isEmpty(param.getOperationDatetime())) {
            param.setOperationDatetime(LocalDateTime.now());
        }
        return ecoChainProductTagMapper.updateProductTag(param);
    }

    @Override
    public boolean updateProductTagSortNumber(Long id, Integer param) {
        EcoChainProductTag currentTag = ecoChainProductTagMapper.selectById(id);
        // 根据param决定是向上还是向下调整
        boolean isNext = param != 0;
        EcoChainProductTag adjacentTag = getAdjacentTag(currentTag, isNext);
        if (adjacentTag == null) {
            return false;
        }
        // 交换sortNumber
        Integer tempSortNumber = currentTag.getSortNumber();
        currentTag.setSortNumber(adjacentTag.getSortNumber());
        adjacentTag.setSortNumber(tempSortNumber);
        // 更新数据库
        ecoChainProductTagMapper.updateById(currentTag);
        ecoChainProductTagMapper.updateById(adjacentTag);
        return true;
    }

    @Override
    public List<EcoChainProductTag> selectProductTagList(EcoChainProductTagSearchParam param) {
        // 一、超级管理员用户登录，查询全部
        if ("-1".equals(getUserInfo.getUserType())) {
            MyLambdaQueryWrapper<EcoChainProductTag> lambdaQueryWrapper = new MyLambdaQueryWrapper<>();
            if (ObjectUtils.isNotEmpty(param.getSocialCreditCode())
                    && ObjectUtils.isNotNull(param.getSocialCreditCode())) {
                lambdaQueryWrapper.eq(EcoChainProductTag::getSocialCreditCode, param.getSocialCreditCode());
            }
            lambdaQueryWrapper.like(ObjectUtils.isNotEmpty(param.getTagName()), EcoChainProductTag::getTagName,
                    param.getTagName());
            lambdaQueryWrapper.eq(ObjectUtils.isNotEmpty(param.getType()), EcoChainProductTag::getType,
                    param.getType());
            lambdaQueryWrapper.orderBy(true, Objects.equals(param.getIsAsc(), 1), EcoChainProductTag::getSortNumber);
            lambdaQueryWrapper.eq(ObjectUtils.isNotEmpty(param.getStatus()), EcoChainProductTag::getStatus,
                    param.getStatus());

            return ecoChainProductTagMapper.selectList(lambdaQueryWrapper);
        }

        // 二、政府人员或二级管理员用户登录，根据管辖区域查询全部
        if ("0".equals(getUserInfo.getUserType())) {
            MyLambdaQueryWrapper<EcoChainProductTag> lambdaQueryWrapper = new MyLambdaQueryWrapper<>();

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
                        lambdaQueryWrapper.eq(EcoChainProductTag::getAreaId, param.getAreaId());
                    }
                    // 当flag为1时先查当前用户的管辖区域，然后再查sys_area表里面传入areaId的子区域，
                    // 最后把两者的交集里面的areaId拿出来，到数据库里面查areaId在交集里面的数据。
                    if (ObjectUtils.isNotEmpty(param.getFlag()) && param.getFlag() == 1) {
                        List<Long> areaIdList = sysAreaService.getChildAreaIdList(param.getAreaId());
                        list.retainAll(areaIdList);
                        lambdaQueryWrapper.in(EcoChainProductTag::getAreaId, list);
                    }
                } else {
                    lambdaQueryWrapper.in(EcoChainProductTag::getAreaId, list);
                }
            }
            if (ObjectUtils.isNotEmpty(param.getSocialCreditCode())
                    && ObjectUtils.isNotNull(param.getSocialCreditCode())) {
                lambdaQueryWrapper.eq(EcoChainProductTag::getSocialCreditCode, param.getSocialCreditCode());
            }
            lambdaQueryWrapper.like(ObjectUtils.isNotEmpty(param.getTagName()), EcoChainProductTag::getTagName,
                    param.getTagName());
            lambdaQueryWrapper.eq(ObjectUtils.isNotEmpty(param.getType()), EcoChainProductTag::getType,
                    param.getType());
            lambdaQueryWrapper.orderBy(true, Objects.equals(param.getIsAsc(), 1), EcoChainProductTag::getSortNumber);
            lambdaQueryWrapper.eq(ObjectUtils.isNotEmpty(param.getStatus()), EcoChainProductTag::getStatus,
                    param.getStatus());

            return ecoChainProductTagMapper.selectList(lambdaQueryWrapper);
        }

        // 三、本企业人员登录
        if ("1".equals(getUserInfo.getUserType())) {
            MyLambdaQueryWrapper<EcoChainProductTag> lambdaQueryWrapper = new MyLambdaQueryWrapper<>();

            lambdaQueryWrapper.like(ObjectUtils.isNotEmpty(param.getTagName()), EcoChainProductTag::getTagName,
                    param.getTagName());
            lambdaQueryWrapper.eq(ObjectUtils.isNotEmpty(param.getType()), EcoChainProductTag::getType,
                    param.getType());
            if (ObjectUtils.isNotEmpty(param.getSocialCreditCode())) {
                lambdaQueryWrapper.eq(EcoChainProductTag::getSocialCreditCode, param.getSocialCreditCode());
            } else {
                lambdaQueryWrapper.eq(ObjectUtils.isNotEmpty(getUserInfo.getSocialCreditCode()),
                        EcoChainProductTag::getSocialCreditCode, getUserInfo.getSocialCreditCode());
            }

            lambdaQueryWrapper.orderBy(true, Objects.equals(param.getIsAsc(), 1), EcoChainProductTag::getSortNumber);
            lambdaQueryWrapper.eq(ObjectUtils.isNotEmpty(param.getStatus()), EcoChainProductTag::getStatus,
                    param.getStatus());

            return ecoChainProductTagMapper.selectList(lambdaQueryWrapper);
        }
        return Collections.emptyList();

    }

    @Override
    public List<EcoChainProductTag> selectEnableProductTagList(EcoChainProductTagSearchParam param) {
        if (ObjectUtils.isEmpty(getUserInfo.getSocialCreditCode())) {
            return Collections.emptyList();
        } else {
            return ecoChainProductTagMapper.selectList(new MyLambdaQueryWrapper<EcoChainProductTag>()
                    .eq(EcoChainProductTag::getSocialCreditCode, getUserInfo.getSocialCreditCode())
                    .eq(EcoChainProductTag::getStatus, "1"));
        }
    }

    @Override
    public void removeProductTag(List<Long> idList) {
        for (Long id : idList) {
            EcoChainProductTag ecoChainProductTag = ecoChainProductTagMapper.selectById(id);
            if (ObjectUtils.isNotEmpty(ecoChainProductTag) && !Objects.equals(ecoChainProductTag.getIsDefault(), "1")) {
                ecoChainProductTagMapper.deleteById(id);
            }
        }
    }

    public EcoChainProductTag getAdjacentTag(EcoChainProductTag currentTag, boolean isNext) {
        LambdaQueryWrapper<EcoChainProductTag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ObjectUtils.isNotEmpty(getUserInfo.getSocialCreditCode()),
                EcoChainProductTag::getSocialCreditCode, getUserInfo.getSocialCreditCode());

        // 根据 isNext 决定查询前一条还是后一条记录
        if (isNext) {
            queryWrapper.gt(EcoChainProductTag::getSortNumber, currentTag.getSortNumber())
                    .orderByAsc(EcoChainProductTag::getSortNumber);
        } else {
            queryWrapper.lt(EcoChainProductTag::getSortNumber, currentTag.getSortNumber())
                    .orderByDesc(EcoChainProductTag::getSortNumber);
        }

        // 使用 Page 对象限制查询结果为 1 条
        Page<EcoChainProductTag> page = new Page<>(1, 1);
        page = ecoChainProductTagMapper.selectPage(page, queryWrapper);

        // 获取查询结果
        List<EcoChainProductTag> records = page.getRecords();
        if (records.isEmpty()) {
            return null; // 如果没有找到相邻记录，则返回 null
        }
        return records.get(0); // 返回查询到的相邻记录
    }
}
