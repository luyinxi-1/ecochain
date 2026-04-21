package upc.c505.modular.ecochain.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.multipart.MultipartFile;
import upc.c505.modular.ecochain.controller.param.EcoChainBuildWareHousePageSearchParam;
import upc.c505.modular.ecochain.controller.param.EcoChainBuildWarehouseReturnParam;
import upc.c505.modular.ecochain.entity.EcoChainBuildWarehouse;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.IOException;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xth
 * @since 2024-09-21
 */
public interface IEcoChainBuildWarehouseService extends IService<EcoChainBuildWarehouse> {

    void insertBuildWarehouse(EcoChainBuildWarehouse param);

    Page<EcoChainBuildWarehouseReturnParam> selectPageBuildWarehouse(EcoChainBuildWareHousePageSearchParam param);

    void removeBatchByIdList(List<Long> idList);

    Integer getPeriodByBuildWarehouseId(Long buildWarehouseId);

    boolean updateByWarehouseId(EcoChainBuildWarehouse param);

    EcoChainBuildWarehouseReturnParam selectBuildWarehouse(Long id);

    /**
     * 导入生态链建库数据
     * @param file 导入文件
     * @return 导入数量
     * @throws IOException IO 异常
     */
    Integer importBuildWarehouseData(MultipartFile file) throws IOException;

    /**
     * 批量修改库的可见人
     * @param warehouseIds 库 IDs
     * @param visiblePeopleIds 可见人 IDs
     * @return 是否成功
     */
    boolean batchUpdateVisiblePeople(List<Long> warehouseIds, List<Long> visiblePeopleIds);
}
