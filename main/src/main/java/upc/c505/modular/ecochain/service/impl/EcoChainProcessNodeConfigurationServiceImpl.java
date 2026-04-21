package upc.c505.modular.ecochain.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import upc.c505.common.wrapper.MyLambdaQueryWrapper;
import upc.c505.exception.BusinessErrorEnum;
import upc.c505.exception.BusinessException;
import upc.c505.modular.auth.service.ISysAreaService;
import upc.c505.modular.ecochain.controller.param.EcoChainProcessNodeConfigurationPageSearchParam;
import upc.c505.modular.ecochain.entity.EcoChainBuildWarehouse;
import upc.c505.modular.ecochain.entity.EcoChainProcessNodeConfiguration;
import upc.c505.modular.ecochain.entity.EcoChainTypeConfiguration;
import upc.c505.modular.ecochain.mapper.EcoChainProcessNodeConfigurationMapper;
import upc.c505.modular.ecochain.service.IEcoChainProcessNodeConfigurationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import upc.c505.modular.ecochain.util.GetUserInfo;
import upc.c505.modular.people.service.IPeopleGovernmentService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xth
 * @since 2024-09-21
 */
@Service
public class EcoChainProcessNodeConfigurationServiceImpl extends ServiceImpl<EcoChainProcessNodeConfigurationMapper, EcoChainProcessNodeConfiguration> implements IEcoChainProcessNodeConfigurationService {

    @Autowired
    private EcoChainProcessNodeConfigurationMapper ecoChainProcessNodeConfigurationMapper;

    @Autowired
    private GetUserInfo getUserInfo;

    @Autowired
    private ISysAreaService sysAreaService;

    @Autowired
    private IPeopleGovernmentService peopleGovernmentService;

    @Override
    public void insertProcessNodeConfiguration(EcoChainProcessNodeConfiguration param) {
        if (ObjectUtils.isEmpty(param)) {
            throw new BusinessException(BusinessErrorEnum.PARAMETER_VALIDATION_ERROR, "前端传参为空");
        }
        validateInputType(param);
        if (getUserInfo.getSocialCreditCode() != null && getUserInfo.getEnterpriseName() != null) {
            param.setSocialCreditCode(getUserInfo.getSocialCreditCode());
            param.setEnterpriseName(getUserInfo.getEnterpriseName());
            this.save(param);
        } else {
            throw new BusinessException(BusinessErrorEnum.PARAMETER_VALIDATION_ERROR, "未查询到社会信用代码");
        }
    }

    @Override
    public boolean updateProcessNodeConfiguration(EcoChainProcessNodeConfiguration param) {
        if (ObjectUtils.isEmpty(param) || param.getId() == null) {
            throw new BusinessException(BusinessErrorEnum.PARAMETER_VALIDATION_ERROR, "传参错误");
        }
        validateInputType(param);
        return this.updateById(param);
    }

    @Override
    public List<EcoChainProcessNodeConfiguration> listBySocialCreditCode(String param) {
        MyLambdaQueryWrapper<EcoChainProcessNodeConfiguration> lambdaQueryWrapper = new MyLambdaQueryWrapper<>();
        List<EcoChainProcessNodeConfiguration> list = new ArrayList<>();
        if(ObjectUtils.isNotNull(param)){
            lambdaQueryWrapper.eq(EcoChainProcessNodeConfiguration::getSocialCreditCode, param);
            list = ecoChainProcessNodeConfigurationMapper.selectList(lambdaQueryWrapper);
        }
        return list;
    }

    private void validateInputType(EcoChainProcessNodeConfiguration param) {
        if (ObjectUtils.isEmpty(param.getInputType())) {
            return;
        }
        if (!Objects.equals("input", param.getInputType()) && !Objects.equals("select", param.getInputType())) {
            throw new BusinessException(BusinessErrorEnum.PARAMETER_VALIDATION_ERROR, "录入类型只能是 input 或 select");
        }
        if (Objects.equals("select", param.getInputType()) && StringUtils.isBlank(param.getSelectConfig())) {
            throw new BusinessException(BusinessErrorEnum.PARAMETER_VALIDATION_ERROR, "录入类型为 select 时，选择配置不能为空");
        }
    }

    @Override
    public Page<EcoChainProcessNodeConfiguration> selectPageProcessNodeConfiguration(EcoChainProcessNodeConfigurationPageSearchParam param) {
        // 一、超级管理员用户登录，查询全部
        if(getUserInfo.getUserType().equals("-1")){
            Page<EcoChainProcessNodeConfiguration> page = new Page<>(param.getCurrent(), param.getSize());
            LambdaQueryWrapper<EcoChainProcessNodeConfiguration> queryWrapper = new LambdaQueryWrapper<>();

            queryWrapper.orderBy(true, Objects.equals(param.getIsAsc(), 1), EcoChainProcessNodeConfiguration::getNumber);

            queryWrapper.orderByDesc(EcoChainProcessNodeConfiguration::getAddDatetime);

            queryWrapper.like(
                    ObjectUtils.isNotEmpty(param.getNodeName()),
                    EcoChainProcessNodeConfiguration::getNodeName,
                    param.getNodeName()
            );
            if (ObjectUtils.isNotEmpty(param.getEndTime())) {
                LocalDate endDate = param.getEndTime();
                endDate = endDate.plusDays(1);
                queryWrapper.le(ObjectUtils.isNotEmpty(param.getEndTime()), EcoChainProcessNodeConfiguration::getAddDatetime, endDate);
            }
            queryWrapper.ge(ObjectUtils.isNotEmpty(param.getStartTime()), EcoChainProcessNodeConfiguration::getAddDatetime, param.getStartTime());

            this.page(page, queryWrapper);
            return page;
        }
        // 二、政府人员或二级管理员用户登录，根据管辖区域查询全部
        if (getUserInfo.getUserType().equals("0")) {
            Page<EcoChainProcessNodeConfiguration> page = new Page<>(param.getCurrent(), param.getSize());
            MyLambdaQueryWrapper<EcoChainProcessNodeConfiguration> queryWrapper = new MyLambdaQueryWrapper<>();

            /**
             * 管辖区域
             *         1、如果传入areaId不为空：判断传入的areaId是不是在当前用户的管辖区域内，
             *         如果传入的areaId合法，当flag为0（或者flag为空）时只查数据库中和传入areaId相等的数据，
             *         当flag为1时先查当前用户的管辖区域，然后再查sys_area表里面传入areaId的子区域，
             *         最后把两者的交集里面的areaId拿出来，到数据库里面查areaId在交集里面的数据。
             *         2、如果传入的areaId为空，那就查询该用户管辖区域的数据。
             */
            if (ObjectUtils.isNotEmpty(param.getIsApplicableArea()) && param.getIsApplicableArea() == 0) {
                // 获取当前用户的管辖区域列表
                List<Long> list = peopleGovernmentService.getManageAreaIdList();
                if (ObjectUtils.isNotEmpty(param.getAreaId())) {
                    // 如果传入的areaId不合法，直接返回空的页面
                    if (!list.contains(param.getAreaId())) {
                        return new Page<>();
                    }
                    // 当传入的areaId合法时
                    // 当flag为0（或者flag为空）时只查数据库中和传入areaId相等的数据
                    if (ObjectUtils.isEmpty(param.getFlag()) || param.getFlag() == 0) {
                        queryWrapper.eq(EcoChainProcessNodeConfiguration::getAreaId, param.getAreaId());
                    }
                    // 当flag为1时先查当前用户的管辖区域，然后再查sys_area表里面传入areaId的子区域，
                    // 最后把两者的交集里面的areaId拿出来，到数据库里面查areaId在交集里面的数据。
                    if (ObjectUtils.isNotEmpty(param.getFlag()) && param.getFlag() == 1) {
                        List<Long> areaIdList = sysAreaService.getChildAreaIdList(param.getAreaId());
                        list.retainAll(areaIdList);
                        queryWrapper.in(EcoChainProcessNodeConfiguration::getAreaId, list);
                    }
                } else {
                    queryWrapper.in(EcoChainProcessNodeConfiguration::getAreaId, list);
                }
            }

            queryWrapper.like(
                    ObjectUtils.isNotEmpty(param.getNodeName()),
                    EcoChainProcessNodeConfiguration::getNodeName,
                    param.getNodeName()
            );

            queryWrapper.orderBy(true, Objects.equals(param.getIsAsc(), 1), EcoChainProcessNodeConfiguration::getNumber);

            queryWrapper.orderByDesc(EcoChainProcessNodeConfiguration::getAddDatetime);

            if (ObjectUtils.isNotEmpty(param.getEndTime())) {
                LocalDate endDate = param.getEndTime();
                endDate = endDate.plusDays(1);
                queryWrapper.le(ObjectUtils.isNotEmpty(param.getEndTime()), EcoChainProcessNodeConfiguration::getAddDatetime, endDate);
            }
            queryWrapper.ge(ObjectUtils.isNotEmpty(param.getStartTime()), EcoChainProcessNodeConfiguration::getAddDatetime, param.getStartTime());


            this.page(page, queryWrapper);
            return page;
        }

        // 三、企业人员或企业用户登录
        if (getUserInfo.getUserType().equals("1")) {
            Page<EcoChainProcessNodeConfiguration> page = new Page<>(param.getCurrent(), param.getSize());
            LambdaQueryWrapper<EcoChainProcessNodeConfiguration> queryWrapper = new LambdaQueryWrapper<>();

            queryWrapper.like(ObjectUtils.isNotEmpty(param.getNodeName()),
                    EcoChainProcessNodeConfiguration::getNodeName, param.getNodeName());
            queryWrapper.eq(ObjectUtils.isNotEmpty(getUserInfo.getSocialCreditCode()),
                    EcoChainProcessNodeConfiguration::getSocialCreditCode, getUserInfo.getSocialCreditCode());

            queryWrapper.orderBy(true, Objects.equals(param.getIsAsc(), 1), EcoChainProcessNodeConfiguration::getNumber);

            queryWrapper.orderByDesc(EcoChainProcessNodeConfiguration::getAddDatetime);

            if (ObjectUtils.isNotEmpty(param.getEndTime())) {
                LocalDate endDate = param.getEndTime();
                endDate = endDate.plusDays(1);
                queryWrapper.le(ObjectUtils.isNotEmpty(param.getEndTime()), EcoChainProcessNodeConfiguration::getAddDatetime, endDate);
            }
            queryWrapper.ge(ObjectUtils.isNotEmpty(param.getStartTime()), EcoChainProcessNodeConfiguration::getAddDatetime, param.getStartTime());


            this.page(page, queryWrapper);
            return page;
        }

        return new Page<>();
    }
}
