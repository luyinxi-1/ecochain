package upc.c505.modular.villageiot.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import upc.c505.common.responseparam.PageBaseReturnParam;
import upc.c505.common.responseparam.R;
import upc.c505.modular.villageiot.controller.param.InspectionAnalysisSearchParam;
import upc.c505.modular.villageiot.controller.param.InspectionRecordPageSearchParam;
import upc.c505.modular.villageiot.controller.param.InspectionAnalysisReturnParam;
import upc.c505.modular.villageiot.entity.VideoInspectionRecord;
import upc.c505.modular.villageiot.service.IVideoInspectionRecordService;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author frd
 * @since 2024-06-28
 */
@RestController
@RequestMapping("/video-inspection-record")
@Api(tags = {"视频巡查记录"})
public class VideoInspectionRecordController {

    @Autowired
    private IVideoInspectionRecordService videoInspectionRecordService;

    @ApiOperation("发送预警，返回id")
    @PostMapping("/sendWarning")
    public R<Long> sendWarning(@RequestBody VideoInspectionRecord videoInspectionRecord){
        return R.ok(videoInspectionRecordService.sendWarning(videoInspectionRecord));
    }

    @ApiOperation("根据id删除巡查记录")
    @GetMapping("/deleteInspectionRecordById")
    public R<Boolean> deleteInspectionRecordById(@RequestParam Long id) {
        return R.ok(videoInspectionRecordService.removeById(id));
    }

    @ApiOperation("修改巡查记录")
    @PostMapping("/updateInspectionRecord")
    public R<Boolean> updateInspectionRecord(@RequestBody VideoInspectionRecord  videoInspectionRecord) {
        return R.ok(videoInspectionRecordService.updateById(videoInspectionRecord));
    }
    @ApiOperation("批量删除巡查记录")
    @GetMapping("/deleteBatchInspectionRecord")
    public R<Boolean> deleteInspectionRecordById(@RequestParam List<Long> idList) {
        return R.ok(videoInspectionRecordService.removeBatchByIds(idList));
    }
    @ApiOperation("分页查询巡查记录")
    @PostMapping("/selectInspectionRecordPage")
    public R<PageBaseReturnParam<VideoInspectionRecord>> selectInspectionRecordPage(@RequestBody InspectionRecordPageSearchParam param){
        Page<VideoInspectionRecord> p = videoInspectionRecordService.selectInspectionRecordPage(param);
        PageBaseReturnParam<VideoInspectionRecord> page = PageBaseReturnParam.ok(p);
        return R.ok(page);
    }

    @ApiOperation("巡检分析")
    @PostMapping("/inspectionAnalysis")
    public R<List<InspectionAnalysisReturnParam>> inspectionAnalysis(@RequestBody InspectionAnalysisSearchParam param) {
        return R.ok(videoInspectionRecordService.inspectionAnalysis(param));
    }

    @ApiOperation("统计项目的远程督导数量")
    @GetMapping("/countRemoteSupervision")
    public R<Map<String, Integer>> countRemoteSupervision(@RequestParam List<String> projectNumberList) {
        return R.ok(videoInspectionRecordService.countRemoteSupervision(projectNumberList));
    }

    @ApiOperation("生成远程巡查单")
    @GetMapping("/generateInspectionBill")
    public void generateInspectionBill(Long id, HttpServletResponse response) {
        videoInspectionRecordService.generateInspectionBill(id, response);
    }
}
