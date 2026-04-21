package upc.c505.modular.ecochain.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import upc.c505.common.responseparam.PageBaseReturnParam;
import upc.c505.common.responseparam.R;
import upc.c505.modular.ecochain.controller.param.EcoChainProcessNodeConfigurationPageSearchParam;
import upc.c505.modular.ecochain.controller.param.EcoChainTypeConfigurationPageSearchParam;
import upc.c505.modular.ecochain.entity.EcoChainProcessNodeConfiguration;
import upc.c505.modular.ecochain.entity.EcoChainTypeConfiguration;
import upc.c505.modular.ecochain.service.IEcoChainProcessNodeConfigurationService;

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
@RequestMapping("/eco-chain-process-node-configuration")
@Api(tags = "过程节点配置")
public class EcoChainProcessNodeConfigurationController {
    @Autowired
    private IEcoChainProcessNodeConfigurationService ecoChainProcessNodeConfigurationService;

    @PostMapping("/insertProcessNodeConfiguration")
    @ApiOperation("新增过程节点配置")
    @Transactional
    public R<Void> insertProcessNodeConfiguration(@RequestBody EcoChainProcessNodeConfiguration param) {
        ecoChainProcessNodeConfigurationService.insertProcessNodeConfiguration(param);
        return R.ok();
    }

    @PostMapping("/updateProcessNodeConfiguration")
    @ApiOperation("修改过程节点配置")
    public R<Boolean> updateProcessNodeConfiguration(@RequestBody EcoChainProcessNodeConfiguration param){
        boolean result = ecoChainProcessNodeConfigurationService.updateProcessNodeConfiguration(param);
        return R.ok(result);
    }

    @PostMapping("/selectProcessNodeConfiguration")
    @ApiOperation("查询节点配置（单个）")
    public R<EcoChainProcessNodeConfiguration> selectProcessNodeConfiguration(@RequestParam Long id){
        EcoChainProcessNodeConfiguration ecoChainProcessNodeConfiguration = ecoChainProcessNodeConfigurationService.getById(id);
        return R.ok(ecoChainProcessNodeConfiguration);
    }


    @PostMapping("/removeProcessNodeConfigurationBatch")
    @ApiOperation("批量删除节点配置")
    public R<Void> removeProcessNodeConfigurationBatch(@RequestParam List<Long> idList){
        ecoChainProcessNodeConfigurationService.removeBatchByIds(idList);
        return R.ok();
    }


    @PostMapping("/listBySocialCreditCode")
    @ApiOperation("查询节点（根据社会信用代码）")
    public R<List<EcoChainProcessNodeConfiguration>> listBySocialCreditCode(@RequestParam String param){
        List<EcoChainProcessNodeConfiguration> result = ecoChainProcessNodeConfigurationService.listBySocialCreditCode(param);
        return R.ok(result);
    }

    @PostMapping("/selectPageProcessNodeConfiguration")
    @ApiOperation("分页查询过程节点配置")
    public R<PageBaseReturnParam<EcoChainProcessNodeConfiguration>> selectPageProcessNodeConfiguration(@RequestBody EcoChainProcessNodeConfigurationPageSearchParam param) {
        Page<EcoChainProcessNodeConfiguration> page = ecoChainProcessNodeConfigurationService.selectPageProcessNodeConfiguration(param);
        PageBaseReturnParam<EcoChainProcessNodeConfiguration> p = PageBaseReturnParam.ok(page);
        return R.page(p);
    }
}
