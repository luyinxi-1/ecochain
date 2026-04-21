package upc.c505.modular.villageiot.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import upc.c505.common.responseparam.R;
import upc.c505.modular.villageiot.controller.param.VideoEsurfingDeviceListSearchParam;
import upc.c505.modular.villageiot.controller.param.VideoEsurfingGroupSearchParam;
import upc.c505.modular.villageiot.service.IVideoConfigEsurfingService;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author frd
 * @since 2024-06-04
 */
@RestController
@RequestMapping("/video-config-esurfing")
@Api(tags = {"天翼摄像头配置管理"})
public class VideoConfigEsurfingController {

    @Autowired
    private IVideoConfigEsurfingService videoConfigEsurfingService;
    @ApiOperation("获取设备的监控直播地址")
    @GetMapping("/getDeviceMediaUrl")
    public R<String> getDeviceMediaUrl(@ApiParam("设备码") @RequestParam String deviceCode,
                                       @ApiParam("直播地址类型 (1.rtsp; 2.rtmp; 3.hls)默认3") @RequestParam(required = false) Integer proto) {
        //2024.4.17新增要求：getDeviceMediaUrl的supportDomain字段直接传1
        return R.ok(videoConfigEsurfingService.getDeviceMediaUrl(deviceCode, proto, 1));
    }

    /**
     * 这个接口最后写，对面的问题对面解决
     * @param param
     */
    @ApiOperation("获取天翼视频当前账号所分配的区域，及其所有下级区域列表")
    @PostMapping("/getReginWithGroupList")
    public void getReginWithGroupList(@ApiParam("区域 id，为空时则返回首层目录树，为空传空字符串")
                                      @RequestBody VideoEsurfingGroupSearchParam param) {
        R.ok(videoConfigEsurfingService.getReginWithGroupList(param));
    }

    @ApiOperation("查询设备列表")
    @PostMapping("/getDeviceList")
    public R<String> getDeviceList(@RequestBody VideoEsurfingDeviceListSearchParam param) {
        return R.ok(videoConfigEsurfingService.getDeviceList(param));
    }
    @ApiOperation("批量查询设备状态")
    @GetMapping("/batchDeviceStatus")
    public R<Integer> batchDeviceStatus(@RequestParam Integer commonId)
    {
        Integer integer = videoConfigEsurfingService.batchDeviceStatus(commonId);
        return R.ok(integer);
    }
}
