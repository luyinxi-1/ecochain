package upc.c505.modular.ecochain.service;

import upc.c505.modular.ecochain.controller.param.EcoChainExtendedTableUpdateParam;
import upc.c505.modular.ecochain.entity.EcoChainExtendedTable;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author la
 * @since 2024-09-24
 */
public interface IEcoChainExtendedTableService extends IService<EcoChainExtendedTable> {


    Integer insertExtendedTable(EcoChainExtendedTableUpdateParam ecoChainExtendedTable);

    Integer updateExtendedTable(EcoChainExtendedTableUpdateParam param);

    EcoChainExtendedTable selectExtendedTable(EcoChainExtendedTable ecoChainExtendedTable);
}
