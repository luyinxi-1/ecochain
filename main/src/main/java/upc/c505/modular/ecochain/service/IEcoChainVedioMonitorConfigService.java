package upc.c505.modular.ecochain.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import upc.c505.modular.ecochain.controller.param.EcoChainVedioMonitorConfigPageSearchParam;
import upc.c505.modular.ecochain.entity.EcoChainVedioMonitorConfig;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author mjh
 * @since 2024-09-23
 */
public interface IEcoChainVedioMonitorConfigService extends IService<EcoChainVedioMonitorConfig> {

    String insertOrUpdateConfig(EcoChainVedioMonitorConfig param);

    IPage<EcoChainVedioMonitorConfig> selectPage(EcoChainVedioMonitorConfigPageSearchParam param);
}
