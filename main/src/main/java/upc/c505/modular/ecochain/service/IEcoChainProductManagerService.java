package upc.c505.modular.ecochain.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import upc.c505.modular.ecochain.controller.param.*;
import upc.c505.modular.ecochain.entity.EcoChainProductManager;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author byh
 * @since 2024-09-25
 */
public interface IEcoChainProductManagerService extends IService<EcoChainProductManager> {

    boolean insertProduct(EcoChainInsertAndUpdateProductParam param);

    Boolean updateProduct(EcoChainInsertAndUpdateProductParam param);

    void removeProductBatch(List<Long> param);

    Page<EcoChainProductManager> selectPageProduct(EcoChainProductManagerPageSearchParam param);

    List<EcoChainProductManager> selectProductByClassificationId(Long classificationId, String socialCreditCode,
            String productName, String type, Integer flag, Integer IsAsc);

    List<EcoChainSelectProductReturnParam> selectProduct(EcoChainSelectProductMapSearchParam param);

    Boolean updateAllProduct(EcoChainProductAllUpdate param);
}
