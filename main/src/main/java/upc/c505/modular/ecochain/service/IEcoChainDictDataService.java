package upc.c505.modular.ecochain.service;

import upc.c505.common.responseparam.PageBaseReturnParam;
import upc.c505.common.responseparam.R;
import upc.c505.modular.dict.controller.searchParam.SysDictDataSearchParam;
import upc.c505.modular.dict.entity.SysDictData;
import upc.c505.modular.ecochain.controller.param.EcoChainDictDataPageSearchParam;
import upc.c505.modular.ecochain.controller.param.EcoChainIdParam;
import upc.c505.modular.ecochain.entity.EcoChainDictData;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author byh
 * @since 2024-09-23
 */
public interface IEcoChainDictDataService extends IService<EcoChainDictData> {


    R<PageBaseReturnParam<EcoChainDictData>> selectDictDataList(EcoChainDictDataPageSearchParam dictData);

    int insertDictData(EcoChainDictData dict);

    int updateDictData(EcoChainDictData dict);

    void deleteDictDataByIds(EcoChainIdParam idParam);

    List<EcoChainDictData> selectDictDataByDictType(String dictType, Long areaId, String name);

    public String selectDictName(String dictType, String dictKey);
}
