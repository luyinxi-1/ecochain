package upc.c505.modular.ecochain.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import upc.c505.modular.dict.entity.SysDictType;
import upc.c505.modular.ecochain.controller.param.EcoChainDictTypePageSearchParam;
import upc.c505.modular.ecochain.entity.EcoChainDictType;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

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
public interface EcoChainDictTypeMapper extends BaseMapper<EcoChainDictType> {

    List<EcoChainDictType> selectDictTypeList(IPage<EcoChainDictType> page, @Param("dictType") EcoChainDictTypePageSearchParam dictType);

    EcoChainDictType checkDictTypeUnique(@Param("dictType") String dictType);

    int insertDictType(@Param("dictType") EcoChainDictType dict);

    EcoChainDictType selectDictTypeById(@Param("dictId") Integer dictId);

    int updateDictType(@Param("dictType") EcoChainDictType dictType);

    void deleteDictTypeById(Integer dictId);
}
