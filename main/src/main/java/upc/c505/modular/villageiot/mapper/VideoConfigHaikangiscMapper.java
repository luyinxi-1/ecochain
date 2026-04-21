package upc.c505.modular.villageiot.mapper;

import upc.c505.modular.villageiot.entity.VideoConfigHaikangisc;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author frd
 * @since 2024-01-19
 */
@Mapper
public interface VideoConfigHaikangiscMapper extends BaseMapper<VideoConfigHaikangisc> {

    /**
     * 批量修改摄像头状态
     * @param list
     * @return
     */
    Integer multiUpdateOnlineStatus(List<VideoConfigHaikangisc> list);
}
