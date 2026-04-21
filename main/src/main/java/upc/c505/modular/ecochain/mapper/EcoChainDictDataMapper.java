package upc.c505.modular.ecochain.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import upc.c505.modular.ecochain.controller.param.EcoChainDictDataPageSearchParam;
import upc.c505.modular.ecochain.entity.EcoChainDictData;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import upc.c505.modular.ecochain.entity.EcoChainDictType;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author byh
 * @since 2024-09-23
 */
@Mapper
public interface EcoChainDictDataMapper extends BaseMapper<EcoChainDictData> {

    List<EcoChainDictData> selectDictDataList(IPage<EcoChainDictData> page, EcoChainDictDataPageSearchParam dictData);

    String selectDictName(@Param("dictType") String dictType, @Param("dictKey") String dictKey);

    int insertDictData(@Param("dictData") EcoChainDictData dictData);

    int updateDictData(@Param("dictData") EcoChainDictData dictData);

    int deleteDictDataById(Integer item);

    List<EcoChainDictData> selectByDictType(String dictType, Long areaId, String name);

    void updateDictDataType(@Param("oldDictType") String oldDictType, @Param("newDictType") String newDictType);

    List<EcoChainDictType> selectDictDataByType(@Param("dictType") String dictType);
}
