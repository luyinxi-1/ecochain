package upc.c505.modular.ecochain.mapper;

import upc.c505.common.responseparam.R;
import upc.c505.modular.ecochain.entity.EcoChainProductParameter;
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
public interface EcoChainProductParameterMapper extends BaseMapper<EcoChainProductParameter> {

    Integer selectMaxSortNumber(EcoChainProductParameter param);


    Boolean updateProductParameter(EcoChainProductParameter param);
}
