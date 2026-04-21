package upc.c505.modular.ecochain.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.RequestParam;
import upc.c505.common.responseparam.R;
import upc.c505.modular.ecochain.controller.param.EcoChainEnterpriseAuthorizePageSearchParam;
import upc.c505.modular.ecochain.controller.param.EcoChainEnterpriseAuthorizeReturnParam;
import upc.c505.modular.ecochain.entity.EcoChainEnterpriseAuthorize;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Collection;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author mjh
 * @since 2024-09-25
 */
public interface IEcoChainEnterpriseAuthorizeService extends IService<EcoChainEnterpriseAuthorize> {
    EcoChainEnterpriseAuthorizeReturnParam selectById(Long id);

    Page<EcoChainEnterpriseAuthorizeReturnParam> selectPage(EcoChainEnterpriseAuthorizePageSearchParam param);

    Boolean updateAuthorize(EcoChainEnterpriseAuthorize param);

    String removeBySocialCreditCodeBatch(List<String> socialCreditCodeList);

    // 构造EcoChainEnterpriseAuthorizeReturnParam新数据
    void constructReturnParam(EcoChainEnterpriseAuthorizeReturnParam returnParam);

    Double queryUsedSize(String creditCode);
}
