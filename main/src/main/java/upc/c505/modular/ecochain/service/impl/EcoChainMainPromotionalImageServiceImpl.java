package upc.c505.modular.ecochain.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import upc.c505.common.UserUtils;
import upc.c505.common.wrapper.MyLambdaQueryWrapper;
import upc.c505.exception.BusinessErrorEnum;
import upc.c505.exception.BusinessException;
import upc.c505.modular.auth.service.ISysAreaService;
import upc.c505.modular.ecochain.controller.param.EcoChainMainPromotionalImageSearchParam;
import upc.c505.modular.ecochain.entity.EcoChainMainPromotionalImage;
import upc.c505.modular.ecochain.entity.EcoChainProductTag;
import upc.c505.modular.ecochain.entity.EcoChainTypeConfiguration;
import upc.c505.modular.ecochain.mapper.EcoChainMainPromotionalImageMapper;
import upc.c505.modular.ecochain.service.IEcoChainMainPromotionalImageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import upc.c505.modular.ecochain.util.GetUserInfo;
import upc.c505.modular.people.service.IPeopleGovernmentService;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static upc.c505.modular.filemanage.utils.FileManageUtil.deleteFile;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author byh
 * @since 2024-09-27
 */
@Service
public class EcoChainMainPromotionalImageServiceImpl extends ServiceImpl<EcoChainMainPromotionalImageMapper, EcoChainMainPromotionalImage> implements IEcoChainMainPromotionalImageService {
    @Autowired
    private GetUserInfo getUserInfo;

    @Autowired
    private EcoChainMainPromotionalImageMapper ecoChainMainPromotionalImageMapper;

    @Autowired
    private ISysAreaService sysAreaService;

    @Autowired
    private IPeopleGovernmentService peopleGovernmentService;
    @Override
    public void insertMainPromotionalImage(EcoChainMainPromotionalImage param) {
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
            if (ObjectUtils.isEmpty(param.getRotationSequence())) {
                Integer RotationSequence = ecoChainMainPromotionalImageMapper.selectMaxRotationSequence(param);
                if (ObjectUtils.isNotEmpty(RotationSequence)) {
                    param.setRotationSequence(RotationSequence + 1);
                } else {
                    param.setRotationSequence(1);
                }
            }
            this.save(param);
        } else {
            throw new BusinessException(BusinessErrorEnum.PARAMETER_VALIDATION_ERROR, "未查询到社会信用代码");
        }
    }

    @Override
    public boolean updateMainPromotionalImage(EcoChainMainPromotionalImage param) {
        if (ObjectUtils.isEmpty(param.getOperator()) && ObjectUtils.isNotEmpty(UserUtils.get().getUsername())) {
            param.setOperator(UserUtils.get().getUsername());
        }
        if (ObjectUtils.isEmpty(param.getOperationDatetime())) {
            param.setOperationDatetime(LocalDateTime.now());
        }

        EcoChainMainPromotionalImage existingRecord = ecoChainMainPromotionalImageMapper.selectById(param);
        if (existingRecord == null) {
            throw new RuntimeException("Record not found");
        }
        // 创建UpdateWrapper
        UpdateWrapper<EcoChainMainPromotionalImage> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", param.getId());

        if (StringUtils.isNotBlank(param.getPicture())) {
            updateWrapper.set("picture", param.getPicture());
            // 如果更新了图片，可能需要删除旧图片
            if (!param.getPicture().equals(existingRecord.getPicture())) {
                deleteFile(existingRecord.getPicture());
            }
        }
        // 图片名称
        if (StringUtils.isNotBlank(param.getPictureName())) {
            updateWrapper.set("picture_name", param.getPictureName());
        }

        // 轮播顺序（允许0）
        if (param.getRotationSequence() != null) {
            updateWrapper.set("rotation_sequence", param.getRotationSequence());
        }

        // 状态
        if (StringUtils.isNotBlank(param.getStatus())) {
            updateWrapper.set("status", param.getStatus());
        }

        // 推广标语
        if (StringUtils.isNotBlank(param.getPromotionSlogan())) {
            updateWrapper.set("promotion_slogan", param.getPromotionSlogan());
        }

        // 企业名称
        if (StringUtils.isNotBlank(param.getEnterpriseName())) {
            updateWrapper.set("enterprise_name", param.getEnterpriseName());
        }

        // 社会信用代码
        if (StringUtils.isNotBlank(param.getSocialCreditCode())) {
            updateWrapper.set("social_credit_code", param.getSocialCreditCode());
        }

        // 区域ID（允许0）
        if (param.getAreaId() != null) {
            updateWrapper.set("area_id", param.getAreaId());
        }

        // 操作人
        if (StringUtils.isNotBlank(param.getOperator())) {
            updateWrapper.set("operator", param.getOperator());
        }

        // 操作时间
        if (param.getOperationDatetime() != null) {
            updateWrapper.set("operation_datetime", param.getOperationDatetime());
        }

        // 执行更新
        int updateResult = ecoChainMainPromotionalImageMapper.update(null, updateWrapper);

        if (updateResult <= 0) {
            throw new RuntimeException("更新失败");
        }
        return true;
    }

    @Override
    public List<EcoChainMainPromotionalImage> selectMainPromotionalImage(EcoChainMainPromotionalImageSearchParam param) {
        if ("-1".equals(getUserInfo.getUserType())) {
            MyLambdaQueryWrapper<EcoChainMainPromotionalImage> lambdaQueryWrapper = new MyLambdaQueryWrapper<>();
            lambdaQueryWrapper.like(ObjectUtils.isNotEmpty(param.getPictureName()), EcoChainMainPromotionalImage::getPictureName, param.getPictureName());
            lambdaQueryWrapper.orderByAsc(EcoChainMainPromotionalImage::getRotationSequence);

            if (ObjectUtils.isNotEmpty(param.getStatus())) {
                lambdaQueryWrapper.eq(EcoChainMainPromotionalImage::getStatus, param.getStatus());
            }

            if (ObjectUtils.isNotEmpty(param.getSocialCreditCode())) {
                lambdaQueryWrapper.eq(EcoChainMainPromotionalImage::getSocialCreditCode, param.getSocialCreditCode());
            }

            return ecoChainMainPromotionalImageMapper.selectList(lambdaQueryWrapper);
        }

        // 二、政府人员或二级管理员用户登录，根据管辖区域查询全部
        if ("0".equals(getUserInfo.getUserType())) {
            MyLambdaQueryWrapper<EcoChainMainPromotionalImage> lambdaQueryWrapper = new MyLambdaQueryWrapper<>();

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
                        return Collections.emptyList();
                    }
                    // 当传入的areaId合法时
                    // 当flag为0（或者flag为空）时只查数据库中和传入areaId相等的数据
                    if (ObjectUtils.isEmpty(param.getFlag()) || param.getFlag() == 0) {
                        lambdaQueryWrapper.eq(EcoChainMainPromotionalImage::getAreaId, param.getAreaId());
                    }
                    // 当flag为1时先查当前用户的管辖区域，然后再查sys_area表里面传入areaId的子区域，
                    // 最后把两者的交集里面的areaId拿出来，到数据库里面查areaId在交集里面的数据。
                    if (ObjectUtils.isNotEmpty(param.getFlag()) && param.getFlag() == 1) {
                        List<Long> areaIdList = sysAreaService.getChildAreaIdList(param.getAreaId());
                        list.retainAll(areaIdList);
                        lambdaQueryWrapper.in(EcoChainMainPromotionalImage::getAreaId, list);
                    }
                } else {
                    lambdaQueryWrapper.in(EcoChainMainPromotionalImage::getAreaId, list);
                }
            }
            lambdaQueryWrapper.like(ObjectUtils.isNotEmpty(param.getPictureName()), EcoChainMainPromotionalImage::getPictureName, param.getPictureName());
            lambdaQueryWrapper.orderByAsc(EcoChainMainPromotionalImage::getRotationSequence);

            if (ObjectUtils.isNotEmpty(param.getStatus())) {
                lambdaQueryWrapper.eq(EcoChainMainPromotionalImage::getStatus, param.getStatus());
            }

            if (ObjectUtils.isNotEmpty(param.getSocialCreditCode())) {
                lambdaQueryWrapper.eq(EcoChainMainPromotionalImage::getSocialCreditCode, param.getSocialCreditCode());
            }

            return ecoChainMainPromotionalImageMapper.selectList(lambdaQueryWrapper);
        }

        // 三、本企业人员登录
        if ("1".equals(getUserInfo.getUserType())) {
            MyLambdaQueryWrapper<EcoChainMainPromotionalImage> lambdaQueryWrapper = new MyLambdaQueryWrapper<>();

            lambdaQueryWrapper.like(ObjectUtils.isNotEmpty(param.getPictureName()), EcoChainMainPromotionalImage::getPictureName, param.getPictureName());
            lambdaQueryWrapper.orderByAsc(EcoChainMainPromotionalImage::getRotationSequence);

            if (ObjectUtils.isNotEmpty(param.getSocialCreditCode())) {
                lambdaQueryWrapper.eq(EcoChainMainPromotionalImage::getSocialCreditCode, param.getSocialCreditCode());
            } else {
                lambdaQueryWrapper.eq(ObjectUtils.isNotEmpty(getUserInfo.getSocialCreditCode()), EcoChainMainPromotionalImage::getSocialCreditCode, param.getSocialCreditCode());
            }

            if (ObjectUtils.isNotEmpty(param.getStatus())) {
                lambdaQueryWrapper.eq(EcoChainMainPromotionalImage::getStatus, param.getStatus());
            }

            return ecoChainMainPromotionalImageMapper.selectList(lambdaQueryWrapper);
        }
        return Collections.emptyList();
    }

    @Override
    public List<EcoChainMainPromotionalImage> selectEnableMainPromotionalImage(EcoChainMainPromotionalImageSearchParam param) {
        if (ObjectUtils.isEmpty(getUserInfo.getSocialCreditCode())) {
            return Collections.emptyList();
        } else {
            return ecoChainMainPromotionalImageMapper.selectList(new MyLambdaQueryWrapper<EcoChainMainPromotionalImage>().eq(EcoChainMainPromotionalImage::getSocialCreditCode, getUserInfo.getSocialCreditCode())
                    .eq(EcoChainMainPromotionalImage::getStatus, "1"));
        }
    }

    @Override
    public boolean updateMainPromotionalImageStatus(Long id, Integer status) {
        LambdaUpdateWrapper<EcoChainMainPromotionalImage> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(EcoChainMainPromotionalImage::getId, id);
        // 设置要更新的字段
        lambdaUpdateWrapper.set(EcoChainMainPromotionalImage::getStatus, status);
        // 执行更新操作，返回更新结果
        return this.update(lambdaUpdateWrapper);
    }

    @Override
    public boolean removeMainPromotionalImage(List<Long> idList) {
        if (idList == null || idList.isEmpty()) {
            return false;
        }

        // Create a query wrapper to find records with the given IDs
        LambdaQueryWrapper<EcoChainMainPromotionalImage> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(EcoChainMainPromotionalImage::getId, idList);

        // Get all records with their picture paths
        List<EcoChainMainPromotionalImage> imageList = this.list(queryWrapper);

        // Delete each picture file
        boolean allFilesDeleted = true;
        for (EcoChainMainPromotionalImage image : imageList) {
            String picturePath = image.getPicture();
            if (picturePath != null && !picturePath.isEmpty()) {
                boolean fileDeleted = deleteFile(picturePath);
                if (!fileDeleted) {
                    allFilesDeleted = false;
                }
            }
        }

        // Remove records from the database
        boolean recordsRemoved = this.removeByIds(idList);

        // Return true only if all operations succeeded
        return allFilesDeleted && recordsRemoved;
    }

}
