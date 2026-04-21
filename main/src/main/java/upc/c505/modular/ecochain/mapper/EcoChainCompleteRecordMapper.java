package upc.c505.modular.ecochain.mapper;

import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Select;
import org.springframework.data.repository.query.Param;
import upc.c505.modular.ecochain.entity.EcoChainCompleteRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author xth
 * @since 2024-09-21
 */
@Mapper
public interface EcoChainCompleteRecordMapper extends BaseMapper<EcoChainCompleteRecord> {

    @MapKey("recorder")
    List<Map<String, Object>> countByRecorder(@Param("startTime") String startTime, @Param("endTime") String endTime, @Param("name") String name, @Param("socialCreditCode") String socialCreditCode);

    @MapKey("detailTypeOption")
    List<Map<String, Object>> countByDetailTypeOption(@Param("startTime") String startTime, @Param("endTime") String endTime, @Param("name") String name, @Param("socialCreditCode") String socialCreditCode);

    @MapKey("industryGroupOption")
    List<Map<String, Object>> countByIndustryGroupOption(@Param("startTime") String startTime, @Param("endTime") String endTime, @Param("name") String name, @Param("socialCreditCode") String socialCreditCode);

    @MapKey("industryWarehouse")
    List<Map<String, Object>> countByIndustryWarehouse(@Param("startTime") String startTime, @Param("endTime") String endTime, @Param("name") String name, @Param("socialCreditCode") String socialCreditCode);

}
