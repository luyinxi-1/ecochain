package upc.c505.modular.villageiot.service;

import upc.c505.modular.villageiot.entity.VideoConfigHaikangisc;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author frd
 * @since 2024-01-19
 */
public interface IVideoConfigHaikangiscService extends IService<VideoConfigHaikangisc> {

    List<VideoConfigHaikangisc> selectHaiKangVideoList(Integer videoCommonId);
}
