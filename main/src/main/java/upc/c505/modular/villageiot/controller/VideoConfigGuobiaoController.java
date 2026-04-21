package upc.c505.modular.villageiot.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import upc.c505.common.responseparam.R;
import upc.c505.modular.villageiot.controller.param.GuoBiaoDevice;
import upc.c505.modular.villageiot.service.IVideoConfigGuobiaoService;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author frd
 * @since 2024-06-24
 */
@RestController
@RequestMapping("/video-config-guobiao")
@Api(tags = {"国标摄像头配置管理"})
public class VideoConfigGuobiaoController {
    @Autowired
    private IVideoConfigGuobiaoService videoConfigGuobiaoService;
    @ApiOperation("获取视频流点播接口")
    @PostMapping("/getFmp4")
    public R<String> getFmp4(@RequestBody GuoBiaoDevice guoBiaoDevice) {
        String data = videoConfigGuobiaoService.getFmp4(guoBiaoDevice);
        return R.ok(data);
    }
    @ApiOperation("查询设备状态")
    @GetMapping("/getDeviceState")
    public R<String> getDeviceState(@RequestParam String deviceId,
                               @ApiParam("页码")@RequestParam Integer pageNo,
                               @ApiParam("分页大小")@RequestParam Integer pageSize) {
        String data = videoConfigGuobiaoService.getDeviceState(deviceId,pageNo,pageSize);
        return R.ok(data);
    }


}
