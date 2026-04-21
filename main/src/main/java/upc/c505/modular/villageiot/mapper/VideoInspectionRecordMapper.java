package upc.c505.modular.villageiot.mapper;

import org.apache.ibatis.annotations.Param;
import upc.c505.modular.villageiot.controller.param.CountRemoteSupervisionReturnParam;
import upc.c505.modular.villageiot.entity.VideoInspectionRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author frd
 * @since 2024-06-28
 */
@Mapper
public interface VideoInspectionRecordMapper extends BaseMapper<VideoInspectionRecord> {

    /**
     * 统计项目的远程督导数量
     * @param project
     * @param projectNumberList
     * @return
     */
    List<CountRemoteSupervisionReturnParam> countRemoteSupervision(@Param("project") String project,
                                                                   @Param("projectNumberList") List<String> projectNumberList);
}
