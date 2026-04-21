package upc.c505.modular.ecochain.service;

import upc.c505.modular.ecochain.entity.EcoChainProductParameterValueContent;
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
public interface IEcoChainProductParameterValueContentService extends IService<EcoChainProductParameterValueContent> {

    void insertProductParameterValueContent(EcoChainProductParameterValueContent param);

    boolean updateProductParameterValueContent(EcoChainProductParameterValueContent param);

    List<EcoChainProductParameterValueContent> selectProductParameterValueContentList(Long productParameterId);

    void removeProductParameterValueContent(List<Long> idList);

    boolean updateProductParameterSortNumber(Long id, Integer param);
}
