package upc.c505.modular.ecochain.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import upc.c505.common.responseparam.R;
import upc.c505.modular.ecochain.controller.param.EcoChainBuildWarehouseReturnParam;
import upc.c505.modular.ecochain.controller.param.EcoChainDistributorPageReturnParam;
import upc.c505.modular.ecochain.controller.param.EcoChainDistributorPageSearchParam;
import upc.c505.modular.ecochain.controller.param.EcoChainRegistrationParam;
import upc.c505.modular.ecochain.entity.EcoChainDistributor;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author byh
 * @since 2024-11-14
 */
public interface IEcoChainDistributorService extends IService<EcoChainDistributor> {

    Boolean insertDistributor(EcoChainDistributor param);

    Page<EcoChainDistributorPageReturnParam> selectDistributorPage(EcoChainDistributorPageSearchParam param);

    void exportDistributor(HttpServletResponse response, EcoChainDistributorPageSearchParam param);

    Boolean integrateRegistration(EcoChainRegistrationParam param);
}
