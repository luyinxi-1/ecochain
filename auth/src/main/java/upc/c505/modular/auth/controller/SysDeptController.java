package upc.c505.modular.auth.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import upc.c505.common.responseparam.PageBaseReturnParam;
import upc.c505.common.responseparam.R;
import upc.c505.modular.auth.controller.param.DeptAreaParam;
import upc.c505.modular.auth.controller.param.DeptPageSearchParam;
import upc.c505.modular.auth.entity.SysArea;
import upc.c505.modular.auth.entity.SysDept;
import upc.c505.modular.auth.service.ISysDeptAreaService;
import upc.c505.modular.auth.service.ISysDeptService;
import upc.c505.utils.StringUtil;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * <p>
 * 系统_部门表 前端控制器
 * </p>
 *
 * @author sxz
 * @since 2023-08-31
 */
@RestController
@RequestMapping("/sys-dept")
@Api(tags = "部门管理")
public class SysDeptController {

    @Autowired
    private ISysDeptService sysDeptService;

    @PostMapping("/addDept")
    @ApiOperation("添加部门")
    public R<String> addDept(@RequestBody SysDept sysDept) {
        sysDeptService.save(sysDept);
        return R.ok();
    }

    @GetMapping("/deleteDeptByIdList")
    @ApiOperation("根据id列表删除部门")
    public R<String> deleteDeptByIdList(@RequestParam("idList")
                                        @NotEmpty(message = "数组不能为空")
                                                List<Long> idList) {
        sysDeptService.deleteByIdList(idList);
        return R.ok();
    }

    @GetMapping("/deleteById")
    @ApiOperation("根据id删除部门")
    public R<String> deleteById(@RequestParam("idList") Long id){
        sysDeptService.deleteById(id);
        return R.ok();
    }

    @PostMapping("/updateDeptById")
    @ApiOperation("根据id更改职务信息")
    public R<String> updateJobById(@RequestBody SysDept sysDept) {
        sysDeptService.updateById(sysDept);
        return R.ok();
    }

    @PostMapping("/selectDeptPage")
    @ApiOperation("查询部门列表")
    public R<PageBaseReturnParam<SysDept>> selectJobPage(@RequestBody DeptPageSearchParam param) {
        Page<SysDept> sysDeptPage = sysDeptService.selectPage(param);
        PageBaseReturnParam<SysDept> ok = PageBaseReturnParam.ok(sysDeptPage);
        return R.page(ok);
    }

    @PostMapping("/listAreaByDeptId")
    @ApiOperation("根据部门id查询管辖区域")
    public R<PageBaseReturnParam<SysArea>> listAreaByDeptId(@RequestBody DeptPageSearchParam param) {
        Page<SysArea> sysAreaPage = sysDeptService.listAreaByDeptId(param);
        PageBaseReturnParam<SysArea> ok = PageBaseReturnParam.ok(sysAreaPage);
        return R.page(ok);
    }

    @PostMapping("/updateAreaByDeptIdAndAreaList")
    @ApiOperation("更新相应部门的管辖区域")
    public R<String> updateAreaByDeptIdAndAreaList(@RequestBody DeptAreaParam deptAreaParam) {
        List<Long> areaIdList = StringUtil.splitToListLong(deptAreaParam.getAreaIds());
        sysDeptService.updateAreaByDeptIdAndAreaList(deptAreaParam,areaIdList);
        return R.ok();
    }
}
