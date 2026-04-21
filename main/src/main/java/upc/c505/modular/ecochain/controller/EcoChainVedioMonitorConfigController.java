package upc.c505.modular.ecochain.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import upc.c505.common.responseparam.PageBaseReturnParam;
import upc.c505.common.responseparam.R;
import upc.c505.modular.ecochain.controller.param.EcoChainVedioMonitorConfigPageSearchParam;
import upc.c505.modular.ecochain.entity.EcoChainVedioMonitorConfig;
import upc.c505.modular.ecochain.service.IEcoChainVedioMonitorConfigService;

import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author mjh
 * @since 2024-09-23
 */
@RestController
@RequestMapping("/eco-chain-vedio-monitor-config")
@Api(tags = "监控配置")
public class EcoChainVedioMonitorConfigController {
    @Autowired
    private IEcoChainVedioMonitorConfigService ecoChainVedioMonitorConfigService;

    @PostMapping("/insertOrUpdateConfig")
    @ApiOperation("新增或更新监控配置")
    public R<String> insertOrUpdateConfig(@RequestBody EcoChainVedioMonitorConfig param) {
        return R.ok(ecoChainVedioMonitorConfigService.insertOrUpdateConfig(param));
    }

    @GetMapping("/removeConfigBatch")
    @ApiOperation("批量删除配置")
    @Transactional
    public R<Boolean> removeConfigBatch(@RequestParam List<Long> idList) {
        return R.ok(ecoChainVedioMonitorConfigService.removeBatchByIds(idList));
    }

    @GetMapping("/selectConfig")
    @ApiOperation("查询单个配置")
    public R<EcoChainVedioMonitorConfig> selectConfig(Long id) {
        return R.ok(ecoChainVedioMonitorConfigService.getById(id));
    }

    @PostMapping("/selectConfigPage")
    @ApiOperation("分页查询配置")
    public R<PageBaseReturnParam<EcoChainVedioMonitorConfig>> selectConfigPage(@RequestBody EcoChainVedioMonitorConfigPageSearchParam param) {
        IPage<EcoChainVedioMonitorConfig> configPage = ecoChainVedioMonitorConfigService.selectPage(param);
        PageBaseReturnParam<EcoChainVedioMonitorConfig> page = PageBaseReturnParam.ok(configPage);
        return R.page(page);
    }
}
