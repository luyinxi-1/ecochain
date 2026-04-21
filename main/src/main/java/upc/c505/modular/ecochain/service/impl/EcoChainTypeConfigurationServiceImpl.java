
package upc.c505.modular.ecochain.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import upc.c505.common.wrapper.MyLambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import upc.c505.exception.BusinessErrorEnum;
import upc.c505.exception.BusinessException;
import upc.c505.modular.auth.service.ISysAreaService;
import upc.c505.modular.ecochain.controller.param.EcoChainTypeConfigurationPageSearchParam;
import upc.c505.modular.ecochain.entity.EcoChainBuildWarehouse;
import upc.c505.modular.ecochain.entity.EcoChainCompleteRecord;
import upc.c505.modular.ecochain.entity.EcoChainProcessNodeConfiguration;
import upc.c505.modular.ecochain.entity.EcoChainTypeConfiguration;
import upc.c505.modular.ecochain.mapper.EcoChainTypeConfigurationMapper;
import upc.c505.modular.ecochain.service.IEcoChainTypeConfigurationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import upc.c505.modular.ecochain.util.GetUserInfo;
import upc.c505.modular.filemanage.utils.FileManageUtil;
import upc.c505.modular.people.service.IPeopleGovernmentService;

import java.time.LocalDate;
import java.util.List;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
public class EcoChainTypeConfigurationServiceImpl extends ServiceImpl<EcoChainTypeConfigurationMapper, EcoChainTypeConfiguration> implements IEcoChainTypeConfigurationService {

    @Autowired
    private EcoChainTypeConfigurationMapper ecoChainTypeConfigurationMapper;

    @Autowired
    private GetUserInfo getUserInfo;

    @Autowired
    private ISysAreaService sysAreaService;

    @Autowired
    private IPeopleGovernmentService peopleGovernmentService;

    @Override
    public boolean insertSupEnterprise(EcoChainTypeConfiguration param) {

        if (ObjectUtils.isEmpty(param)) {
            throw new BusinessException(BusinessErrorEnum.PARAMETER_VALIDATION_ERROR, "前端传参为空");
        }
        if (getUserInfo.getSocialCreditCode() != null && getUserInfo.getEnterpriseName() != null) {
            param.setSocialCreditCode(getUserInfo.getSocialCreditCode());
            param.setEnterpriseName(getUserInfo.getEnterpriseName());
            if (getUserInfo.getAreaId() != 0L) {
                Long areaId = getUserInfo.getAreaId();
                param.setAreaId(areaId);
            }
            return this.save(param);
        } else {
            throw new BusinessException(BusinessErrorEnum.PARAMETER_VALIDATION_ERROR, "未查询到社会信用代码");
        }

    }

    @Override
    public List<EcoChainTypeConfiguration> listBySocialCreditCode(String param) {
        MyLambdaQueryWrapper<EcoChainTypeConfiguration> lambdaQueryWrapper = new MyLambdaQueryWrapper<>();
        List<EcoChainTypeConfiguration> list = new ArrayList<>();
        if(ObjectUtils.isNotNull(param)){
            lambdaQueryWrapper.eq(EcoChainTypeConfiguration::getSocialCreditCode, param);
            list = ecoChainTypeConfigurationMapper.selectList(lambdaQueryWrapper);
        }
        return list;
    }

    @Override
    public boolean updateTypeConfiguration(EcoChainTypeConfiguration param) {
        if (ObjectUtils.isEmpty(param) || param.getId() == null) {
            throw new BusinessException(BusinessErrorEnum.PARAMETER_VALIDATION_ERROR, "传参错误");
        }

        EcoChainTypeConfiguration ecoChainCompleteRecord = this.getById(param.getId());
        if (ecoChainCompleteRecord == null) {
            throw new BusinessException(BusinessErrorEnum.PARAMETER_VALIDATION_ERROR, "记录不存在");
        }

        // 统一处理图片更新
        FileManageUtil.handlePictureUpdate(ecoChainCompleteRecord.getPictures(), param.getPictures());

        return this.updateById(param);
    }

    @Override
    public void removeBatchTypeConfiguration(List<Long> idList) {
        if (ObjectUtils.isEmpty(idList)) {
            throw new BusinessException(BusinessErrorEnum.PARAMETER_VALIDATION_ERROR, "ID 列表不能为空");
        }

        // 批量查询，减少数据库查询次数
        List<EcoChainTypeConfiguration> records = this.listByIds(idList);

        // 提取所有图片 JSON
        List<String> picturesList = records.stream()
                .map(EcoChainTypeConfiguration::getPictures)
                .filter(ObjectUtils::isNotEmpty)
                .collect(Collectors.toList());

        // 删除图片
        FileManageUtil.handleBatchPictureDelete(picturesList);

        // 批量删除数据库记录
        this.removeByIds(idList);

    }

    @Override
    public Page<EcoChainTypeConfiguration> selectPageTypeConfiguration(EcoChainTypeConfigurationPageSearchParam param) {
        // 一、超级管理员用户登录，查询全部
        if(getUserInfo.getUserType().equals("-1")){
            Page<EcoChainTypeConfiguration> page = new Page<>(param.getCurrent(), param.getSize());
            LambdaQueryWrapper<EcoChainTypeConfiguration> queryWrapper = new LambdaQueryWrapper<>();

            queryWrapper.like(
                    ObjectUtils.isNotEmpty(param.getDetailType()),
                    EcoChainTypeConfiguration::getDetailType,
                    param.getDetailType()
            );

            queryWrapper.orderBy(true, Objects.equals(param.getIsAsc(), 1), EcoChainTypeConfiguration::getNumber);

            queryWrapper.orderByDesc(EcoChainTypeConfiguration::getAddDatetime);

            if (ObjectUtils.isNotEmpty(param.getEndTime())) {
                LocalDate endDate = param.getEndTime();
                endDate = endDate.plusDays(1);
                queryWrapper.le(ObjectUtils.isNotEmpty(param.getEndTime()), EcoChainTypeConfiguration::getAddDatetime, endDate);
            }

            queryWrapper.ge(ObjectUtils.isNotEmpty(param.getStartTime()), EcoChainTypeConfiguration::getAddDatetime, param.getStartTime());

            this.page(page, queryWrapper);
            return page;
        }
        // 二、政府人员或二级管理员用户登录，根据管辖区域查询全部
        if (getUserInfo.getUserType().equals("0")) {
            Page<EcoChainTypeConfiguration> page = new Page<>(param.getCurrent(), param.getSize());

            MyLambdaQueryWrapper<EcoChainTypeConfiguration> queryWrapper = new MyLambdaQueryWrapper<>();

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
                        queryWrapper.eq(EcoChainTypeConfiguration::getAreaId, param.getAreaId());
                    }
                    // 当flag为1时先查当前用户的管辖区域，然后再查sys_area表里面传入areaId的子区域，
                    // 最后把两者的交集里面的areaId拿出来，到数据库里面查areaId在交集里面的数据。
                    if (ObjectUtils.isNotEmpty(param.getFlag()) && param.getFlag() == 1) {
                        List<Long> areaIdList = sysAreaService.getChildAreaIdList(param.getAreaId());
                        list.retainAll(areaIdList);
                        queryWrapper.in(EcoChainTypeConfiguration::getAreaId, list);
                    }
                } else {
                    queryWrapper.in(EcoChainTypeConfiguration::getAreaId, list);
                }
            }
            queryWrapper.like(
                    ObjectUtils.isNotEmpty(param.getDetailType()),
                    EcoChainTypeConfiguration::getDetailType,
                    param.getDetailType()
            );

            queryWrapper.orderBy(true, Objects.equals(param.getIsAsc(), 1), EcoChainTypeConfiguration::getNumber);

            queryWrapper.orderByDesc(EcoChainTypeConfiguration::getAddDatetime);

            if (ObjectUtils.isNotEmpty(param.getEndTime())) {
                LocalDate endDate = param.getEndTime();
                endDate = endDate.plusDays(1);
                queryWrapper.le(ObjectUtils.isNotEmpty(param.getEndTime()), EcoChainTypeConfiguration::getAddDatetime, endDate);
            }

            queryWrapper.ge(ObjectUtils.isNotEmpty(param.getStartTime()), EcoChainTypeConfiguration::getAddDatetime, param.getStartTime());

            this.page(page, queryWrapper);
            return page;
        }

        // 三、企业人员或企业用户登录
        if (getUserInfo.getUserType().equals("1")) {
            Page<EcoChainTypeConfiguration> page = new Page<>(param.getCurrent(), param.getSize());
            LambdaQueryWrapper<EcoChainTypeConfiguration> queryWrapper = new LambdaQueryWrapper<>();

            queryWrapper.like(ObjectUtils.isNotEmpty(param.getDetailType()),
                    EcoChainTypeConfiguration::getDetailType, param.getDetailType());
            queryWrapper.eq(ObjectUtils.isNotEmpty(getUserInfo.getSocialCreditCode()),
                    EcoChainTypeConfiguration::getSocialCreditCode, getUserInfo.getSocialCreditCode());

            queryWrapper.orderBy(true, Objects.equals(param.getIsAsc(), 1), EcoChainTypeConfiguration::getNumber);

            queryWrapper.orderByDesc(EcoChainTypeConfiguration::getAddDatetime);

            if (ObjectUtils.isNotEmpty(param.getEndTime())) {
                LocalDate endDate = param.getEndTime();
                endDate = endDate.plusDays(1);
                queryWrapper.le(ObjectUtils.isNotEmpty(param.getEndTime()), EcoChainTypeConfiguration::getAddDatetime, endDate);
            }

            queryWrapper.ge(ObjectUtils.isNotEmpty(param.getStartTime()), EcoChainTypeConfiguration::getAddDatetime, param.getStartTime());

            this.page(page, queryWrapper);
            return page;
        }

        return new Page<>();
    }
}