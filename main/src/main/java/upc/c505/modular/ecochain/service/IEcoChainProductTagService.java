package upc.c505.modular.ecochain.service;

import upc.c505.modular.ecochain.controller.param.EcoChainProductTagSearchParam;
import upc.c505.modular.ecochain.entity.EcoChainProductTag;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author byh
 * @since 2024-09-25
 */
public interface IEcoChainProductTagService extends IService<EcoChainProductTag> {

    void insertProductTag(EcoChainProductTag param);

    boolean updateProductTagStatus(Long id, Integer status);

    boolean updateProductTag(EcoChainProductTag param);

    boolean updateProductTagSortNumber(Long id, Integer param);

    List<EcoChainProductTag> selectProductTagList(EcoChainProductTagSearchParam param);

    List<EcoChainProductTag> selectEnableProductTagList(EcoChainProductTagSearchParam param);

    void removeProductTag(List<Long> idList);
}
