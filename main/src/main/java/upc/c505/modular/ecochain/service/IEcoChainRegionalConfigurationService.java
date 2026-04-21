package upc.c505.modular.ecochain.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import upc.c505.modular.ecochain.controller.param.EcoChainRegionalConfigurationPageSearchParam;
import upc.c505.modular.ecochain.entity.EcoChainRegionalConfiguration;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xth
 * @since 2024-09-21
 */
public interface IEcoChainRegionalConfigurationService extends IService<EcoChainRegionalConfiguration> {

    boolean insertRegionalConfiguration(EcoChainRegionalConfiguration param);

    List<EcoChainRegionalConfiguration> listBySocialCreditCode(String param);

    Page<EcoChainRegionalConfiguration> selectPageRegionalConfiguration(EcoChainRegionalConfigurationPageSearchParam param);

    boolean updateRegionalConfiguration(EcoChainRegionalConfiguration param);

    void removeBatchRegionalConfiguration(List<Long> idList);
}
