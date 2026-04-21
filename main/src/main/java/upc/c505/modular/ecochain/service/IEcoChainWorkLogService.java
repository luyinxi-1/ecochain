package upc.c505.modular.ecochain.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import upc.c505.modular.ecochain.controller.param.EcoChainBuildWareHousePageSearchParam;
import upc.c505.modular.ecochain.controller.param.EcoChainBuildWarehouseReturnParam;
import upc.c505.modular.ecochain.controller.param.EcoChainWorkLogParam.*;
import upc.c505.common.responseparam.R;
import upc.c505.modular.ecochain.controller.param.EcoChainWorkLogGetByIdParam;
import upc.c505.modular.ecochain.controller.param.EcoChainWorkLogGetByIdReturnParam;
import upc.c505.modular.ecochain.entity.EcoChainWorkLog;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author byh
 * @since 2025-04-16
 */
public interface IEcoChainWorkLogService extends IService<EcoChainWorkLog> {

    boolean updateWorkLog(EcoChainWorkLog param);

    List<EcoChainWorkStatisticsReturnParam> workStatistics(EcoChainWorkStatisticsSearchParam param);

    List<EcoChainWorkStatisticsSignContractRateReturnParam> workStatisticsSignContractRate(EcoChainWorkStatisticsSearchParam param);

    EcoChainWorkLogGetByIdReturnParam getDetailsById(EcoChainWorkLogGetByIdParam param);

    Page<SelectWorkLogReturnParam> selectWorkLog(SelectWorkLogSearchParam searchParam);

    void exportWorkLog(String prefixUrl, HttpServletResponse response, SelectWorkLogSearchParam searchParam);

    void exportWorkStatistics(HttpServletResponse response, EcoChainWorkStatisticsSearchParam param);


    Page<EcoChainBuildWarehouseReturnParam> selectPageBuildWarehouseGroup(EcoChainBuildWareHousePageSearchParam param);

    Page<EcoChainBuildWarehouseReturnParam> selectPageBuildWarehouseGroupByLogTime(EcoChainBuildWareHousePageSearchParam param);
}
