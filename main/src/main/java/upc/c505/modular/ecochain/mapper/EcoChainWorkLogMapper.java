package upc.c505.modular.ecochain.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import upc.c505.modular.ecochain.controller.param.EcoChainWorkLogGetByIdReturnParam;
import upc.c505.modular.ecochain.controller.param.EcoChainWorkLogParam.ExportWorkLogReturnParam;
import upc.c505.modular.ecochain.controller.param.EcoChainWorkLogParam.SelectWorkLogReturnParam;
import upc.c505.modular.ecochain.controller.param.EcoChainWorkLogParam.SelectWorkLogSearchParam;
import upc.c505.modular.ecochain.entity.EcoChainWorkLog;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author byh
 * @since 2025-04-16
 */
@Mapper
public interface EcoChainWorkLogMapper extends BaseMapper<EcoChainWorkLog> {
    List<SelectWorkLogReturnParam> selectWorkLog(@Param("searchParam") SelectWorkLogSearchParam searchParam);

    List<SelectWorkLogReturnParam> selectCompleteWorkLog(@Param("searchParam") SelectWorkLogSearchParam searchParam);

    List<SelectWorkLogReturnParam> selectProcessWorkLog(@Param("searchParam") SelectWorkLogSearchParam searchParam);

    List<ExportWorkLogReturnParam> exportWorkLog(@Param("searchParam") SelectWorkLogSearchParam searchParam);

    List<ExportWorkLogReturnParam> exportWorkLogCompleteRecord(@Param("searchParam") SelectWorkLogSearchParam searchParam);

    List<ExportWorkLogReturnParam> exportWorkLogProcessTracking(@Param("searchParam") SelectWorkLogSearchParam searchParam);

    EcoChainWorkLogGetByIdReturnParam getDetailsByProcessId(Long id);

    EcoChainWorkLogGetByIdReturnParam getDetailsByCompleteId(Long id);
}
