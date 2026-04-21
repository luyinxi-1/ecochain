package upc.c505.modular.villageiot.service;

import upc.c505.modular.villageiot.controller.param.VideoEsurfingDeviceListSearchParam;
import upc.c505.modular.villageiot.controller.param.VideoEsurfingGroupSearchParam;
import upc.c505.modular.villageiot.entity.VideoConfigEsurfing;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author frd
 * @since 2024-03-20
 */
public interface IVideoConfigEsurfingService extends IService<VideoConfigEsurfing> {

    /**
     * 获取当前账号所分配的区域，及其所有下级区域列表
     * @param param 包含areaId三件套，查询host表中天翼设备的id，再根据id查，为空时则返回首层目录树，为空传空字符串
     */
    String getReginWithGroupList(VideoEsurfingGroupSearchParam param);

    /**
     * 获取设备的监控直播地址
     * @param deviceCode 设备码
     * @param proto 直播地址类型 (1.rtsp; 2.rtmp; 3.hls)默认为 3
     * @Param supportDomain 是否获取hpps协议的直播地址（0:不支持;1:支持）默认为0
     */
    String getDeviceMediaUrl(String deviceCode, Integer proto, Integer supportDomain);

    /**
     * 批量更新所有天翼设备在线状态
     * @return
     */
    Integer batchDeviceStatus(Integer commonId);

    /**
     * 查询设备列表
     */
    String getDeviceList(VideoEsurfingDeviceListSearchParam param);
}
