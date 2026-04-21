package upc.c505.modular.villageiot.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.catalina.startup.HostConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import upc.c505.common.responseparam.PageBaseReturnParam;
import upc.c505.common.responseparam.R;
import upc.c505.modular.villageiot.controller.param.HostConfigPageSearchParam;
import upc.c505.modular.villageiot.entity.VideoHostConfig;
import upc.c505.modular.villageiot.service.IVideoHostConfigService;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author frd
 * @since 2023-11-04
 */
@RestController
@RequestMapping("/video-host-config")
@Api(tags = {"host配置管理"})
public class VideoHostConfigController {
    @Autowired
    private IVideoHostConfigService videoHostConfigService;

    @PostMapping("/insertHostConfig")
    @ApiOperation("新增host配置")
    public R<Object> insertHostConfig(@RequestBody VideoHostConfig videoHostConfig){
        return R.ok(videoHostConfigService.insertHostConfig(videoHostConfig));
    }

    @GetMapping("/deleteOneHostConfig")
    @ApiOperation("删除单个host配置")
    public R<Long> deleteOneHostConfig(@RequestParam Long id){
        return R.ok(videoHostConfigService.deleteOneHostConfig(id));
    }

    @PostMapping("/updateHostConfig")
    @ApiOperation("修改host配置")
    public R<Object> updateHostConfig(@RequestBody VideoHostConfig videoHostConfig){
        return R.ok(videoHostConfigService.updateHostConfig(videoHostConfig));
    }

    @PostMapping("/selectHostConfigPage")
    @ApiOperation("分页查询host配置")
    public R<PageBaseReturnParam<VideoHostConfig>> selectHostConfigPage(@RequestBody HostConfigPageSearchParam param){
        Page<VideoHostConfig> page = videoHostConfigService.selectPage(param);
        PageBaseReturnParam<VideoHostConfig> p = PageBaseReturnParam.ok(page);
        return R.ok(p);
    }
}
