package upc.c505.modular.ecochain.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import upc.c505.exception.BusinessErrorEnum;
import upc.c505.exception.BusinessException;
import upc.c505.modular.auth.entity.SysUser;
import upc.c505.modular.auth.mapper.NewSysUserMapper;
import upc.c505.modular.auth.service.ISysAreaService;
import upc.c505.modular.ecochain.controller.param.*;
import upc.c505.modular.ecochain.ecoConst.EcoChainDistributorConst;
import upc.c505.modular.ecochain.entity.EcoChainDistributor;
import upc.c505.modular.ecochain.entity.EcoChainEnterpriseAuthorize;
import upc.c505.modular.ecochain.mapper.EcoChainDistributorMapper;
import upc.c505.modular.ecochain.service.IEcoChainDistributorService;
import upc.c505.modular.ecochain.util.GetUserInfo;
import upc.c505.modular.people.controller.param.PeopleEnterpriseInsertParam;
import upc.c505.modular.people.entity.PeopleEnterprise;
import upc.c505.modular.people.mapper.PeopleEnterpriseMapper;
import upc.c505.modular.people.service.IPeopleEnterpriseService;
import upc.c505.modular.people.service.IPeopleGovernmentService;
import upc.c505.modular.supenterprise.entity.SupEnterprise;
import upc.c505.modular.supenterprise.service.ISupEnterpriseService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author byh
 * @since 2024-11-14
 */
@Service
public class EcoChainDistributorServiceImpl extends ServiceImpl<EcoChainDistributorMapper, EcoChainDistributor>
        implements IEcoChainDistributorService {
    @Autowired
    private EcoChainDistributorMapper ecoChainDistributorMapper;

    @Autowired
    private GetUserInfo getUserInfo;

    @Autowired
    private EcoChainEnterpriseAuthorizeServiceImpl ecoChainEnterpriseAuthorizeService;

    @Autowired
    private IPeopleGovernmentService peopleGovernmentService;

    @Autowired
    private ISysAreaService sysAreaService;

    @Autowired
    private ISupEnterpriseService supEnterpriseService;

    @Autowired
    private IPeopleEnterpriseService peopleEnterpriseService;

    @Autowired
    private PeopleEnterpriseMapper peopleEnterpriseMapper;

    @Autowired
    private NewSysUserMapper sysUserMapper;

    @Override
    public Boolean insertDistributor(EcoChainDistributor param) {
        if (ObjectUtils.isEmpty(param)) {
            throw new BusinessException(BusinessErrorEnum.PARAMETER_VALIDATION_ERROR, "前端传参为空");
        }

        return this.save(param);
    }

    @Override
    public Page<EcoChainDistributorPageReturnParam> selectDistributorPage(EcoChainDistributorPageSearchParam param) {
        if (ObjectUtils.isEmpty(param.getSharerSocialCreditCode())) {
            if (ObjectUtils.isNotEmpty(getUserInfo.getSocialCreditCode())) {
                param.setSharerSocialCreditCode(getUserInfo.getSocialCreditCode());
            }
        }
        List<EcoChainDistributorListParam> ecoChainDistributorListParams = ecoChainDistributorMapper
                .selectDistributorsList(param);
        // 二级管理员权限判断
        if ("0".equals(getUserInfo.getUserType())) {
            // 获取当前用户的管辖区域列表
            List<Long> manageAreaIds = peopleGovernmentService.getManageAreaIdList();

            if (param.getIsApplicableArea() != null && param.getIsApplicableArea() == 0) {
                // 如果 areaId 不为空且合法，判断是否在管辖区域内
                if (param.getAreaId() != null && manageAreaIds.contains(param.getAreaId())) {
                    ecoChainDistributorListParams = ecoChainDistributorListParams.stream()
                            .filter(distributor -> {
                                if (param.getFlag() == null || param.getFlag() == 0) {
                                    // flag 为 0 或为空，只保留与传入的 areaId 相等的记录
                                    return distributor.getAreaId().equals(param.getAreaId());
                                } else if (param.getFlag() == 1) {
                                    // flag 为 1，获取当前用户的子区域，并取交集
                                    List<Long> childAreaIds = sysAreaService.getChildAreaIdList(param.getAreaId());
                                    childAreaIds.retainAll(manageAreaIds);
                                    return childAreaIds.contains(distributor.getAreaId());
                                }
                                return false;
                            })
                            .collect(Collectors.toList());
                } else if (param.getAreaId() == null) {
                    // areaId 为空，直接过滤出用户管辖区域的数据
                    ecoChainDistributorListParams = ecoChainDistributorListParams.stream()
                            .filter(distributor -> manageAreaIds.contains(distributor.getAreaId()))
                            .collect(Collectors.toList());
                } else {
                    // 如果 areaId 不在管辖范围，返回空列表
                    ecoChainDistributorListParams = Collections.emptyList();
                }
            }
        }
        List<EcoChainDistributorPageReturnParam> list = new ArrayList<EcoChainDistributorPageReturnParam>();

        for (EcoChainDistributorListParam param1 : ecoChainDistributorListParams) {
            Double aDouble = ecoChainEnterpriseAuthorizeService.queryUsedSize(param1.getSharerSocialCreditCode());
            Integer userStatus = ecoChainEnterpriseAuthorizeService.getUserStatus(param1.getStorageCapacity(),
                    param1.getEndDate(), aDouble);
            param1.setUserStatus(userStatus);
            EcoChainDistributorPageReturnParam result = new EcoChainDistributorPageReturnParam();
            BeanUtils.copyProperties(param1, result);
            list.add(result);
        }
        int fromIndex = (int) ((param.getCurrent() - 1) * param.getSize());
        int toIndex = (int) Math.min(fromIndex + param.getSize(), list.size());
        // 获取当前页的记录
        List<EcoChainDistributorPageReturnParam> currentPageRecords = list.subList(fromIndex, toIndex);
        // 创建分页对象
        Page<EcoChainDistributorPageReturnParam> pageResult = new Page<>(param.getCurrent(), param.getSize());
        pageResult.setTotal(list.size());
        pageResult.setRecords(currentPageRecords);

        return pageResult;
    }

    @Override
    public void exportDistributor(HttpServletResponse response, EcoChainDistributorPageSearchParam param) {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");

        try {
            String fileName = URLEncoder.encode("分销商列表", "UTF-8").replaceAll("\\+", "%20");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");

            // 获取分页的分销商数据
            Page<EcoChainDistributorPageReturnParam> distributorPage = selectDistributorPage(param);
            List<EcoChainDistributorExportParam> exportList = distributorPage.getRecords().stream()
                    .map(this::convertToExportParam)
                    .collect(Collectors.toList());

            // 写入数据到 Excel 表中
            EasyExcel.write(response.getOutputStream(), EcoChainDistributorExportParam.class)
                    .sheet("分销商列表")
                    .doWrite(exportList);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("导出失败，请重试");
        }
    }

    @Override
    @Transactional
    public Boolean integrateRegistration(EcoChainRegistrationParam param) {
        // 流程：
        // 如果企业未注册则注册企业、已注册则报错返回（返回的信息要明确）
        // 如果人员已注册则更新数据，未注册则注册人员
        // 授权表和分销商表

        SupEnterprise enterprise = supEnterpriseService.getOne(new LambdaQueryWrapper<SupEnterprise>()
                .eq(SupEnterprise::getSocialCreditCode, param.getRegistrantSocialCreditCode()));
        if (ObjectUtils.isNotEmpty(enterprise)) {
            // 如果企业已注册则报错返回
            throw new BusinessException(BusinessErrorEnum.PARAMETER_VALIDATION_ERROR, " 该企业已注册，请勿重复注册");
        }
        // 如果企业未注册，则注册企业
        SupEnterprise supEnterprise = new SupEnterprise();
        supEnterprise.setContactName(param.getRegistrantName())
                .setContactPhone(param.getRegistrantPhone())
                .setSupEnterpriseName(param.getEnterpriseName())
                .setLegalRepresentative(param.getLegalRepresentative())
                .setIndustryCategory(param.getIndustryType())
                .setSocialCreditCode(param.getRegistrantSocialCreditCode())
                .setBusinessPlace(param.getBusinessPlace())
                .setObtermStartDate(param.getObtermStartDate())
                .setEntityType("1") // 默认为1
                .setUnitName("无");
        // 如果参数中的企业类型为空，默认为1
        if (ObjectUtils.isEmpty(param.getEnterpriseType())) {
            supEnterprise.setEnterpriseType(1);
        } else {
            supEnterprise.setEnterpriseType(param.getEnterpriseType());
        }
        supEnterpriseService.insertEcoChainSupEnterprise(supEnterprise);

        // 添加授权请求
        EcoChainEnterpriseAuthorize authorize = new EcoChainEnterpriseAuthorize();
        authorize.setCreditCode(param.getRegistrantSocialCreditCode())
                .setAuthorizeStatus(0); // 待授权
        ecoChainEnterpriseAuthorizeService.save(authorize);

        // 添加经销商表
        if (param.getIsAddDistributor()) {
            EcoChainDistributor distributor = new EcoChainDistributor();
            distributor.setSharerName(param.getSharerName())
                    .setSharerPhone(param.getSharerPhone())
                    .setSharerSocialCreditCode(param.getSharerSocialCreditCode())
                    .setRegistrantName(param.getRegistrantName())
                    .setRegistrantPhone(param.getRegistrantPhone())
                    .setRegistrantSocialCreditCode(param.getRegistrantSocialCreditCode());

            this.insertDistributor(distributor);
        }

        SysUser sysUser = sysUserMapper.selectOne(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getOpenid, param.getOpenid())
                .eq(StringUtils.isNotBlank(param.getAppId()), SysUser::getAppId, param.getAppId()));
        if (ObjectUtils.isEmpty(sysUser)) {
            // openid不存在，如果人员未注册则注册人员
            PeopleEnterpriseInsertParam peopleEnterprise = new PeopleEnterpriseInsertParam();
            peopleEnterprise.setOpenid(param.getOpenid())
                    .setPeopleName(param.getRegistrantName())
                    .setContactPhone(param.getRegistrantPhone())
                    .setAppId(param.getAppId()); // Add appId
            if (ObjectUtils.isNotEmpty(param.getRegisterIdentityNumber())) { // 因为identityNumber 注册人身份号（非必填）
                peopleEnterprise.setIdentityNumber(param.getRegisterIdentityNumber());
            }
            peopleEnterprise.setSocialCreditCode(param.getRegistrantSocialCreditCode())
                    .setEnterpriseName(param.getEnterpriseName())
                    .setIndustryTypePerson(param.getIndustryType())
                    .setIsAdmin(1) // 默认为1
                    .setIsMain(1) // 默认为1
                    .setCheckStatus(1); // 默认为1
            peopleEnterpriseService.insertPeopleEnterprise(peopleEnterprise);
        } else {
            // openid存在
            if (ObjectUtils.isEmpty(sysUser.getPeopleEnterpriseId())) {
                // openid存在，但是没有关联企业人员
                throw new BusinessException(BusinessErrorEnum.USER_NO, " 没有关联企业人员");
            } else {
                PeopleEnterprise peopleEnterprise = peopleEnterpriseService.getById(sysUser.getPeopleEnterpriseId());
                if (ObjectUtils.isNotEmpty(peopleEnterprise)) {
                    // 如果人员已注册则更新数据
                    peopleEnterprise.setPeopleName(param.getRegistrantName())
                            .setContactPhone(param.getRegistrantPhone());
                    if (ObjectUtils.isNotEmpty(param.getRegisterIdentityNumber())) { // 因为identityNumber 注册人身份号非必填
                        peopleEnterprise.setIdentityNumber(param.getRegisterIdentityNumber());
                    }
                    peopleEnterprise.setSocialCreditCode(param.getRegistrantSocialCreditCode())
                            .setEnterpriseName(param.getEnterpriseName())
                            .setIndustryTypePerson(param.getIndustryType());
                    peopleEnterprise.setIsAdmin(1);
                    peopleEnterprise.setCheckStatus(1);
                    peopleEnterprise.setIsMain(1);
                    peopleEnterpriseService.updateById(peopleEnterprise);
                } else {
                    // openid存在，但是对应的企业人员信息为空
                    throw new BusinessException(BusinessErrorEnum.USER_NO, "关联企业人员信息不存在");
                }
            }
        }

        return true;
    }

    /**
     * 将分页返回参数对象转换为导出参数对象，并设置用户状态和授权状态的字符串值
     */
    private EcoChainDistributorExportParam convertToExportParam(EcoChainDistributorPageReturnParam record) {
        EcoChainDistributorExportParam exportParam = new EcoChainDistributorExportParam();
        BeanUtils.copyProperties(record, exportParam);
        exportParam.setUserStatus(getStatusString(record.getUserStatus(), EcoChainDistributorConst.USER_STATUS_MAP));
        exportParam.setAuthorizeStatus(
                getStatusString(record.getAuthorizeStatus(), EcoChainDistributorConst.AUTHORIZE_STATUS_MAP));
        return exportParam;
    }

    /**
     * 根据预定义的映射表获取用户或授权状态的字符串值
     */
    private String getStatusString(Integer status, Map<Integer, String> statusMap) {
        return statusMap.getOrDefault(status, null);
    }

}
