package upc.c505.modular.villageiot.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import upc.c505.common.responseparam.R;
import upc.c505.modular.villageiot.entity.VideoConfigDahua7016;
import upc.c505.modular.villageiot.entity.VideoConfigHaikangisc;
import upc.c505.modular.villageiot.service.IVideoConfigDahua7016Service;
import upc.c505.modular.villageiot.service.IVideoConfigHaikangiscService;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author frd
 * @since 2024-01-19
 */
@RestController
@RequestMapping("/video-config-dahua7016")
@Api(tags = {"大华摄像头配置管理"})
public class VideoConfigDahua7016Controller {

    @Autowired
    private IVideoConfigDahua7016Service videoConfigDahua7016Service;

    @ApiOperation("新增大华设备")
    @PostMapping("/addDaHuaVideo")
    public R<Boolean> addHaiKangVideo(@RequestBody VideoConfigDahua7016 videoConfigDahua7016){
        return R.ok(videoConfigDahua7016Service.save(videoConfigDahua7016));
    }

    @ApiOperation("删除大华设备")
    @PostMapping("/deleteDaHuaVideo")
    public R<Boolean> deleteHaiKangVideo(@RequestParam Integer id){
        return R.ok(videoConfigDahua7016Service.removeById(id));
    }

    @ApiOperation("修改大华设备")
    @PostMapping("/updateDaHuaVideo")
    public R<Boolean> updateHaiKangVideo(@RequestBody VideoConfigDahua7016 videoConfigDahua7016){
        return R.ok(videoConfigDahua7016Service.updateById(videoConfigDahua7016));
    }

    @ApiOperation("根据commonId查看大华设备列表")
    @PostMapping("/selectDaHuaVideoList")
    public R<List<VideoConfigDahua7016>> selectDaHuaVideoList(@RequestParam Integer videoCommonId) {
        return R.ok(videoConfigDahua7016Service.selectDaHuaVideoList(videoCommonId));
    }
}
