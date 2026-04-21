package upc.c505.modular.auth.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import upc.c505.common.responseparam.PageBaseReturnParam;
import upc.c505.common.responseparam.R;
import upc.c505.modular.auth.controller.param.AreaParam;
import upc.c505.modular.auth.controller.param.GetAreaPageParam;
import upc.c505.modular.auth.controller.param.tree.AreaTreeNode;
import upc.c505.modular.auth.service.ISysAreaService;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 区域表 前端控制器
 * </p>
 *
 * @author sxz
 * @since 2023-07-30
 */
@RestController
@RequestMapping("/sys-area")
@Api(tags = "区域管理")
public class SysAreaController {

    @Autowired
    private ISysAreaService sysAreaService;

    @PostMapping("addArea")
    @ApiOperation("添加区域")
    public R<String> addArea(@RequestBody @Validated AreaParam areaParam) {
        sysAreaService.addArea(areaParam);
        return R.ok("添加成功");
    }

    @GetMapping("/deleteAreasByIdList")
    @ApiOperation("根据id列表删除区域")
    public R<String> deleteAreasByIdList(@RequestParam("idList")
                                         @NotEmpty(message = "数组不能为空")
                                                 List<Integer> idList) {
        //删除区域还得删除相关连表
        sysAreaService.deleteAreasByIdList(idList);
        return R.ok();
    }

    @PostMapping("/updateAreaById")
    @ApiOperation("根据id更改区域信息")
    public R<String> updateAreaById(@RequestBody AreaParam areaParam) {
        sysAreaService.updateAreaById(areaParam);
        return R.ok();
    }

    @PostMapping("/getAreaPage")
    @ApiOperation("查询区域列表")
    public R<PageBaseReturnParam<AreaTreeNode>> getAreaPage(@RequestBody GetAreaPageParam param) {
        return R.page(sysAreaService.getAreaPage(param));
    }

    @GetMapping("/getChildAreaIdList")
    @ApiOperation("获取传入id的子区域idList")
    public R<List<Long>> getChildAreaIdList(@RequestParam("areaId") Long areaId) {
        List<Long> childAreaIdList = sysAreaService.getChildAreaIdList(areaId);
        return R.ok(childAreaIdList);
    }

    @GetMapping("/getAreaTreeNode")
    @ApiOperation("获取该区域节点下所有子区域节点")
    public R<AreaTreeNode> getAreaTreeNode(@RequestParam("areaId") Long areaId) {
        AreaTreeNode areaTreeNode = sysAreaService.getAreaTreeNode(areaId);
        return R.ok(areaTreeNode);
    }

    @GetMapping("/queryWayById")
    @ApiOperation("查找某区域根节点到它的某个子节点的路径")
    public R<List<Long>> queryWayById(@RequestParam("searchId") Long searchId) {
        List<Long> wayList = sysAreaService.getElders(searchId);
        return R.ok(wayList);
    }
}
