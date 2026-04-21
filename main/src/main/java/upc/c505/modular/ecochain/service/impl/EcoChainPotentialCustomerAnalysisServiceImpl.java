package upc.c505.modular.ecochain.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import upc.c505.modular.ecochain.controller.param.EcoChainPotentialCustomerAnalysisSearchParam;
import upc.c505.modular.ecochain.entity.EcoChainPotentialCustomerAnalysis;
import upc.c505.modular.ecochain.entity.EcoChainProductParameter;
import upc.c505.modular.ecochain.mapper.EcoChainPotentialCustomerAnalysisMapper;
import upc.c505.modular.ecochain.service.IEcoChainPotentialCustomerAnalysisService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author la
 * @since 2024-11-22
 */
@Service
public class EcoChainPotentialCustomerAnalysisServiceImpl extends ServiceImpl<EcoChainPotentialCustomerAnalysisMapper, EcoChainPotentialCustomerAnalysis> implements IEcoChainPotentialCustomerAnalysisService {

    @Autowired
    EcoChainPotentialCustomerAnalysisMapper ecoChainPotentialCustomerAnalysisMapper;


    @Override
    public Integer selectPotentialCustomerByOpenid(EcoChainPotentialCustomerAnalysis ecoChainPotentialCustomerAnalysis){
//      openid和信用代码都符合的时候返回1，其余返回0
        String openid = ecoChainPotentialCustomerAnalysis.getOpenid();
        LambdaQueryWrapper<EcoChainPotentialCustomerAnalysis> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(EcoChainPotentialCustomerAnalysis::getOpenid,openid)
                .eq(EcoChainPotentialCustomerAnalysis::getSocialCreditCode,ecoChainPotentialCustomerAnalysis.getSocialCreditCode());
        EcoChainPotentialCustomerAnalysis record = ecoChainPotentialCustomerAnalysisMapper.selectOne(queryWrapper);
        if(ObjectUtils.isNotEmpty(record)){
            Integer currentBrowseCount = record.getViewsNumber() !=null ? record.getViewsNumber() : 0;
            record.setViewsNumber(currentBrowseCount + 1);
            record.setOperationTime(LocalDateTime.now());
            int updateResult = ecoChainPotentialCustomerAnalysisMapper.updateById(record);
            if (updateResult > 0) {
                return 1;
            } else {
                return 0;
            }
        }else {
            return 0;
        }
    }

    @Override
    public List<EcoChainPotentialCustomerAnalysis> selectPotentialCustomerAnalysis(EcoChainPotentialCustomerAnalysisSearchParam param) {
        if(ObjectUtils.isNotEmpty(param.getSocialCreditCode())){
            List<EcoChainPotentialCustomerAnalysis> returnList = ecoChainPotentialCustomerAnalysisMapper.selectList(
                    new LambdaQueryWrapper<EcoChainPotentialCustomerAnalysis>()
                            .eq(ObjectUtils.isNotEmpty(param.getSocialCreditCode()),EcoChainPotentialCustomerAnalysis::getSocialCreditCode,param.getSocialCreditCode())
                            .orderBy(true, Objects.equals(param.getIsAsc(), 1), EcoChainPotentialCustomerAnalysis::getOperationTime)
            );
            return returnList;
        }
        LambdaQueryWrapper<EcoChainPotentialCustomerAnalysis> lambdaQueryWrapper = new LambdaQueryWrapper<EcoChainPotentialCustomerAnalysis>();
        lambdaQueryWrapper.orderBy(true, Objects.equals(param.getIsAsc(), 1), EcoChainPotentialCustomerAnalysis::getOperationTime);
        List<EcoChainPotentialCustomerAnalysis> returnList = ecoChainPotentialCustomerAnalysisMapper.selectList(lambdaQueryWrapper);
        return returnList;
    }

    @Override
    public Boolean insertPotentialCustomerAnalysis(EcoChainPotentialCustomerAnalysis param) {
//      如果有openid和信用代码都符合的，浏览次数+1、更新修改时间，否则新增一条，浏览次数为1，修改时间为现在时刻
        String openid = param.getOpenid();
        LambdaQueryWrapper<EcoChainPotentialCustomerAnalysis> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(EcoChainPotentialCustomerAnalysis::getOpenid,openid)
                .eq(EcoChainPotentialCustomerAnalysis::getSocialCreditCode,param.getSocialCreditCode());
        EcoChainPotentialCustomerAnalysis record = ecoChainPotentialCustomerAnalysisMapper.selectOne(queryWrapper);
        if(ObjectUtils.isNotEmpty(record)){
            Integer currentBrowseCount = record.getViewsNumber() !=null ? record.getViewsNumber() : 0;
            record.setViewsNumber(currentBrowseCount + 1);
            record.setOperationTime(LocalDateTime.now());
            int updateResult = ecoChainPotentialCustomerAnalysisMapper.updateById(record);
        }else {
            param.setViewsNumber(1)
                    .setOperationTime(LocalDateTime.now());
            ecoChainPotentialCustomerAnalysisMapper.insert(param);
        }
        return true;
    }
}
