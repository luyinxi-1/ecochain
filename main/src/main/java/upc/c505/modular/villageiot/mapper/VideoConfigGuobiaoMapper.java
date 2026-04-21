package upc.c505.modular.villageiot.mapper;

import upc.c505.modular.villageiot.entity.VideoConfigGuobiao;
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
public interface VideoConfigGuobiaoMapper extends BaseMapper<VideoConfigGuobiao> {

    void MultiUpdateOnlineStatus(List<VideoConfigGuobiao> addList);
}
