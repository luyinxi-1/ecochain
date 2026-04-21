package upc.c505.modular.ecochain.service;

import upc.c505.modular.ecochain.controller.param.EcoChainCollectInsertParam;
import upc.c505.modular.ecochain.controller.param.EcoChainCollectSearchParam;
import upc.c505.modular.ecochain.controller.param.EcoChainProductViewNumberParam;
import upc.c505.modular.ecochain.entity.EcoChainCollect;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author byh
 * @since 2025-01-20
 */
public interface IEcoChainCollectService extends IService<EcoChainCollect> {

    Long insertCollect(EcoChainCollectInsertParam param);

    boolean deleteCollect(Long id);

    Long selectCollectNumber(Integer type, String collectId);

    List<Long> selectIsOrNoCollect(Integer type, String collectId);

    List<?> selectCollectInformation(EcoChainCollectSearchParam param);

    EcoChainProductViewNumberParam selectProductNumber(Long id);
}
