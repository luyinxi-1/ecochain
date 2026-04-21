package upc.c505.modular.ecochain.service;

import upc.c505.modular.ecochain.controller.param.EcoChainMainPromotionalImageSearchParam;
import upc.c505.modular.ecochain.entity.EcoChainMainPromotionalImage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author byh
 * @since 2024-09-27
 */
public interface IEcoChainMainPromotionalImageService extends IService<EcoChainMainPromotionalImage> {

    void insertMainPromotionalImage(EcoChainMainPromotionalImage param);

    boolean updateMainPromotionalImage(EcoChainMainPromotionalImage param);

    List<EcoChainMainPromotionalImage> selectMainPromotionalImage(EcoChainMainPromotionalImageSearchParam param);

    List<EcoChainMainPromotionalImage> selectEnableMainPromotionalImage(EcoChainMainPromotionalImageSearchParam param);

    boolean updateMainPromotionalImageStatus(Long id, Integer status);

    boolean removeMainPromotionalImage(List<Long> idList);
}
