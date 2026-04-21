package upc.c505.modular.ecochain.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import upc.c505.modular.ecochain.controller.param.EcoChainTypeConfigurationPageSearchParam;
import upc.c505.modular.ecochain.entity.EcoChainTypeConfiguration;
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
public interface IEcoChainTypeConfigurationService extends IService<EcoChainTypeConfiguration> {

    boolean insertSupEnterprise(EcoChainTypeConfiguration param);

    Page<EcoChainTypeConfiguration> selectPageTypeConfiguration(EcoChainTypeConfigurationPageSearchParam param);

    List<EcoChainTypeConfiguration> listBySocialCreditCode(String param);

    boolean updateTypeConfiguration(EcoChainTypeConfiguration param);

    void removeBatchTypeConfiguration(List<Long> idList);
}
