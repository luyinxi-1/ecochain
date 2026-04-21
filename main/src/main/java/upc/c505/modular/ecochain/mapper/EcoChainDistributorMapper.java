package upc.c505.modular.ecochain.mapper;

import org.apache.ibatis.annotations.Param;
import upc.c505.modular.ecochain.controller.param.EcoChainDistributorListParam;
import upc.c505.modular.ecochain.controller.param.EcoChainDistributorPageSearchParam;
import upc.c505.modular.ecochain.entity.EcoChainDistributor;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author byh
 * @since 2024-11-14
 */
@Mapper
public interface EcoChainDistributorMapper extends BaseMapper<EcoChainDistributor> {

    List<EcoChainDistributorListParam> selectDistributorsList(@Param("params") EcoChainDistributorPageSearchParam params);
}
