package upc.c505.modular.villageiot.service;

import upc.c505.modular.villageiot.controller.param.GuoBiaoDevice;
import upc.c505.modular.villageiot.entity.VideoConfigGuobiao;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author frd
 * @since 2024-03-20
 */
public interface IVideoConfigGuobiaoService extends IService<VideoConfigGuobiao> {

    String getFmp4(GuoBiaoDevice guoBiaoDevice);

    String getDeviceState(String deviceId, Integer pageNo, Integer pageSize);
}
