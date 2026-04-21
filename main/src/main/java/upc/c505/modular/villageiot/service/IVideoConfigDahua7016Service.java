package upc.c505.modular.villageiot.service;

import upc.c505.modular.villageiot.entity.VideoConfigDahua7016;
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
public interface IVideoConfigDahua7016Service extends IService<VideoConfigDahua7016> {

    List<VideoConfigDahua7016> selectDaHuaVideoList(Integer videoCommonId);
}
