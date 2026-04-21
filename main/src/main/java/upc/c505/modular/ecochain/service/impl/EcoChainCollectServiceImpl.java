package upc.c505.modular.ecochain.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import upc.c505.common.UserUtils;
import upc.c505.exception.BusinessErrorEnum;
import upc.c505.exception.BusinessException;
import upc.c505.modular.ecochain.controller.param.*;
import upc.c505.modular.ecochain.entity.EcoChainCollect;
import upc.c505.modular.ecochain.entity.EcoChainDataStatistics;
import upc.c505.modular.ecochain.mapper.EcoChainCollectMapper;
import upc.c505.modular.ecochain.service.IEcoChainCollectService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import upc.c505.modular.ecochain.service.IEcoChainDataStatisticsService;
import upc.c505.modular.ecochain.service.IEcoChainDistributorService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author byh
 * @since 2025-01-20
 */
@Service
public class EcoChainCollectServiceImpl extends ServiceImpl<EcoChainCollectMapper, EcoChainCollect> implements IEcoChainCollectService {

    @Autowired
    private EcoChainCollectMapper ecoChainCollectMapper;

    @Autowired
    private IEcoChainDataStatisticsService ecoChainDataStatisticsService;

    @Override
    public Long insertCollect(EcoChainCollectInsertParam param) {
        if (ObjectUtils.isEmpty(param)) {
            throw new BusinessException(BusinessErrorEnum.PARAMETER_VALIDATION_ERROR, "前端传参为空");
        }

        Long userId = null;
        if (ObjectUtils.isNotEmpty(UserUtils.get()) && ObjectUtils.isNotEmpty(UserUtils.get().getId())) {
            userId = UserUtils.get().getId();
        }

        List<EcoChainCollect> existList = this.list(new LambdaQueryWrapper<EcoChainCollect>()
                .eq(ObjectUtils.isNotEmpty(param.getCollectId()), EcoChainCollect::getCollectId, param.getCollectId())
                .eq(ObjectUtils.isNotEmpty(param.getType()), EcoChainCollect::getType, param.getType())
                .eq(ObjectUtils.isNotEmpty(userId), EcoChainCollect::getCollectPeopleId, userId)
                .last("limit 1"));
        if (ObjectUtils.isNotEmpty(existList)) {
            return existList.get(0).getId();
        }

        EcoChainCollect result = new EcoChainCollect();
        BeanUtils.copyProperties(param, result);

        if (ObjectUtils.isNotEmpty(userId)) {
            result.setCollectPeopleId(userId);
        }

        // 保存实体对象，ID 会被自动填充
        boolean isSaved = this.save(result);
        if (!isSaved) {
            throw new BusinessException(BusinessErrorEnum.PARAMETER_VALIDATION_ERROR, "保存失败");
        }

        // 返回自动生成的 ID
        return result.getId();
    }


    @Override
    public boolean deleteCollect(Long id) {
        if (ObjectUtils.isEmpty(id)) {
            throw new BusinessException(BusinessErrorEnum.PARAMETER_VALIDATION_ERROR, "前端传参为空");
        }
        return this.remove(new LambdaQueryWrapper<EcoChainCollect>().eq(EcoChainCollect::getId, id));
    }

    @Override
    public Long selectCollectNumber(Integer type, String collectId) {
        if (ObjectUtils.isEmpty(type) && ObjectUtils.isEmpty(collectId)) {
            throw new BusinessException(BusinessErrorEnum.PARAMETER_VALIDATION_ERROR, "前端传参为空");
        }
        return this.count(new LambdaQueryWrapper<EcoChainCollect>()
                .eq(ObjectUtils.isNotEmpty(collectId), EcoChainCollect::getCollectId, collectId)
                .eq(ObjectUtils.isNotEmpty(type), EcoChainCollect::getType, type));
    }

    @Override
    public List<Long> selectIsOrNoCollect(Integer type, String collectId) {
        if (ObjectUtils.isEmpty(type) && ObjectUtils.isEmpty(collectId)) {
            throw new BusinessException(BusinessErrorEnum.PARAMETER_VALIDATION_ERROR, "前端传参为空");
        }
        Long userId = UserUtils.get().getId();
        List<EcoChainCollect> ecoChainCollects = ecoChainCollectMapper.selectList(new LambdaQueryWrapper<EcoChainCollect>()
                .eq(ObjectUtils.isNotEmpty(type), EcoChainCollect::getType, type)
                .eq(ObjectUtils.isNotEmpty(collectId), EcoChainCollect::getCollectId, collectId)
                .eq(ObjectUtils.isNotEmpty(userId), EcoChainCollect::getCollectPeopleId, userId));
        if (ObjectUtils.isNotEmpty(ecoChainCollects)) {
            return ecoChainCollects.stream().map(EcoChainCollect::getId).collect(Collectors.toList());
        }
        List<Long> list = new ArrayList<>();
        list.add(0L);
        return list;
    }

    @Override
    public List<?> selectCollectInformation(EcoChainCollectSearchParam param) {
        if (ObjectUtils.isEmpty(param.getType())) {
            throw new BusinessException(BusinessErrorEnum.PARAMETER_VALIDATION_ERROR, "前端传参为空");
        }
        // 获取用户id（收藏人id）
        Long userId = UserUtils.get().getId();
        // 获取收藏表内容
        List<EcoChainCollect> list = ecoChainCollectMapper.selectList(new LambdaQueryWrapper<EcoChainCollect>()
                .eq(ObjectUtils.isNotEmpty(param.getType()), EcoChainCollect::getType, param.getType())
                .eq(ObjectUtils.isNotEmpty(userId), EcoChainCollect::getCollectPeopleId, userId));
        // 企业
        if (param.getType() == 0) {
            List<EcoChainCollectEnterpriseParam> resultList = new ArrayList<>();
            for (EcoChainCollect collect : list) {
                List<EcoChainCollectEnterpriseParam> result = ecoChainCollectMapper.selectCollectEnterprise(param.getName(), collect.getCollectId());
                if (ObjectUtils.isEmpty(result) || (ObjectUtils.isEmpty(result.get(0).getSupEnterpriseName()) && ObjectUtils.isEmpty(result.get(0).getBusinessPlace()))) {
                    ecoChainCollectMapper.deleteById(collect.getId());
                } else {
                    result.get(0).setCollectId(collect.getId());
                    if (ObjectUtils.isNotEmpty(param.getName())) {
                        if (result.get(0).getSupEnterpriseName().contains(param.getName())) {
                            resultList.add(result.get(0));
                        }
                    } else {
                        resultList.add(result.get(0));
                    }
                }
            }
            return resultList;
        }
        // 产品
        if (param.getType() == 1) {
            List<EcoChainCollectProductParam> resultList = new ArrayList<>();
            for(EcoChainCollect collect : list){
                List<EcoChainCollectProductParam> result = ecoChainCollectMapper.selectCollectProduct(collect.getCollectId(), param.getName());
                if (ObjectUtils.isEmpty(result)) {
                    EcoChainCollectProductParam productParam = new EcoChainCollectProductParam();
                    productParam.setCollectId(collect.getId());
                    productParam.setProductName(collect.getCollectName());
                    productParam.setIsDelete(1);
                    collect.setIsDelete(1);
                    ecoChainCollectMapper.updateById(collect);
                    if (ObjectUtils.isNotEmpty(param.getName())) {
                       if (productParam.getProductName().contains(param.getName())) {
                           resultList.add(productParam);
                       }
                    } else {
                        resultList.add(productParam);
                    }
                } else {
                    result.get(0).setIsDelete(0);
                    result.get(0).setCollectId(collect.getId());
                    result.get(0).setCollectPeopleId(UserUtils.get().getId());
                    if (ObjectUtils.isNotEmpty(param.getName())) {
                        if(result.get(0).getProductName().contains(param.getName())) {
                            resultList.add(result.get(0));
                        }
                    } else {
                        resultList.add(result.get(0));
                    }
                }
            }
            return resultList;
        }
        // 人员
        if (param.getType() == 2) {
            List<EcoChainCollectPeopleParam> resultList = new ArrayList<>();
            for(EcoChainCollect collect : list){
                List<EcoChainCollectPeopleParam> result = ecoChainCollectMapper.selectCollectPeople(collect.getCollectId(), param.getName());
                if (ObjectUtils.isEmpty(result)) {
                    EcoChainCollectPeopleParam peopleParam = new EcoChainCollectPeopleParam();
                    peopleParam.setId(collect.getId());
                    peopleParam.setPeopleName(collect.getCollectName());
                    peopleParam.setIsDelete(1);
                    peopleParam.setCollectId(collect.getCollectId());
                    peopleParam.setCollectPeopleId(collect.getCollectPeopleId());
                    collect.setIsDelete(1);
                    ecoChainCollectMapper.updateById(collect);
                    if (ObjectUtils.isNotEmpty(param.getName())) {
                        if (peopleParam.getPeopleName().contains(param.getName())) {
                            resultList.add(peopleParam);
                        }
                    } else {
                        resultList.add(peopleParam);
                    }
                } else {
                    result.get(0).setIsDelete(0);
                    result.get(0).setId(collect.getId());
                    result.get(0).setCollectPeopleId(collect.getCollectPeopleId());
                    result.get(0).setCollectId(collect.getCollectId());
                    if (ObjectUtils.isNotEmpty(param.getName())) {
                        if(result.get(0).getPeopleName().contains(param.getName())) {
                            resultList.add(result.get(0));
                        }
                    } else {
                        resultList.add(result.get(0));
                    }
                }
            }
            return resultList;
        }
        return null;
    }

    @Override
    public EcoChainProductViewNumberParam selectProductNumber(Long id) {
        long productScanCount = ecoChainDataStatisticsService.count(new LambdaQueryWrapper<EcoChainDataStatistics>()
                .eq(EcoChainDataStatistics::getEcoChainProductManagerId, id)
                .eq(EcoChainDataStatistics::getStatisticalType, "产品扫码")
        );
        long productShareNumber = ecoChainDataStatisticsService.count(new LambdaQueryWrapper<EcoChainDataStatistics>()
                .eq(EcoChainDataStatistics::getEcoChainProductManagerId, id)
                .eq(EcoChainDataStatistics::getStatisticalType, "产品分享浏览")
        );
        EcoChainProductViewNumberParam result = new EcoChainProductViewNumberParam();
        result.setId(id);
        result.setViewNumber(productShareNumber + productScanCount);
        return result;
    }


}
