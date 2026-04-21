package upc.c505.modular.ecochain.mapper;

import upc.c505.modular.ecochain.entity.EcoChainMainPromotionalImage;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author byh
 * @since 2024-09-28
 */
@Mapper
public interface EcoChainMainPromotionalImageMapper extends BaseMapper<EcoChainMainPromotionalImage> {

    boolean updateMainPromotionalImage(EcoChainMainPromotionalImage param);

    Integer selectMaxRotationSequence(EcoChainMainPromotionalImage param);
}
