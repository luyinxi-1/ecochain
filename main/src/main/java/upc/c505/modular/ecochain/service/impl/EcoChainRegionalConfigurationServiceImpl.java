package upc.c505.modular.ecochain.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import upc.c505.common.UserUtils;
import upc.c505.common.wrapper.MyLambdaQueryWrapper;
import upc.c505.exception.BusinessErrorEnum;
import upc.c505.exception.BusinessException;
import upc.c505.modular.auth.entity.SysUser;
import upc.c505.modular.auth.service.ISysAreaService;
import upc.c505.modular.auth.service.ISysUserService;
import upc.c505.modular.ecochain.controller.param.EcoChainRegionalConfigurationPageSearchParam;
import upc.c505.modular.ecochain.entity.*;
import upc.c505.modular.ecochain.mapper.EcoChainRegionalConfigurationMapper;
import upc.c505.modular.ecochain.service.IEcoChainRegionalConfigurationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import upc.c505.modular.ecochain.util.GetUserInfo;
import upc.c505.modular.filemanage.utils.FileManageUtil;
import upc.c505.modular.people.service.IPeopleGovernmentService;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xth
 * @since 2024-09-21
 */
@Service
public class EcoChainRegionalConfigurationServiceImpl extends ServiceImpl<EcoChainRegionalConfigurationMapper, EcoChainRegionalConfiguration> implements IEcoChainRegionalConfigurationService {

    @Autowired
    EcoChainRegionalConfigurationMapper ecoChainRegionalConfigurationMapper;

    @Autowired
    private GetUserInfo getUserInfo;

    @Autowired
    private ISysAreaService sysAreaService;

    @Autowired
    private IPeopleGovernmentService peopleGovernmentService;

    @Autowired
    private ISysUserService sysUserService;

    @Override
    public boolean insertRegionalConfiguration(EcoChainRegionalConfiguration param) {
        if (ObjectUtils.isEmpty(param)) {
            throw new BusinessException(BusinessErrorEnum.PARAMETER_VALIDATION_ERROR, "前端传参为空");
        }
        if (getUserInfo.getSocialCreditCode() != null && getUserInfo.getEnterpriseName() != null) {
            param.setSocialCreditCode(getUserInfo.getSocialCreditCode());
            param.setEnterpriseName(getUserInfo.getEnterpriseName());
            if (ObjectUtils.isNotEmpty(UserUtils.get().getId())){
               param.setCreatorId(UserUtils.get().getId());
            }
            return this.save(param);
        } else {
            throw new BusinessException(BusinessErrorEnum.PARAMETER_VALIDATION_ERROR, "未查询到社会信用代码");
        }

    }

    @Override
    public List<EcoChainRegionalConfiguration> listBySocialCreditCode(String param) {
        MyLambdaQueryWrapper<EcoChainRegionalConfiguration> lambdaQueryWrapper = new MyLambdaQueryWrapper<>();
        List<EcoChainRegionalConfiguration> list = new ArrayList<>();
        if (ObjectUtils.isNotNull(param)){
            lambdaQueryWrapper.eq(EcoChainRegionalConfiguration::getSocialCreditCode, param);
            list = ecoChainRegionalConfigurationMapper.selectList(lambdaQueryWrapper);
        }
        return list;
    }

    @Override
    public Page<EcoChainRegionalConfiguration> selectPageRegionalConfiguration(EcoChainRegionalConfigurationPageSearchParam param) {
        // 一、超级管理员登录时，查询全部
        if (getUserInfo.getUserType().equals("-1")){
            Page<EcoChainRegionalConfiguration> page = new Page<>(param.getCurrent(), param.getSize());
            LambdaQueryWrapper<EcoChainRegionalConfiguration> queryWrapper = new LambdaQueryWrapper<>();

            queryWrapper.like(
                    ObjectUtils.isNotEmpty(param.getIndustryGroup()),
                    EcoChainRegionalConfiguration::getIndustryGroup,
                    param.getIndustryGroup()
            );

            queryWrapper.orderBy(true, Objects.equals(param.getIsAsc(), 1), EcoChainRegionalConfiguration::getAddDatetime);

            if (ObjectUtils.isNotEmpty(param.getEndTime())) {
                LocalDate endDate = param.getEndTime();
                endDate = endDate.plusDays(1);
                queryWrapper.le(ObjectUtils.isNotEmpty(param.getEndTime()), EcoChainRegionalConfiguration::getAddDatetime, endDate);
            }
            queryWrapper.ge(ObjectUtils.isNotEmpty(param.getStartTime()), EcoChainRegionalConfiguration::getAddDatetime, param.getStartTime());

            this.page(page, queryWrapper);
            return page;
        }
        // 二、政府人员或管理员用户登录，根据管辖区域查询全部
        if (getUserInfo.getUserType().equals("0")) {
            Page<EcoChainRegionalConfiguration> page = new Page<>(param.getCurrent(), param.getSize());
            MyLambdaQueryWrapper<EcoChainRegionalConfiguration> queryWrapper = new MyLambdaQueryWrapper<>();

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
                        queryWrapper.eq(EcoChainRegionalConfiguration::getAreaId, param.getAreaId());
                    }
                    // 当flag为1时先查当前用户的管辖区域，然后再查sys_area表里面传入areaId的子区域，
                    // 最后把两者的交集里面的areaId拿出来，到数据库里面查areaId在交集里面的数据。
                    if (ObjectUtils.isNotEmpty(param.getFlag()) && param.getFlag() == 1) {
                        List<Long> areaIdList = sysAreaService.getChildAreaIdList(param.getAreaId());
                        list.retainAll(areaIdList);
                        queryWrapper.in(EcoChainRegionalConfiguration::getAreaId, list);
                    }
                } else {
                    queryWrapper.in(EcoChainRegionalConfiguration::getAreaId, list);
                }
            }
            queryWrapper.like(
                    ObjectUtils.isNotEmpty(param.getIndustryGroup()),
                    EcoChainRegionalConfiguration::getIndustryGroup,
                    param.getIndustryGroup()
            );

            if (ObjectUtils.isNotEmpty(param.getEndTime())) {
                LocalDate endDate = param.getEndTime();
                endDate = endDate.plusDays(1);
                queryWrapper.le(ObjectUtils.isNotEmpty(param.getEndTime()), EcoChainRegionalConfiguration::getAddDatetime, endDate);
            }
            queryWrapper.ge(ObjectUtils.isNotEmpty(param.getStartTime()), EcoChainRegionalConfiguration::getAddDatetime, param.getStartTime());

            queryWrapper.orderBy(true, Objects.equals(param.getIsAsc(), 1), EcoChainRegionalConfiguration::getAddDatetime);

            this.page(page, queryWrapper);
            return page;
        }

        // 三、本企业人员登录
        if (getUserInfo.getUserType().equals("1")) {
            Page<EcoChainRegionalConfiguration> page = new Page<>(param.getCurrent(), param.getSize());
            LambdaQueryWrapper<EcoChainRegionalConfiguration> queryWrapper = new LambdaQueryWrapper<>();

            queryWrapper.like(ObjectUtils.isNotEmpty(param.getIndustryGroup()),
                    EcoChainRegionalConfiguration::getIndustryGroup, param.getIndustryGroup());
            queryWrapper.eq(ObjectUtils.isNotEmpty(getUserInfo.getSocialCreditCode()),
                    EcoChainRegionalConfiguration::getSocialCreditCode, getUserInfo.getSocialCreditCode());

            if (ObjectUtils.isNotEmpty(param.getEndTime())) {
                LocalDate endDate = param.getEndTime();
                endDate = endDate.plusDays(1);
                queryWrapper.le(ObjectUtils.isNotEmpty(param.getEndTime()), EcoChainRegionalConfiguration::getAddDatetime, endDate);
            }
            queryWrapper.ge(ObjectUtils.isNotEmpty(param.getStartTime()), EcoChainRegionalConfiguration::getAddDatetime, param.getStartTime());

            queryWrapper.orderBy(true, Objects.equals(param.getIsAsc(), 1), EcoChainRegionalConfiguration::getAddDatetime);

            // 1.本企业管理员用户登录---查询当前企业的所有数据
            if (getUserInfo.getIsAdmin() == "1" || UserUtils.get().getUserType() == 1) {
                this.page(page, queryWrapper);
                return page;
            }

            // 2.本企业普通员工登录---查看被设为可见人的数据
            List<EcoChainRegionalConfiguration> list = this.list(queryWrapper);
            List<EcoChainRegionalConfiguration> resultList = new ArrayList<>();

            // 2.1获取当前登录用户id，并根据用户id查询企业人员表id
            Long sysUserId = UserUtils.get().getId();
            SysUser one = sysUserService.getOne(new LambdaQueryWrapper<SysUser>()
                    .eq(SysUser::getId, sysUserId));
            if (one == null || one.getPeopleEnterpriseId() == null || one.getPeopleEnterpriseId() == 0L) {
                throw new BusinessException(BusinessErrorEnum.PARAMETER_VALIDATION_ERROR, "未查询到当前登录用户的企业人员id");
            }
            Long id = one.getPeopleEnterpriseId();

            // 2.2若当前用户在可见人列表
            for (EcoChainRegionalConfiguration eachRegionalConfiguration : list) {
                String visiblePeople = eachRegionalConfiguration.getVisiblePeople() != null ? eachRegionalConfiguration.getVisiblePeople() : "";
                String[] split = visiblePeople.split(",");
                // 2.3判断id是否在split中
                if (Arrays.asList(split).contains(String.valueOf(id)) || Objects.equals(eachRegionalConfiguration.getCreatorId(), UserUtils.get().getId())) {
                    // 2.4添加当前登录用户可见的数据
                    resultList.add(eachRegionalConfiguration);
                }
            }

            // --此处需要手动设置分页
            // 计算当前页的起始索引
            int fromIndex = (int)((param.getCurrent() - 1) * param.getSize());
            int toIndex = (int)Math.min(fromIndex + param.getSize(), resultList.size());
            // 获取当前页的记录
            List<EcoChainRegionalConfiguration> currentPageRecords = resultList.subList(fromIndex, toIndex);
            // 创建分页对象

            page.setTotal(resultList.size());
            page.setRecords(currentPageRecords);

            return page;
        }
        return new Page<>();
    }

    @Override
    public boolean updateRegionalConfiguration(EcoChainRegionalConfiguration param) {
        if (ObjectUtils.isEmpty(param) || param.getId() == null) {
            throw new BusinessException(BusinessErrorEnum.PARAMETER_VALIDATION_ERROR, "传参错误");
        }

        EcoChainRegionalConfiguration ecoChainRegionalConfiguration = this.getById(param.getId());
        if (ecoChainRegionalConfiguration == null) {
            throw new BusinessException(BusinessErrorEnum.PARAMETER_VALIDATION_ERROR, "记录不存在");
        }

        // 统一处理图片更新
        FileManageUtil.handlePictureUpdate(ecoChainRegionalConfiguration.getPictures(), param.getPictures());

        return this.updateById(param);
    }


    @Override
    public void removeBatchRegionalConfiguration(List<Long> idList) {
        if (ObjectUtils.isEmpty(idList)) {
            throw new BusinessException(BusinessErrorEnum.PARAMETER_VALIDATION_ERROR, "ID 列表不能为空");
        }

        // 批量查询，减少数据库查询次数
        List<EcoChainRegionalConfiguration> records = this.listByIds(idList);

        // 提取所有图片 JSON
        List<String> picturesList = records.stream()
                .map(EcoChainRegionalConfiguration::getPictures)
                .filter(ObjectUtils::isNotEmpty)
                .collect(Collectors.toList());

        // 删除图片
        FileManageUtil.handleBatchPictureDelete(picturesList);

        // 批量删除数据库记录
        this.removeByIds(idList);
    }

}
