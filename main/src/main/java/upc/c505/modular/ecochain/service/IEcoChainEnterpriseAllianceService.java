package upc.c505.modular.ecochain.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import upc.c505.modular.ecochain.controller.param.EcoChainEnterpriseAlliancePageSearchParam;
import upc.c505.modular.ecochain.controller.param.EcoChainEnterpriseAllianceReturnParam;
import upc.c505.modular.ecochain.controller.param.EcoChainEnterpriseAllianceReturnParam;
import upc.c505.modular.ecochain.controller.param.EcoChainGetEnterpriseBySocialCreditCodeReturnParam;
import upc.c505.modular.ecochain.entity.EcoChainEnterpriseAlliance;
import com.baomidou.mybatisplus.extension.service.IService;
import upc.c505.modular.supenterprise.entity.SupEnterprise;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author xth
 * @since 2024-09-25
 */
public interface IEcoChainEnterpriseAllianceService extends IService<EcoChainEnterpriseAlliance> {

        void insertEnterpriseAlliance(EcoChainEnterpriseAlliance param);

        List<EcoChainGetEnterpriseBySocialCreditCodeReturnParam> getEnterpriseBySocialCreditCode(
                        String socialCreditCode,
                        String enterpriseName);

        Page<EcoChainEnterpriseAllianceReturnParam> selectPageEnterpriseAlliance(
                        EcoChainEnterpriseAlliancePageSearchParam param);
}
