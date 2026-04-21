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
import upc.c505.modular.ecochain.controller.param.EcoChainProductParameterSearchParam;
import upc.c505.modular.ecochain.entity.EcoChainProductClassification;
import upc.c505.modular.ecochain.entity.EcoChainProductManagerValueContent;
import upc.c505.modular.ecochain.entity.EcoChainProductParameter;
import upc.c505.modular.ecochain.entity.EcoChainProductParameterValueContent;
import upc.c505.modular.ecochain.mapper.EcoChainProductManagerValueContentMapper;
import upc.c505.modular.ecochain.mapper.EcoChainProductParameterMapper;
import upc.c505.modular.ecochain.mapper.EcoChainProductParameterValueContentMapper;
import upc.c505.modular.ecochain.service.IEcoChainProductParameterService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import upc.c505.modular.ecochain.service.IEcoChainProductParameterValueContentService;
import upc.c505.modular.ecochain.util.GetUserInfo;
import upc.c505.modular.people.service.IPeopleGovernmentService;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
public class EcoChainProductParameterServiceImpl
        extends ServiceImpl<EcoChainProductParameterMapper, EcoChainProductParameter>
        implements IEcoChainProductParameterService {

    @Autowired
    private GetUserInfo getUserInfo;

    @Autowired
    private EcoChainProductParameterMapper ecoChainProductParameterMapper;

    @Autowired
    private EcoChainProductParameterValueContentMapper ecoChainProductParameterValueContentMapper;

    @Autowired
    private ISysAreaService sysAreaService;

    @Autowired
    private IPeopleGovernmentService peopleGovernmentService;

    @Autowired
    private IEcoChainProductParameterValueContentService ecoChainProductParameterValueContentService;

    @Autowired
    private EcoChainProductManagerValueContentMapper ecoChainProductManagerValueContentMapper;

    @Override
    public void insertProductParameter(EcoChainProductParameter param) {
        if (ObjectUtils.isEmpty(param)) {
            throw new BusinessException(BusinessErrorEnum.PARAMETER_VALIDATION_ERROR, "前端传参为空");
        }
        if (getUserInfo.getSocialCreditCode() != null && getUserInfo.getEnterpriseName() != null) {
            param.setSocialCreditCode(getUserInfo.getSocialCreditCode());
            param.setEnterpriseName(getUserInfo.getEnterpriseName());
            if (ObjectUtils.isEmpty(param.getStatus())) {
                // 状态1为正常，状态0为禁用
                param.setStatus("1");
            }
            if (ObjectUtils.isEmpty(param.getAreaId()) && ObjectUtils.isNotEmpty(UserUtils.get().getAreaId())) {
                param.setAreaId(UserUtils.get().getAreaId());
            }
            Integer sortNumber = ecoChainProductParameterMapper.selectMaxSortNumber(param);
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
    public void removeProductParameter(List<Long> idList) {
        if (ObjectUtils.isNotEmpty(idList)) {
            for (Long id : idList) {
                EcoChainProductParameter ecoChainProductParameter = ecoChainProductParameterMapper.selectById(id);
                if (ObjectUtils.isNotEmpty(ecoChainProductParameter)
                        && !Objects.equals(ecoChainProductParameter.getIsDefault(), "1")) {
                    ecoChainProductParameterMapper.deleteById(id);
                    MyLambdaQueryWrapper<EcoChainProductParameterValueContent> lambdaQueryWrapper = new MyLambdaQueryWrapper<>();
                    lambdaQueryWrapper.eq(EcoChainProductParameterValueContent::getProductParameterId, id);
                    ecoChainProductParameterValueContentMapper.delete(lambdaQueryWrapper);
                    // MyLambdaQueryWrapper<EcoChainProductManagerValueContent> lambdaQueryWrapper1
                    // = new MyLambdaQueryWrapper<>();
                    // lambdaQueryWrapper1.eq(EcoChainProductManagerValueContent::getEcoChainProductParameterId,
                    // id);
                    // ecoChainProductManagerValueContentMapper.delete(lambdaQueryWrapper1);
                }
            }
        }
    }

    @Override
    public Boolean updateProductParameter(EcoChainProductParameter param) {
        if (ObjectUtils.isEmpty(param.getOperator()) && ObjectUtils.isNotEmpty(UserUtils.get().getUsername())) {
            param.setOperator(UserUtils.get().getUsername());
        }
        if (ObjectUtils.isEmpty(param.getOperationDatetime())) {
            param.setOperationDatetime(LocalDateTime.now());
        }
        return ecoChainProductParameterMapper.updateProductParameter(param);
    }

    @Override
    public boolean updateProductParameterStatus(Long id, Integer status) {
        LambdaUpdateWrapper<EcoChainProductParameter> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(EcoChainProductParameter::getId, id);
        // 设置要更新的字段
        lambdaUpdateWrapper.set(EcoChainProductParameter::getStatus, status);
        // 执行更新操作，返回更新结果
        return this.update(lambdaUpdateWrapper);
    }

    @Override
    public boolean updateProductParameterSortNumber(Long id, Integer param) {
        EcoChainProductParameter currentTag = ecoChainProductParameterMapper.selectById(id);
        // 根据param决定是向上还是向下调整
        // 0向上，1向下
        boolean isNext = param != 0;
        EcoChainProductParameter adjacentParameter = getAdjacentParameter(currentTag, isNext);
        if (adjacentParameter == null) {
            return false;
        }
        // 交换sortNumber
        Integer tempSortNumber = currentTag.getSortNumber();
        currentTag.setSortNumber(adjacentParameter.getSortNumber());
        adjacentParameter.setSortNumber(tempSortNumber);
        // 更新数据库
        ecoChainProductParameterMapper.updateById(currentTag);
        ecoChainProductParameterMapper.updateById(adjacentParameter);
        return true;
    }

    @Override
    public List<EcoChainProductParameter> selectProductParameterList(EcoChainProductParameterSearchParam param) {
        List<EcoChainProductParameter> resultList = new ArrayList<>();
        // 一、超级管理员用户登录，查询全部
        if ("-1".equals(getUserInfo.getUserType())) {
            MyLambdaQueryWrapper<EcoChainProductParameter> lambdaQueryWrapper = new MyLambdaQueryWrapper<>();

            lambdaQueryWrapper.like(ObjectUtils.isNotEmpty(param.getParameterName()),
                    EcoChainProductParameter::getParameterName, param.getParameterName());
            lambdaQueryWrapper.eq(ObjectUtils.isNotEmpty(param.getType()), EcoChainProductParameter::getType,
                    param.getType());
            lambdaQueryWrapper.orderBy(true, Objects.equals(param.getIsAsc(), 1),
                    EcoChainProductParameter::getSortNumber);
            lambdaQueryWrapper.eq(ObjectUtils.isNotEmpty(param.getSocialCreditCode()),
                    EcoChainProductParameter::getSocialCreditCode, param.getSocialCreditCode());
            lambdaQueryWrapper.eq(ObjectUtils.isNotEmpty(param.getStatus()), EcoChainProductParameter::getStatus,
                    param.getStatus());

            resultList = ecoChainProductParameterMapper.selectList(lambdaQueryWrapper);
        }
        // 二、政府人员或二级管理员用户登录，根据管辖区域查询全部
        if ("0".equals(getUserInfo.getUserType())) {
            MyLambdaQueryWrapper<EcoChainProductParameter> lambdaQueryWrapper = new MyLambdaQueryWrapper<>();

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
                        lambdaQueryWrapper.eq(EcoChainProductParameter::getAreaId, param.getAreaId());
                    }
                    // 当flag为1时先查当前用户的管辖区域，然后再查sys_area表里面传入areaId的子区域，
                    // 最后把两者的交集里面的areaId拿出来，到数据库里面查areaId在交集里面的数据。
                    if (ObjectUtils.isNotEmpty(param.getFlag()) && param.getFlag() == 1) {
                        List<Long> areaIdList = sysAreaService.getChildAreaIdList(param.getAreaId());
                        list.retainAll(areaIdList);
                        lambdaQueryWrapper.in(EcoChainProductParameter::getAreaId, list);
                    }
                } else {
                    lambdaQueryWrapper.in(EcoChainProductParameter::getAreaId, list);
                }
            }

            lambdaQueryWrapper.like(ObjectUtils.isNotEmpty(param.getParameterName()),
                    EcoChainProductParameter::getParameterName, param.getParameterName());
            lambdaQueryWrapper.eq(ObjectUtils.isNotEmpty(param.getType()), EcoChainProductParameter::getType,
                    param.getType());
            lambdaQueryWrapper.orderBy(true, Objects.equals(param.getIsAsc(), 1),
                    EcoChainProductParameter::getSortNumber);
            lambdaQueryWrapper.eq(ObjectUtils.isNotEmpty(param.getSocialCreditCode()),
                    EcoChainProductParameter::getSocialCreditCode, param.getSocialCreditCode());
            lambdaQueryWrapper.eq(ObjectUtils.isNotEmpty(param.getStatus()), EcoChainProductParameter::getStatus,
                    param.getStatus());

            resultList = ecoChainProductParameterMapper.selectList(lambdaQueryWrapper);
        }

        // 三、本企业人员登录
        if ("1".equals(getUserInfo.getUserType())) {
            MyLambdaQueryWrapper<EcoChainProductParameter> lambdaQueryWrapper = new MyLambdaQueryWrapper<>();

            lambdaQueryWrapper.like(ObjectUtils.isNotEmpty(param.getParameterName()),
                    EcoChainProductParameter::getParameterName, param.getParameterName());
            lambdaQueryWrapper.eq(ObjectUtils.isNotEmpty(param.getType()), EcoChainProductParameter::getType,
                    param.getType());

            if (ObjectUtils.isNotEmpty(param.getSocialCreditCode())) {
                lambdaQueryWrapper.eq(EcoChainProductParameter::getSocialCreditCode, param.getSocialCreditCode());
            } else {
                if (ObjectUtils.isEmpty(getUserInfo.getSocialCreditCode())) {
                    throw new BusinessException(BusinessErrorEnum.USER_NO, "，未查询到用户社会信用代码");
                }
                lambdaQueryWrapper.eq(EcoChainProductParameter::getSocialCreditCode, getUserInfo.getSocialCreditCode());
            }

            lambdaQueryWrapper.orderBy(true, Objects.equals(param.getIsAsc(), 1),
                    EcoChainProductParameter::getSortNumber);
            lambdaQueryWrapper.eq(ObjectUtils.isNotEmpty(param.getStatus()), EcoChainProductParameter::getStatus,
                    param.getStatus());

            resultList = ecoChainProductParameterMapper.selectList(lambdaQueryWrapper);
        }
        List<EcoChainProductParameter> resultList1 = new ArrayList<>();
        if (ObjectUtils.isNotEmpty(resultList)) {
            for (EcoChainProductParameter ecoChainProductParameter : resultList) {
                List<EcoChainProductParameterValueContent> list = ecoChainProductParameterValueContentService
                        .selectProductParameterValueContentList(ecoChainProductParameter.getId());
                String valueName = "";
                int number = 0;
                if (ObjectUtils.isNotEmpty(list)) {
                    for (EcoChainProductParameterValueContent ecoChainProductParameterValueContent : list) {
                        if (ObjectUtils.isNotEmpty(ecoChainProductParameterValueContent.getValueName())) {
                            if (number == 0) {
                                valueName = valueName + ecoChainProductParameterValueContent.getValueName();
                                number = number + 1;
                            } else {
                                valueName = valueName + "," + ecoChainProductParameterValueContent.getValueName();
                            }
                        }
                    }
                }
                ecoChainProductParameter.setValueContent(valueName);
                resultList1.add(ecoChainProductParameter);
            }
            return resultList1;
        } else {
            return resultList;
        }
    }

    @Override
    public String selectProductParameterById(Long id) {
        EcoChainProductParameter ecoChainProductParameter = ecoChainProductParameterMapper.selectById(id);
        if (ObjectUtils.isNotEmpty(ecoChainProductParameter.getParameterName())) {
            return ecoChainProductParameter.getParameterName();
        }
        return "";
    }

    @Override
    public List<EcoChainProductParameter> selectEnableProductParameter(EcoChainProductParameterSearchParam param) {
        if (ObjectUtils.isEmpty(getUserInfo.getSocialCreditCode())) {
            return Collections.emptyList();
        } else {
            List<EcoChainProductParameter> resultList = ecoChainProductParameterMapper.selectList(
                    new MyLambdaQueryWrapper<EcoChainProductParameter>().eq(EcoChainProductParameter::getStatus, "1")
                            .eq(EcoChainProductParameter::getSocialCreditCode, getUserInfo.getSocialCreditCode()));
            List<EcoChainProductParameter> resultList1 = new ArrayList<>();
            if (ObjectUtils.isNotEmpty(resultList)) {
                for (EcoChainProductParameter ecoChainProductParameter : resultList) {
                    List<EcoChainProductParameterValueContent> list = ecoChainProductParameterValueContentService
                            .selectProductParameterValueContentList(ecoChainProductParameter.getId());
                    StringBuilder valueName = new StringBuilder();
                    int number = 0;
                    if (ObjectUtils.isNotEmpty(list)) {
                        for (EcoChainProductParameterValueContent ecoChainProductParameterValueContent : list) {
                            if (ObjectUtils.isNotEmpty(ecoChainProductParameterValueContent.getValueName())) {
                                if (number == 0) {
                                    valueName.append(ecoChainProductParameterValueContent.getValueName());
                                    number = number + 1;
                                } else {
                                    valueName.append(",").append(ecoChainProductParameterValueContent.getValueName());
                                }
                            }
                        }
                    }
                    ecoChainProductParameter.setValueContent(valueName.toString());
                    resultList1.add(ecoChainProductParameter);
                }
                return resultList1;
            } else {
                return resultList;
            }
        }
    }

    public EcoChainProductParameter getAdjacentParameter(EcoChainProductParameter currentTag, boolean isNext) {
        LambdaQueryWrapper<EcoChainProductParameter> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ObjectUtils.isNotEmpty(getUserInfo.getSocialCreditCode()),
                EcoChainProductParameter::getSocialCreditCode, getUserInfo.getSocialCreditCode());
        // 根据 isNext 决定查询前一条还是后一条记录
        if (isNext) {
            queryWrapper.gt(EcoChainProductParameter::getSortNumber, currentTag.getSortNumber())
                    .orderByAsc(EcoChainProductParameter::getSortNumber);
        } else {
            queryWrapper.lt(EcoChainProductParameter::getSortNumber, currentTag.getSortNumber())
                    .orderByDesc(EcoChainProductParameter::getSortNumber);
        }

        // 使用 Page 对象限制查询结果为 1 条
        Page<EcoChainProductParameter> page = new Page<>(1, 1);
        page = ecoChainProductParameterMapper.selectPage(page, queryWrapper);

        // 获取查询结果
        List<EcoChainProductParameter> records = page.getRecords();
        if (records.isEmpty()) {
            return null; // 如果没有找到相邻记录，则返回 null
        }
        return records.get(0); // 返回查询到的相邻记录
    }
}
