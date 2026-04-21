package upc.c505.modular.auth.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import upc.c505.common.responseparam.PageBaseReturnParam;
import upc.c505.common.responseparam.R;
import upc.c505.modular.auth.controller.param.JobPageSearchParam;
import upc.c505.modular.auth.entity.SysJob;
import upc.c505.modular.auth.service.ISysJobService;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * <p>
 * 职务表 前端控制器
 * </p>
 *
 * @author sxz
 * @since 2023-08-31
 */
@RestController
@RequestMapping("/sys-job")
@Api(tags = "职务配置")
public class SysJobController {

    @Autowired
    private ISysJobService sysJobService;


    @PostMapping("/addJob")
    @ApiOperation("添加职务")
    public R<String> addJob(@RequestBody SysJob sysJob) {
        sysJobService.save(sysJob);
        return R.ok();
    }

    @GetMapping("/deleteJobsByIdList")
    @ApiOperation("根据id列表删除职务")
    public R<String> deleteJobsByIdList(@RequestParam("idList")
                                        @NotEmpty(message = "数组不能为空")
                                                List<Long> idList) {
        sysJobService.deleteByIdList(idList);
        return R.ok();
    }

    @GetMapping("/deleteById")
    @ApiOperation("根据id删除职务")
    public R<String> deleteById(@RequestParam("idList") Long id){
        sysJobService.deleteById(id);
        return R.ok();
    }

    @PostMapping("/updateJobById")
    @ApiOperation("根据id更改职务信息")
    public R<String> updateJobById(@RequestBody SysJob sysJob) {
        sysJobService.updateById(sysJob);
        return R.ok();
    }

    @PostMapping("/selectJobPage")
    @ApiOperation("查询职务列表")
    public R<PageBaseReturnParam<SysJob>> selectJobPage(@RequestBody JobPageSearchParam param) {
        Page<SysJob> sysJobPage = sysJobService.selectPage(param);
        PageBaseReturnParam<SysJob> ok = PageBaseReturnParam.ok(sysJobPage);
        return R.page(ok);
    }
}
