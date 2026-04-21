package upc.c505.modular.ecochain.mapper;

import upc.c505.modular.ecochain.controller.param.EcoChainBuildWareHouseFinishParam;
import upc.c505.modular.ecochain.entity.EcoChainBuildWarehouse;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDateTime;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author xth
 * @since 2024-09-21
 */
@Mapper
public interface EcoChainBuildWarehouseMapper extends BaseMapper<EcoChainBuildWarehouse> {

    void updateStatusById(Long warehouseId, String status, LocalDateTime now, String userName);

    void updateByWarehouseId(EcoChainBuildWarehouse param);

    void finishWarehouse(EcoChainBuildWareHouseFinishParam param);

    void updateAllCompletionDatetime( Long id,  LocalDateTime allCompletionDatetime);
}
