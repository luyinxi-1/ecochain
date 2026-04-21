package upc.c505.modular.ecochain.service;

import upc.c505.modular.ecochain.entity.EcoChainHistoryList;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xth
 * @since 2024-09-21
 */
public interface IEcoChainHistoryListService extends IService<EcoChainHistoryList> {

    void insertHistoryList(EcoChainHistoryList param);

    Integer countCompleteRecord(Long warehouseId);
}
