package upc.c505.modular.ecochain.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import upc.c505.common.responseparam.PageBaseReturnParam;
import upc.c505.common.responseparam.R;
import upc.c505.modular.ecochain.controller.param.EcoChainRegionalConfigurationPageSearchParam;
import upc.c505.modular.ecochain.controller.param.EcoChainTypeConfigurationPageSearchParam;
import upc.c505.modular.ecochain.entity.EcoChainRegionalConfiguration;
import upc.c505.modular.ecochain.entity.EcoChainTypeConfiguration;
import upc.c505.modular.ecochain.service.IEcoChainRegionalConfigurationService;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author xth
 * @since 2024-09-21
 */
@RestController
@RequestMapping("/eco-chain-regional-configuration")
@Api(tags = "区域组配置")
public class EcoChainRegionalConfigurationController {

    @Autowired
    private IEcoChainRegionalConfigurationService ecoChainRegionalConfigurationService;

    @PostMapping("/insertRegionalConfiguration")
    @ApiOperation("新增区域组配置")
    @Transactional
    public R<Void> insertRegionalConfiguration(@RequestBody EcoChainRegionalConfiguration param) {
        ecoChainRegionalConfigurationService.insertRegionalConfiguration(param);
        return R.ok();
    }

    @PostMapping("/updateRegionalConfiguration")
    @ApiOperation("修改区域组配置")
    public R<Boolean> updateRegionalConfiguration(@RequestBody EcoChainRegionalConfiguration param) {
        boolean result = ecoChainRegionalConfigurationService.updateRegionalConfiguration(param);
        return R.ok(result);
    }

    @PostMapping("/selectOneRegionalConfiguration")
    @ApiOperation("查询单个类型配置")
    public R<EcoChainRegionalConfiguration> selectOneRegionalConfiguration(@RequestParam Long id) {
        EcoChainRegionalConfiguration ecoChainRegionalConfiguration = ecoChainRegionalConfigurationService.getById(id);
        return R.ok(ecoChainRegionalConfiguration);
    }

    @PostMapping("/removeBatchRegionalConfiguration")
    @ApiOperation("批量删除类型配置")
    public R<Void> removeBatchRegionalConfiguration(@RequestBody List<Long> idList) {
        ecoChainRegionalConfigurationService.removeBatchRegionalConfiguration(idList);
        return R.ok();
    }

    @PostMapping("/listBySocialCreditCode")
    @ApiOperation("查询区域信息（根据社会信用代码）")
    public R<List<EcoChainRegionalConfiguration>> listBySocialCreditCode(@RequestParam String param){
        List<EcoChainRegionalConfiguration> result = ecoChainRegionalConfigurationService.listBySocialCreditCode(param);
        return R.ok(result);
    }

    @PostMapping("/selectPageRegionalConfiguration")
    @ApiOperation("分页查询区域组配置")
    public R<PageBaseReturnParam<EcoChainRegionalConfiguration>> selectPageRegionalConfiguration(@RequestBody EcoChainRegionalConfigurationPageSearchParam param) {
        Page<EcoChainRegionalConfiguration> page = ecoChainRegionalConfigurationService.selectPageRegionalConfiguration(param);
        PageBaseReturnParam<EcoChainRegionalConfiguration> p = PageBaseReturnParam.ok(page);
        return R.page(p);
    }
}
