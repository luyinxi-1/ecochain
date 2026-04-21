package upc.c505.modular.ecochain.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import upc.c505.modular.ecochain.controller.param.*;
import upc.c505.modular.ecochain.entity.EcoChainDataStatistics;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author la
 * @since 2024-09-26
 */
public interface IEcoChainDataStatisticsService extends IService<EcoChainDataStatistics> {
    Boolean insertDataStatistics(EcoChainDataStatistics ecoChainDataStatistics);

    EcoChainAnalyticalOverviewReturnParamOne selectAnalyticalOverview(EcoChainAnalyticalOverviewSearchParam param);

    EcoChainProductAnalysisReturnParam productAnalyticalChart(EcoChainProductAnalyticalChartSearchParam param);

    Page<EcoChainPotentialCustomerPageReturnParam> selectPotentialCustomer(EcoChainPotentialCustomerPageSearchParam param);

    Page<EcoChainProductAnalyticalPageReturnParam> productAnalyticalPage(EcoChainProductAnalyticalPageSearchParam param);

    EcoChainAnalyticalOverviewReturnParamMiniProgram selectAnalyticalOverviewMiniProgram(EcoChainAnalyticalOverviewSearchParam param);
}
