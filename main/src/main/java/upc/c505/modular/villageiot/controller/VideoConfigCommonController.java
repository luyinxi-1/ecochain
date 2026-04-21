package upc.c505.modular.villageiot.controller;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import upc.c505.common.requestparam.PageBaseSearchParam;
import upc.c505.common.responseparam.PageBaseReturnParam;
import upc.c505.common.responseparam.R;
import upc.c505.modular.villageiot.controller.param.*;
import upc.c505.modular.villageiot.entity.VideoConfigCommon;
import upc.c505.modular.villageiot.service.IVideoConfigCommonService;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author frd
 * @since 2024-01-19
 */
@RestController
@RequestMapping("/video-config-common")
@Api(tags = {"视频配置管理"})
public class VideoConfigCommonController {

    @Autowired
    private IVideoConfigCommonService videoConfigCommonService;

    @ApiOperation("获取海康视频地区列表")
    @PostMapping("/getHikVedioList")
    public R<JSONObject> getHikVedioList(@RequestBody PageBaseSearchParam pageQuery) {
        JSONObject json = videoConfigCommonService.getHikVedioList(pageQuery);
        return R.ok(json);
    }

    @ApiOperation("新增视频配置")
    @PostMapping("/addOneVideoCommon")
    public R<Boolean> addOneVideoCommon(@RequestBody VideoConfigAddParam videoConfigAddParam) {
        return R.ok(videoConfigCommonService.addOneVideoCommon(videoConfigAddParam));
    }

    @ApiOperation("新增视频配置时分页查询视频单位")
    @PostMapping("/selectVideoUnitPage")
    public R<PageBaseReturnParam<VideoUnitReturnParam>> selectVideoUnitPage(@RequestBody VideoUnitSearchParam param) {
        Page<VideoUnitReturnParam> page = videoConfigCommonService.selectVideoUnitPage(param);
        PageBaseReturnParam<VideoUnitReturnParam> p = PageBaseReturnParam.ok(page);
        return R.ok(p);
    }

//    @ApiOperation("删除单条视频配置信息及其关联的摄像头信息")
//    @PostMapping("/deleteVideoConfig")
//    public R<Boolean> deleteVideoConfig(@RequestBody VideoConfigDeleteParam videoConfigDeleteParam) {
//        videoConfigCommonService.deleteVideoConfigById(videoConfigDeleteParam);
//        return R.ok();
//    }

    @ApiOperation("删除单条视频配置信息及其关联的摄像头信息")
    @GetMapping("/deleteVideoConfig")
    public R<Boolean> deleteVideoConfig(@RequestParam Integer id) {
        videoConfigCommonService.deleteVideoConfigById(id);
        return R.ok();
    }

    @ApiOperation("修改视频配置")
    @PostMapping("/updateOneVideoCommon")
    public R<Boolean> updateOneVideoCommon(@RequestBody VideoConfigUpdateParam videoConfigUpdateParam) {
        return R.ok(videoConfigCommonService.updateOneVideoCommon(videoConfigUpdateParam));
    }

    @ApiOperation("根据视频配置id删除其关联的摄像头点位")
    @PostMapping("/deletePointById")
    public R<Boolean> deletePointById(@RequestBody VideoConfigDeleteParam videoConfigDeleteParam) {
        videoConfigCommonService.deletePointById(videoConfigDeleteParam.getId(), videoConfigDeleteParam.getPointIds());
        return R.ok();
    }

    @ApiOperation("分页查询视频配置")
    @PostMapping("/selectVideoConfigPage")
    public R<PageBaseReturnParam<VideoConfigCommon>> selectVideoConfigPage(@RequestBody VideoConfigPageSearchParam param) {
        Page<VideoConfigCommon> page = videoConfigCommonService.selectVideoConfigPage(param);
        PageBaseReturnParam<VideoConfigCommon> p = PageBaseReturnParam.ok(page);
        return R.ok(p);
    }

    @ApiOperation("根据id查询视频配置详情")
    @GetMapping("/selectVideoConfigById")
    public R<VideoConfigReturnParam> selectVideoConfigById(@RequestParam Long id) {
        return R.ok(videoConfigCommonService.selectVideoConfigById(id));
    }

}
