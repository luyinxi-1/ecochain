package upc.c505.modular.ecochain.mapper;

import org.apache.ibatis.annotations.Param;
import upc.c505.modular.ecochain.controller.param.EcoChainCollectEnterpriseParam;
import upc.c505.modular.ecochain.controller.param.EcoChainCollectPeopleParam;
import upc.c505.modular.ecochain.controller.param.EcoChainCollectProductParam;
import upc.c505.modular.ecochain.entity.EcoChainCollect;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author byh
 * @since 2025-01-20
 */
@Mapper
public interface EcoChainCollectMapper extends BaseMapper<EcoChainCollect> {

    List<EcoChainCollectEnterpriseParam> selectCollectEnterprise(@Param("name") String name, @Param("collectId") String collectId);

    List<EcoChainCollectProductParam> selectCollectProduct(@Param("collectId") String collectId, @Param("name") String name);

    List<EcoChainCollectPeopleParam> selectCollectPeople(@Param("collectId") String collectId, @Param("name") String name);
}
