package upc.c505.modular.ecochain.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import upc.c505.common.wrapper.MyLambdaQueryWrapper;
import upc.c505.exception.BusinessErrorEnum;
import upc.c505.exception.BusinessException;
import upc.c505.modular.auth.service.ISysAreaService;
import upc.c505.modular.ecochain.controller.param.EcoChainEnterpriseAlliancePageSearchParam;
import upc.c505.modular.ecochain.controller.param.EcoChainEnterpriseAllianceReturnParam;
import upc.c505.modular.ecochain.controller.param.EcoChainGetEnterpriseBySocialCreditCodeReturnParam;
import upc.c505.modular.ecochain.entity.EcoChainEnterpriseAlliance;
import upc.c505.modular.ecochain.mapper.EcoChainEnterpriseAllianceMapper;
import upc.c505.modular.ecochain.service.IEcoChainEnterpriseAllianceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import upc.c505.modular.ecochain.util.GetUserInfo;
import upc.c505.modular.people.service.IPeopleEnterpriseService;
import upc.c505.modular.people.service.IPeopleGovernmentService;
import upc.c505.modular.supenterprise.entity.SupEnterprise;
import upc.c505.modular.supenterprise.service.ISupEnterpriseService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author xth
 * @since 2024-09-25
 */
@Service
public class EcoChainEnterpriseAllianceServiceImpl
        extends ServiceImpl<EcoChainEnterpriseAllianceMapper, EcoChainEnterpriseAlliance>
        implements IEcoChainEnterpriseAllianceService {

    @Autowired
    private GetUserInfo getUserInfo;

    @Autowired
    private IPeopleEnterpriseService peopleEnterpriseService;

    @Autowired
    private ISupEnterpriseService supEnterpriseService;

    @Autowired
    private ISysAreaService sysAreaService;

    @Autowired
    private IPeopleGovernmentService peopleGovernmentService;

    @Override
    public void insertEnterpriseAlliance(EcoChainEnterpriseAlliance param) {
        if (ObjectUtils.isEmpty(param)) {
            throw new BusinessException(BusinessErrorEnum.PARAMETER_VALIDATION_ERROR, "前端传参为空");
        }
        if (getUserInfo.getSocialCreditCode() != null && getUserInfo.getEnterpriseName() != null) {
            param.setSocialCreditCode(getUserInfo.getSocialCreditCode());
            param.setEnterpriseName(getUserInfo.getEnterpriseName());
            param.setStatus(1);
            this.save(param);
        } else {
            throw new BusinessException(BusinessErrorEnum.PARAMETER_VALIDATION_ERROR, "未查询到社会信用代码");
        }
    }

    @Override
    public List<EcoChainGetEnterpriseBySocialCreditCodeReturnParam> getEnterpriseBySocialCreditCode(
            String socialCreditCode,
            String enterpriseName) {
        List<EcoChainGetEnterpriseBySocialCreditCodeReturnParam> resultList = new ArrayList<>();

        LambdaQueryWrapper<SupEnterprise> queryWrapper = new LambdaQueryWrapper<>();
        boolean hasCondition = false;

        if (StringUtils.isNotBlank(socialCreditCode)) {
            queryWrapper.eq(SupEnterprise::getSocialCreditCode, socialCreditCode);
            hasCondition = true;
        } else if (StringUtils.isNotBlank(enterpriseName)) {
            queryWrapper.like(SupEnterprise::getSupEnterpriseName, enterpriseName);
            hasCondition = true;
        }

        if (hasCondition) {
            List<SupEnterprise> list = supEnterpriseService.list(queryWrapper);
            if (list == null || list.isEmpty()) {
                throw new BusinessException(BusinessErrorEnum.PARAMETER_VALIDATION_ERROR, "未查询到该企业");
            }

            String mySocialCreditCode = getUserInfo.getSocialCreditCode();

            for (SupEnterprise supEnterprise : list) {
                EcoChainGetEnterpriseBySocialCreditCodeReturnParam returnParam = new EcoChainGetEnterpriseBySocialCreditCodeReturnParam();
                BeanUtils.copyProperties(supEnterprise, returnParam);
                returnParam.setStatus(0);

                // 注意：这里需要使用查出来的企业的信用代码去查询关联关系
                String targetSocialCreditCode = supEnterprise.getSocialCreditCode();

                if (StringUtils.isNotBlank(mySocialCreditCode) && StringUtils.isNotBlank(targetSocialCreditCode)) {
                    EcoChainEnterpriseAlliance one = this.getOne(new LambdaQueryWrapper<EcoChainEnterpriseAlliance>()
                            .eq(EcoChainEnterpriseAlliance::getSocialCreditCode, mySocialCreditCode)
                            .eq(EcoChainEnterpriseAlliance::getAllianceSocialCreditCode, targetSocialCreditCode));
                    if (one != null && one.getStatus() != null) {
                        returnParam.setStatus(one.getStatus());
                    }
                }
                resultList.add(returnParam);
            }
            return resultList;
        }

        return new ArrayList<>();
    }

    @Override
    public Page<EcoChainEnterpriseAllianceReturnParam> selectPageEnterpriseAlliance(
            EcoChainEnterpriseAlliancePageSearchParam param) {
        // 一、超级管理员登录，查询全部关联数据
        if ("-1".equals(getUserInfo.getUserType())) {
            LambdaQueryWrapper<EcoChainEnterpriseAlliance> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper
                    .and((wrapper) -> wrapper
                            .like(param.getEnterpriseName() != null, EcoChainEnterpriseAlliance::getEnterpriseName,
                                    param.getEnterpriseName())
                            .or()
                            .like(param.getEnterpriseName() != null,
                                    EcoChainEnterpriseAlliance::getAllianceEnterpriseName, param.getEnterpriseName()))
                    .like(param.getSocialCreditCode() != null, EcoChainEnterpriseAlliance::getSocialCreditCode,
                            param.getSocialCreditCode())
                    .like(param.getAllianceSocialCreditCode() != null,
                            EcoChainEnterpriseAlliance::getAllianceSocialCreditCode,
                            param.getAllianceSocialCreditCode())
                    .eq(param.getStatus() != null, EcoChainEnterpriseAlliance::getStatus, param.getStatus())
                    .orderByDesc(EcoChainEnterpriseAlliance::getOperationDatetime);
            queryWrapper.orderBy(true, Objects.equals(param.getIsAsc(), 1), EcoChainEnterpriseAlliance::getAddDatetime);

            Page<EcoChainEnterpriseAlliance> pageInfo = new Page<>(param.getCurrent(), param.getSize());
            this.page(pageInfo, queryWrapper);
            List<EcoChainEnterpriseAlliance> list = pageInfo.getRecords();
            List<EcoChainEnterpriseAllianceReturnParam> resultList = new ArrayList<>();
            for (EcoChainEnterpriseAlliance item : list) {
                EcoChainEnterpriseAllianceReturnParam enterpriseAllianceReturnParam = new EcoChainEnterpriseAllianceReturnParam();
                BeanUtils.copyProperties(item, enterpriseAllianceReturnParam);
                enterpriseAllianceReturnParam.setAllianceMode(1);
                resultList.add(enterpriseAllianceReturnParam);
            }

            Page<EcoChainEnterpriseAllianceReturnParam> pageResult = new Page<>(param.getCurrent(), param.getSize());
            pageResult.setTotal(resultList.size());
            pageResult.setRecords(resultList);

            return pageResult;
        }

        // 二、政府人员或二级管理员登录，查询管辖区域内的公司
        if ("0".equals(getUserInfo.getUserType())) {
            MyLambdaQueryWrapper<EcoChainEnterpriseAlliance> queryWrapper = new MyLambdaQueryWrapper<>();
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
                        return new Page<>();
                    }
                    // 当传入的areaId合法时
                    // 当flag为0（或者flag为空）时只查数据库中和传入areaId相等的数据
                    if (ObjectUtils.isEmpty(param.getFlag()) || param.getFlag() == 0) {
                        queryWrapper.eq(EcoChainEnterpriseAlliance::getAreaId, param.getAreaId());
                    }
                    // 当flag为1时先查当前用户的管辖区域，然后再查sys_area表里面传入areaId的子区域，
                    // 最后把两者的交集里面的areaId拿出来，到数据库里面查areaId在交集里面的数据。
                    if (ObjectUtils.isNotEmpty(param.getFlag()) && param.getFlag() == 1) {
                        List<Long> areaIdList = sysAreaService.getChildAreaIdList(param.getAreaId());
                        list.retainAll(areaIdList);
                        queryWrapper.in(EcoChainEnterpriseAlliance::getAreaId, list);
                    }
                } else {
                    queryWrapper.in(EcoChainEnterpriseAlliance::getAreaId, list);
                }
            }

            queryWrapper
                    .like(param.getEnterpriseName() != null, EcoChainEnterpriseAlliance::getEnterpriseName,
                            param.getEnterpriseName())
                    .like(param.getSocialCreditCode() != null, EcoChainEnterpriseAlliance::getSocialCreditCode,
                            param.getSocialCreditCode())
                    .like(param.getEnterpriseName() != null, EcoChainEnterpriseAlliance::getAllianceEnterpriseName,
                            param.getEnterpriseName())
                    .like(param.getAllianceSocialCreditCode() != null,
                            EcoChainEnterpriseAlliance::getAllianceSocialCreditCode,
                            param.getAllianceSocialCreditCode())
                    .eq(param.getStatus() != null, EcoChainEnterpriseAlliance::getStatus, param.getStatus())
                    .orderByDesc(EcoChainEnterpriseAlliance::getOperationDatetime);
            queryWrapper.orderBy(true, Objects.equals(param.getIsAsc(), 1), EcoChainEnterpriseAlliance::getAddDatetime);

            Page<EcoChainEnterpriseAlliance> pageInfo = new Page<>(param.getCurrent(), param.getSize());
            List<EcoChainEnterpriseAlliance> list = pageInfo.getRecords();
            List<EcoChainEnterpriseAllianceReturnParam> resultList = new ArrayList<>();
            for (EcoChainEnterpriseAlliance item : list) {
                EcoChainEnterpriseAllianceReturnParam enterpriseAllianceReturnParam = new EcoChainEnterpriseAllianceReturnParam();
                BeanUtils.copyProperties(item, enterpriseAllianceReturnParam);
                enterpriseAllianceReturnParam.setAllianceMode(1);
                resultList.add(enterpriseAllianceReturnParam);
            }

            Page<EcoChainEnterpriseAllianceReturnParam> pageResult = new Page<>(param.getCurrent(), param.getSize());
            pageResult.setTotal(resultList.size());
            pageResult.setRecords(resultList);

            return pageResult;
        }

        // 三、公司人员查询
        if ("1".equals(getUserInfo.getUserType())) {
            // 1.查询本公司联盟的企业
            List<EcoChainEnterpriseAllianceReturnParam> resultList = new ArrayList<>();
            LambdaQueryWrapper<EcoChainEnterpriseAlliance> queryWrapper = new LambdaQueryWrapper<>();
            // 未查询到当前登录用户所在企业
            if (ObjectUtils.isEmpty(getUserInfo.getEnterpriseName())
                    || ObjectUtils.isEmpty(getUserInfo.getSocialCreditCode())) {
                return new Page<>();
            }
            queryWrapper
                    .eq(ObjectUtils.isNotEmpty(getUserInfo.getEnterpriseName()),
                            EcoChainEnterpriseAlliance::getEnterpriseName, getUserInfo.getEnterpriseName())
                    .eq(ObjectUtils.isNotEmpty(getUserInfo.getSocialCreditCode()),
                            EcoChainEnterpriseAlliance::getSocialCreditCode, getUserInfo.getSocialCreditCode())
                    .like(param.getEnterpriseName() != null, EcoChainEnterpriseAlliance::getAllianceEnterpriseName,
                            param.getEnterpriseName())
                    .like(param.getAllianceSocialCreditCode() != null,
                            EcoChainEnterpriseAlliance::getAllianceSocialCreditCode,
                            param.getAllianceSocialCreditCode())
                    .eq(param.getStatus() != null, EcoChainEnterpriseAlliance::getStatus, param.getStatus())
                    .orderByDesc(EcoChainEnterpriseAlliance::getOperationDatetime);
            queryWrapper.orderBy(true, Objects.equals(param.getIsAsc(), 1), EcoChainEnterpriseAlliance::getAddDatetime);

            List<EcoChainEnterpriseAlliance> listSelf = this.list(queryWrapper);
            for (EcoChainEnterpriseAlliance item : listSelf) {
                EcoChainEnterpriseAllianceReturnParam enterpriseAllianceReturnParam = new EcoChainEnterpriseAllianceReturnParam();
                BeanUtils.copyProperties(item, enterpriseAllianceReturnParam);
                enterpriseAllianceReturnParam.setAllianceMode(1);
                resultList.add(enterpriseAllianceReturnParam);
            }

            // 2.查询本公司加入联盟的企业
            LambdaQueryWrapper<EcoChainEnterpriseAlliance> queryWrapper1 = new LambdaQueryWrapper<>();
            queryWrapper1
                    .eq(ObjectUtils.isNotEmpty(getUserInfo.getEnterpriseName()),
                            EcoChainEnterpriseAlliance::getAllianceEnterpriseName, getUserInfo.getEnterpriseName())
                    .eq(ObjectUtils.isNotEmpty(getUserInfo.getSocialCreditCode()),
                            EcoChainEnterpriseAlliance::getAllianceSocialCreditCode, getUserInfo.getSocialCreditCode())
                    .like(param.getEnterpriseName() != null, EcoChainEnterpriseAlliance::getEnterpriseName,
                            param.getEnterpriseName())
                    .like(param.getAllianceSocialCreditCode() != null, EcoChainEnterpriseAlliance::getSocialCreditCode,
                            param.getAllianceSocialCreditCode())
                    .eq(param.getStatus() != null, EcoChainEnterpriseAlliance::getStatus, param.getStatus())
                    .orderByDesc(EcoChainEnterpriseAlliance::getOperationDatetime);
            queryWrapper1.orderBy(true, Objects.equals(param.getIsAsc(), 1),
                    EcoChainEnterpriseAlliance::getAddDatetime);

            List<EcoChainEnterpriseAlliance> listOther = this.list(queryWrapper1);
            for (EcoChainEnterpriseAlliance item : listOther) {
                EcoChainEnterpriseAllianceReturnParam enterpriseAllianceReturnParam = new EcoChainEnterpriseAllianceReturnParam();
                BeanUtils.copyProperties(item, enterpriseAllianceReturnParam);
                enterpriseAllianceReturnParam.setAllianceMode(2);
                resultList.add(enterpriseAllianceReturnParam);
            }

            // --此处需要手动设置分页
            // 计算当前页的起始索引
            int fromIndex = (int) ((param.getCurrent() - 1) * param.getSize());
            int toIndex = (int) Math.min(fromIndex + param.getSize(), resultList.size());
            // 获取当前页的记录
            List<EcoChainEnterpriseAllianceReturnParam> currentPageRecords = resultList.subList(fromIndex, toIndex);
            // 创建分页对象
            Page<EcoChainEnterpriseAllianceReturnParam> page = new Page<>(param.getCurrent(), param.getSize());
            page.setTotal(resultList.size());
            page.setRecords(currentPageRecords);

            return page;

        }

        return new Page<>();
    }

}
