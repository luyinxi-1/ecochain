package upc.c505.modular.ecochain.mapper;

import upc.c505.modular.ecochain.entity.EcoChainProductParameterValueContent;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author byh
 * @since 2024-09-25
 */
@Mapper
public interface EcoChainProductParameterValueContentMapper extends BaseMapper<EcoChainProductParameterValueContent> {

    Integer selectMaxSortNumber(EcoChainProductParameterValueContent param);

    boolean updateProductParameterValueContent(EcoChainProductParameterValueContent param);
}
