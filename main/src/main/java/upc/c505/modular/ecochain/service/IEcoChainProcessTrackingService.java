package upc.c505.modular.ecochain.service;

import org.springframework.web.multipart.MultipartFile;
import upc.c505.modular.ecochain.entity.EcoChainProcessTracking;
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
public interface IEcoChainProcessTrackingService extends IService<EcoChainProcessTracking> {

    boolean insertProcessTrackingConfiguration(EcoChainProcessTracking param);

    List<EcoChainProcessTracking> listByWarehouseId(Long warehouseId);

    boolean updateProcessTrackingConfiguration(EcoChainProcessTracking param);

    void removeBatchProcessTrackingConfiguration(List<Long> idList);
}
