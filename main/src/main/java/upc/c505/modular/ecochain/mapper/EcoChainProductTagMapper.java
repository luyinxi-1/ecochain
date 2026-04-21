package upc.c505.modular.ecochain.mapper;

import upc.c505.modular.ecochain.entity.EcoChainProductTag;
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
public interface EcoChainProductTagMapper extends BaseMapper<EcoChainProductTag> {

    Integer selectMaxSortNumber(EcoChainProductTag param);

    boolean updateProductTag(EcoChainProductTag param);
}
