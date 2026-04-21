package upc.c505.modular.villageiot.controller;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import upc.c505.common.requestparam.PageBaseSearchParam;
import upc.c505.common.responseparam.PageBaseReturnParam;
import upc.c505.common.responseparam.R;
import upc.c505.modular.villageiot.controller.param.*;
import upc.c505.modular.villageiot.entity.VideoConfigCommon;
import upc.c505.modular.villageiot.service.IVideoRandomInspectionService;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author frd
 * @since 2024-03-25
 */
@RestController
@Api(tags = {"视频随机巡查"})
@RequestMapping("/video-random-inspection")
public class VideoRandomInspectionController {

    @Autowired
    private IVideoRandomInspectionService videoRandomInspectionService;

    @ApiOperation("视频巡查首页")
    @PostMapping("/getIndex")
    public R<PageBaseReturnParam<VideoConfigCommon>> getIndex(@RequestBody VideoRandomInspectionIndexSearchParam param) {
        Page<VideoConfigCommon> page = videoRandomInspectionService.getIndex(param);
        PageBaseReturnParam<VideoConfigCommon> p = PageBaseReturnParam.ok(page);
        return R.ok(p);
    }

    @ApiOperation("获取海康视频地区摄像头列表")
    @GetMapping("/getPageCamerasByRegions")
    public R<JSONObject> getPageCamerasByRegions(@RequestParam("pageNo") Long pageNo, @RequestParam("regionIndexCode") String regionIndexCode) {
        PageBaseSearchParam pageQuery = new PageBaseSearchParam();
        pageQuery.setSize(10L);
        pageQuery.setCurrent(pageNo);
        JSONObject json = videoRandomInspectionService.getPageCamerasByRegions(pageQuery, regionIndexCode);
        return R.ok(json);
    }

    @ApiOperation("视频巡查")
    @GetMapping("/videoInspection")
    public R<List<VideoInspectionPointLocationInfoParam>> videoInspection(@RequestParam Long id) {
        return R.ok(videoRandomInspectionService.videoInspection(id));
    }

    @ApiOperation("视频通道实况流程-获取视频通道URL")
    @PostMapping("/getVideoChannelUrl")
    public R<JSONObject> getVideoChannelUrl(@RequestBody VideoChannelUrlParam param,
                                            @RequestParam("ChannelsID") Long ChannelsID, @RequestParam("StreamsID") Long StreamsID,
                                            @RequestParam("TransType") Long TransType, @RequestParam("TransProtocol") Long TransProtocol, @RequestParam("areaId") Long areaId) {
        JSONObject result = videoRandomInspectionService.getVideoChannelUrl(param, ChannelsID, StreamsID, TransType, TransProtocol, areaId);
        return R.ok(result);
    }

    @ApiOperation("获取视频在线和离线数量")
    @PostMapping("/getOnlineAndOfflineNumber")
    public R<Map<String, Long>> getOnlineAndOfflineNumber(@RequestBody OnlineAndOfflineSearchParam param) {
        return R.ok(videoRandomInspectionService.getOnlineAndOfflineNumber(param));
    }

    @ApiOperation("根据文字生成图片")
    @PostMapping("/generateImageByString")
    public R<String> generateImageByString(@RequestBody ImageGenerateParam param) throws IOException {
        return R.ok(videoRandomInspectionService.generateImageByString(param));
    }

    @ApiOperation("国标在线状态更新")
    @GetMapping("/refreshGuoBiaoOnlineNumber")
    public R<Boolean> refreshGuoBiaoOnlineNumber(@RequestParam Integer commonId) {
        videoRandomInspectionService.refreshGuoBiaoOnlineNumber(commonId);
        return R.ok();
    }

    @ApiOperation("截图")
    @PostMapping("/cut")
    public R<String> cut(@RequestBody String json) {
        return R.ok(videoRandomInspectionService.cutPicture(json));
    }

    @ApiOperation("分页查询项目远程监督情况")
    @PostMapping("/selectProjectRemoteSupervisionPage")
    public R<PageBaseReturnParam<ProjectRemoteSupervisionReturnParam>> selectProjectRemoteSupervisionPage(@RequestBody ProjectRemoteSupervisionSearchParam param) {
        Page<ProjectRemoteSupervisionReturnParam> p = videoRandomInspectionService.selectProjectRemoteSupervisionPage(param);
        PageBaseReturnParam<ProjectRemoteSupervisionReturnParam> page = PageBaseReturnParam.ok(p);
        return R.ok(page);
    }
}
