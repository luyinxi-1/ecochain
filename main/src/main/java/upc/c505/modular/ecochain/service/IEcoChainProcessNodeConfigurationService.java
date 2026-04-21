package upc.c505.modular.ecochain.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import upc.c505.modular.ecochain.controller.param.EcoChainProcessNodeConfigurationPageSearchParam;
import upc.c505.modular.ecochain.entity.EcoChainProcessNodeConfiguration;
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
public interface IEcoChainProcessNodeConfigurationService extends IService<EcoChainProcessNodeConfiguration> {

    void insertProcessNodeConfiguration(EcoChainProcessNodeConfiguration param);

    boolean updateProcessNodeConfiguration(EcoChainProcessNodeConfiguration param);

    List<EcoChainProcessNodeConfiguration> listBySocialCreditCode(String param);

    Page<EcoChainProcessNodeConfiguration> selectPageProcessNodeConfiguration(EcoChainProcessNodeConfigurationPageSearchParam param);
}
