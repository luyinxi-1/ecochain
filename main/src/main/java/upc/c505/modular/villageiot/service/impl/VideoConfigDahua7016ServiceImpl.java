package upc.c505.modular.villageiot.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import upc.c505.common.wrapper.MyLambdaQueryWrapper;
import upc.c505.modular.villageiot.entity.VideoConfigDahua7016;
import upc.c505.modular.villageiot.mapper.VideoConfigDahua7016Mapper;
import upc.c505.modular.villageiot.service.IVideoConfigDahua7016Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author frd
 * @since 2024-01-19
 */
@Service
public class VideoConfigDahua7016ServiceImpl extends ServiceImpl<VideoConfigDahua7016Mapper, VideoConfigDahua7016> implements IVideoConfigDahua7016Service {

    @Autowired
    private VideoConfigDahua7016Mapper videoConfigDahua7016Mapper;
    @Override
    public List<VideoConfigDahua7016> selectDaHuaVideoList(Integer videoCommonId) {
        return videoConfigDahua7016Mapper.selectList(new MyLambdaQueryWrapper<VideoConfigDahua7016>()
                .eq(VideoConfigDahua7016::getVideoConfigCommonId, videoCommonId));
    }
}
