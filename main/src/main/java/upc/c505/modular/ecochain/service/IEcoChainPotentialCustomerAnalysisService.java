package upc.c505.modular.ecochain.service;

import upc.c505.modular.ecochain.controller.param.EcoChainPotentialCustomerAnalysisSearchParam;
import upc.c505.modular.ecochain.entity.EcoChainPotentialCustomerAnalysis;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author la
 * @since 2024-11-22
 */
public interface IEcoChainPotentialCustomerAnalysisService extends IService<EcoChainPotentialCustomerAnalysis> {

    Integer selectPotentialCustomerByOpenid(EcoChainPotentialCustomerAnalysis ecoChainPotentialCustomerAnalysis);

    List<EcoChainPotentialCustomerAnalysis> selectPotentialCustomerAnalysis(EcoChainPotentialCustomerAnalysisSearchParam ecoChainPotentialCustomerAnalysis);

    Boolean insertPotentialCustomerAnalysis(EcoChainPotentialCustomerAnalysis ecoChainPotentialCustomerAnalysis);
}
