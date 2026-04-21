package upc.c505.modular.ecochain.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import upc.c505.modular.ecochain.controller.param.EcoChainInsertAndUpdatePromotionalVideo;
import upc.c505.modular.ecochain.controller.param.EcoChainPromotionalVideoSearchParam;
import upc.c505.modular.ecochain.entity.EcoChainPromotionalVideo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xth
 * @since 2025-02-15
 */
public interface IEcoChainPromotionalVideoService extends IService<EcoChainPromotionalVideo> {

    void insertPromotionalVideo(EcoChainInsertAndUpdatePromotionalVideo param);

    Page<EcoChainPromotionalVideo> selectPromotionalVideo(EcoChainPromotionalVideoSearchParam param);

    void updatePromotionalVideo(EcoChainInsertAndUpdatePromotionalVideo param);

    void deletePromotionalVideo(Long id);

    EcoChainPromotionalVideo selectPromotionalVideoById(Long id);

    Page<EcoChainPromotionalVideo> selectPublishedPromotionalVideo(EcoChainPromotionalVideoSearchParam param);
}
