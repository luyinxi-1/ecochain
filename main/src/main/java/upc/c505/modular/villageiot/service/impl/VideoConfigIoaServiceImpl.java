package upc.c505.modular.villageiot.service.impl;

import cn.hutool.core.codec.Base64;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import upc.c505.modular.villageiot.entity.VideoConfigIoa;
import upc.c505.modular.villageiot.mapper.VideoConfigIoaMapper;
import upc.c505.modular.villageiot.service.IVideoConfigIoaService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author frd
 * @since 2024-03-20
 */
@Service
@Slf4j
public class VideoConfigIoaServiceImpl extends ServiceImpl<VideoConfigIoaMapper, VideoConfigIoa> implements IVideoConfigIoaService {

    /**
     * 请求的ip和端口号
     */
    public static final String URL_PREFIX = "http://116.112.47.182:9840/apiserver/v1";
    /**
     * 获取实时播放地址
     */
    public static final String GET_STREAM = "/device/video/preview";
    /**
     * 获取token的路径
     */
    private final String TOKEN_PATH = "/user/authentication-token";

    public String getToken() {
        String authorization = Base64.encode(("qdyl" + ":" + "qdyl@2022").getBytes(StandardCharsets.UTF_8));
        String token;
        try (HttpResponse response = HttpUtil.createGet(URL_PREFIX + TOKEN_PATH)
                .header("Authorization", "Basic " + authorization)
                .execute()) {
            token = response.body();
        }
        log.info("token为:{}", token);
        return token;
    }
}
