package upc.c505.modular.villageiot.mapper;

import upc.c505.modular.villageiot.entity.VideoConfigEsurfing;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author frd
 * @since 2024-03-20
 */
@Mapper
public interface VideoConfigEsurfingMapper extends BaseMapper<VideoConfigEsurfing> {

    Integer multiUpdateOnlineStatus(List<VideoConfigEsurfing> updateList);
}
