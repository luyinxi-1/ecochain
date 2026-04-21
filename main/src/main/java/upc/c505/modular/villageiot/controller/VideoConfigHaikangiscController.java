package upc.c505.modular.villageiot.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import upc.c505.common.responseparam.R;
import upc.c505.modular.villageiot.entity.VideoConfigHaikangisc;
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
@RequestMapping("/video-config-haikangisc")
@Api(tags = {"海康摄像头配置管理"})
public class VideoConfigHaikangiscController {

    @Autowired
    private IVideoConfigHaikangiscService videoConfigHaikangiscService;

    @ApiOperation("新增海康设备")
    @PostMapping("/addHaiKangVideo")
    public R<Boolean> addHaiKangVideo(@RequestBody VideoConfigHaikangisc videoConfigHaikangisc){
        return R.ok(videoConfigHaikangiscService.save(videoConfigHaikangisc));
    }

    @ApiOperation("删除海康设备")
    @PostMapping("/deleteHaiKangVideo")
    public R<Boolean> deleteHaiKangVideo(@RequestParam Integer id){
        return R.ok(videoConfigHaikangiscService.removeById(id));
    }

    @ApiOperation("修改海康设备")
    @PostMapping("/updateHaiKangVideo")
    public R<Boolean> updateHaiKangVideo(@RequestBody VideoConfigHaikangisc videoConfigHaikangisc){
        return R.ok(videoConfigHaikangiscService.updateById(videoConfigHaikangisc));
    }

    @ApiOperation("根据commonId查看海康设备列表")
    @PostMapping("/selectHaiKangVideoList")
    public R<List<VideoConfigHaikangisc>> selectHaiKangVideoList(@RequestParam Integer videoCommonId) {
        return R.ok(videoConfigHaikangiscService.selectHaiKangVideoList(videoCommonId));
    }
}
