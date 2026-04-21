package upc.c505.modular.ecochain.service;

import upc.c505.common.responseparam.PageBaseReturnParam;
import upc.c505.common.responseparam.R;
import upc.c505.modular.dict.controller.searchParam.IdParam;
import upc.c505.modular.dict.entity.SysDictType;
import upc.c505.modular.ecochain.controller.param.EcoChainDictTypePageSearchParam;
import upc.c505.modular.ecochain.controller.param.EcoChainIdParam;
import upc.c505.modular.ecochain.entity.EcoChainDictType;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author byh
 * @since 2024-09-23
 */
public interface IEcoChainDictTypeService extends IService<EcoChainDictType> {

    R<PageBaseReturnParam<EcoChainDictType>> selectDictTypeList(EcoChainDictTypePageSearchParam dictType);

    String checkDictTypeUnique(EcoChainDictType dict);

    int insertDictType(EcoChainDictType dict);

    int updateDictType(EcoChainDictType dict);

    void deleteDictTypeByIds(EcoChainIdParam idParam);

    EcoChainDictType selectDictTypeById(Integer dictId);
}
