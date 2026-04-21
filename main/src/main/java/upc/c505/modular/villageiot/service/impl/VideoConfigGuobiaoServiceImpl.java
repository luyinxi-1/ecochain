package upc.c505.modular.villageiot.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import upc.c505.modular.villageiot.controller.param.GuoBiaoDevice;
import upc.c505.modular.villageiot.entity.VideoConfigGuobiao;
import upc.c505.modular.villageiot.mapper.VideoConfigGuobiaoMapper;
import upc.c505.modular.villageiot.service.IVideoConfigGuobiaoService;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author frd
 * @since 2024-03-20
 */
@Service
public class VideoConfigGuobiaoServiceImpl extends ServiceImpl<VideoConfigGuobiaoMapper, VideoConfigGuobiao> implements IVideoConfigGuobiaoService {

    @Autowired
    private VideoConfigGuobiaoMapper videoConfigGuobiaoMapper;

    @Autowired
    private IVideoConfigGuobiaoService videoConfigGuobiaoService;
    @Autowired
    private RestTemplate restTemplate;

    @Override
    public String getFmp4(GuoBiaoDevice guoBiaoDevice) {
        String url = "http://112.6.123.213:8088/api/play/start/" + guoBiaoDevice.getDeviceId() + "/" + guoBiaoDevice.getChannelId();
        ResponseEntity<String> forEntity = restTemplate.getForEntity(url, String.class);
        String body = forEntity.getBody();
        return body;
    }

    @Override
    public String getDeviceState(String deviceId, Integer pageNo, Integer pageSize) {
        String url = "http://112.6.123.213:8088/api/device/query/devices/" + deviceId + "/channels?page=" + pageNo + "&count=" + pageSize;
        ResponseEntity<String> forEntity = restTemplate.getForEntity(url, String.class);
        String body = forEntity.getBody();
        return body;
    }
}
