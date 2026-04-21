package upc.c505.modular.ecochain.service;

import upc.c505.modular.ecochain.controller.param.EcoChainProductParameterSearchParam;
import upc.c505.modular.ecochain.entity.EcoChainProductParameter;
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
public interface IEcoChainProductParameterService extends IService<EcoChainProductParameter> {

    void insertProductParameter(EcoChainProductParameter param);

    void removeProductParameter(List<Long> idList);

    Boolean updateProductParameter(EcoChainProductParameter param);

    boolean updateProductParameterStatus(Long id, Integer status);

    boolean updateProductParameterSortNumber(Long id, Integer param);

    List<EcoChainProductParameter> selectProductParameterList(EcoChainProductParameterSearchParam param);

    String selectProductParameterById(Long id);

    List<EcoChainProductParameter> selectEnableProductParameter(EcoChainProductParameterSearchParam param);
}
