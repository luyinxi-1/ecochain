package upc.c505.modular.miniprogram.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import upc.c505.common.responseparam.PageBaseReturnParam;
import upc.c505.common.responseparam.R;
import upc.c505.modular.miniprogram.entity.MiniProgramBindEntity;
import upc.c505.modular.miniprogram.param.MiniProgramBindParam;
import upc.c505.modular.miniprogram.param.MiniProgramBindSearchParam;
import upc.c505.modular.miniprogram.param.UpdateMiniProgramBindParam;
import upc.c505.modular.miniprogram.service.IMiniProgramBindService;

import javax.validation.Valid;

/**
 * 小程序绑定控制器
 */
@Api(tags = "小程序绑定管理")
@RestController
@RequestMapping("/miniprogram")
public class MiniProgramBindController {

    @Autowired
    private IMiniProgramBindService miniProgramBindService;

    /**
     * 创建小程序绑定
     */
    @ApiOperation("创建小程序绑定")
    @PostMapping("/create")
    public R<MiniProgramBindEntity> create(@Valid @RequestBody MiniProgramBindParam param) {
        try {
            if (miniProgramBindService.existsByAppId(param.getAppId())) {
                return R.fail("该小程序AppId已存在");
            }
            MiniProgramBindEntity entity = miniProgramBindService.create(param);
            return R.ok(entity);
        } catch (Exception e) {
            return R.fail("创建小程序绑定失败: " + e.getMessage());
        }
    }

    /**
     * 删除小程序绑定
     */
    @ApiOperation("删除小程序绑定")
    @PostMapping("/delete")
    public R<Boolean> delete(@RequestParam Long id) {
        try {
            Boolean result = miniProgramBindService.deleteById(id);
            return R.ok(result);
        } catch (Exception e) {
            return R.fail("删除小程序绑定失败: " + e.getMessage());
        }
    }

    /**
     * 更新小程序绑定
     */
    @ApiOperation("更新小程序绑定")
    @PostMapping("/update")
    public R<MiniProgramBindEntity> update(@Valid @RequestBody UpdateMiniProgramBindParam param) {
        try {
            MiniProgramBindEntity entity = miniProgramBindService.update(param);
            return R.ok(entity);
        } catch (Exception e) {
            return R.fail("更新小程序绑定失败: " + e.getMessage());
        }
    }

    /**
     * 获取小程序绑定详情
     */
    @ApiOperation("获取小程序绑定详情")
    @GetMapping("/get/{id}")
    public R<MiniProgramBindEntity> getById(@PathVariable Long id) {
        try {
            MiniProgramBindEntity entity = miniProgramBindService.getById(id);
            if (entity != null) {
                return R.ok(entity);
            } else {
                return R.fail("未找到对应的小程序绑定信息");
            }
        } catch (Exception e) {
            return R.fail("获取小程序绑定详情失败: " + e.getMessage());
        }
    }

    /**
     * 根据AppID获取小程序绑定详情
     */
    @ApiOperation("根据AppID获取小程序绑定详情")
    @GetMapping("/getByAppId")
    public R<MiniProgramBindEntity> getByAppId(@RequestParam String appId) {
        try {
            MiniProgramBindEntity entity = miniProgramBindService.getByAppId(appId);
            if (entity != null) {
                return R.ok(entity);
            } else {
                return R.fail("未找到对应的小程序绑定信息");
            }
        } catch (Exception e) {
            return R.fail("获取小程序绑定详情失败: " + e.getMessage());
        }
    }

    /**
     * 分页查询小程序绑定
     */
    @ApiOperation("分页查询小程序绑定")
    @PostMapping("/listPage")
    public R<PageBaseReturnParam<MiniProgramBindEntity>> listPage(
            @Valid @RequestBody MiniProgramBindSearchParam param) {
        try {
            PageBaseReturnParam<MiniProgramBindEntity> result = miniProgramBindService.listPage(param);
            return R.ok(result);
        } catch (Exception e) {
            return R.fail("查询小程序绑定列表失败: " + e.getMessage());
        }
    }
}
