package upc.c505.modular.villageiot.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import upc.c505.common.wrapper.MyLambdaQueryWrapper;
import upc.c505.modular.villageiot.entity.VideoConfigHaikangisc;
import upc.c505.modular.villageiot.mapper.VideoConfigHaikangiscMapper;
import upc.c505.modular.villageiot.service.IVideoConfigHaikangiscService;
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
public class VideoConfigHaikangiscServiceImpl extends ServiceImpl<VideoConfigHaikangiscMapper, VideoConfigHaikangisc> implements IVideoConfigHaikangiscService {

    @Autowired
    private VideoConfigHaikangiscMapper videoConfigHaikangiscMapper;
    @Override
    public List<VideoConfigHaikangisc> selectHaiKangVideoList(Integer videoCommonId) {
        return videoConfigHaikangiscMapper.selectList(new MyLambdaQueryWrapper<VideoConfigHaikangisc>()
                .eq(VideoConfigHaikangisc::getVideoConfigCommonId,videoCommonId));
    }
}
