package upc.c505.modular.ecochain.service;

import cn.hutool.core.lang.Dict;
import upc.c505.modular.ecochain.controller.param.EcoChainProductClassificationSearchParam;
import upc.c505.modular.ecochain.controller.param.EcoChainTopLevelProductClassificationSearchParam;
import upc.c505.modular.ecochain.entity.EcoChainProductClassification;
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
public interface IEcoChainProductClassificationService extends IService<EcoChainProductClassification> {

    void insertProductClassification(EcoChainProductClassification param);


    boolean updateProductClassification(EcoChainProductClassification param);

    List<EcoChainProductClassification> selectProductClassificationParentIdList(Integer classificationGrade);

    void removeProductClassification(List<Long> idList);

    List<EcoChainProductClassification> selectProductClassificationDownList(Long id);

    List<EcoChainProductClassification> selectProductClassificationList(EcoChainProductClassificationSearchParam param);

    List<EcoChainProductClassification> buildDictTree(List<EcoChainProductClassification> list);

    boolean updateProductClassificationSortName(Long id, Integer param);

    List<EcoChainProductClassification> selectTopLevelProductClassification(EcoChainTopLevelProductClassificationSearchParam param);
}
