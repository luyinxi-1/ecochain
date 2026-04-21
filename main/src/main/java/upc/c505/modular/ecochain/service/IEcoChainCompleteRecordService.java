package upc.c505.modular.ecochain.service;

import upc.c505.modular.ecochain.controller.param.EcoChainBuildWareHouseFinishParam;
import upc.c505.modular.ecochain.entity.EcoChainCompleteRecord;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xth
 * @since 2024-09-21
 */
public interface IEcoChainCompleteRecordService extends IService<EcoChainCompleteRecord> {

    void insertCompleteRecord(EcoChainCompleteRecord param);

    List<EcoChainCompleteRecord> listByWarehouseId(Long warehouseId);

    void finishWarehouse(EcoChainBuildWareHouseFinishParam param);

    boolean updateCompleteRecord(EcoChainCompleteRecord param);

    void removeCompleteRecord(List<Long> idList);
}
