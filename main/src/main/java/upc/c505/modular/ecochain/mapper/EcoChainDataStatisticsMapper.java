package upc.c505.modular.ecochain.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import upc.c505.modular.ecochain.entity.EcoChainDataStatistics;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
//import upc.c505.modular.ecochain.service.impl.EcoChainDataStatisticsServiceImpl.StatTypeCount;
//import upc.c505.modular.ecochain.service.impl.EcoChainDataStatisticsServiceImpl.StatTypeHourCount;
//import upc.c505.modular.ecochain.service.impl.EcoChainDataStatisticsServiceImpl.StatTypeDayCount;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author la
 * @since 2024-09-26
 */
@Mapper
public interface EcoChainDataStatisticsMapper extends BaseMapper<EcoChainDataStatistics> {


}
