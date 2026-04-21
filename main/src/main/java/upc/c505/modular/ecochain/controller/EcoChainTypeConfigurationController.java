package upc.c505.modular.ecochain.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import upc.c505.common.responseparam.PageBaseReturnParam;
import upc.c505.common.responseparam.R;
import upc.c505.modular.ecochain.controller.param.EcoChainTypeConfigurationPageSearchParam;
import upc.c505.modular.ecochain.entity.EcoChainTypeConfiguration;
import upc.c505.modular.ecochain.service.IEcoChainTypeConfigurationService;
import upc.c505.modular.supenterprise.controller.param.SupEnterprisePageSearchParam;
import upc.c505.modular.supenterprise.entity.SupEnterprise;

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
@RequestMapping("/eco-chain-type-configuration")
@Api(tags = "类型配置")
public class EcoChainTypeConfigurationController {

    @Autowired
    private IEcoChainTypeConfigurationService ecoChainTypeConfigurationService;

    @PostMapping("/insertTypeConfiguration")
    @ApiOperation("新增类型配置")
    @Transactional
    public R<Void> insertTypeConfiguration(@RequestBody EcoChainTypeConfiguration param) {
        ecoChainTypeConfigurationService.insertSupEnterprise(param);
        return R.ok();
    }

    @PostMapping("/updateTypeConfiguration")
    @ApiOperation("修改类型配置")
    public R<Boolean> updateTypeConfiguration(@RequestBody EcoChainTypeConfiguration param) {
        boolean result = ecoChainTypeConfigurationService.updateTypeConfiguration(param);
        return R.ok(result);
    }

    @PostMapping("/selectOneTypeConfiguration")
    @ApiOperation("查询单个类型配置")
    public R<EcoChainTypeConfiguration> selectOneTypeConfiguration(@RequestParam Long id) {
        EcoChainTypeConfiguration ecoChainTypeConfiguration = ecoChainTypeConfigurationService.getById(id);
        return R.ok(ecoChainTypeConfiguration);
    }

    @PostMapping("/removeBatchTypeConfiguration")
    @ApiOperation("批量删除类型配置")
    public R<Void> removeBatchSupEnterprise(@RequestBody List<Long> idList) {
        ecoChainTypeConfigurationService.removeBatchTypeConfiguration(idList);
        return R.ok();
    }

    @PostMapping("/selectPageTypeConfiguration")
    @ApiOperation("分页查询类型配置")
    public R<PageBaseReturnParam<EcoChainTypeConfiguration>> selectPageTypeConfiguration(@RequestBody EcoChainTypeConfigurationPageSearchParam param) {
        Page<EcoChainTypeConfiguration> page = ecoChainTypeConfigurationService.selectPageTypeConfiguration(param);
        PageBaseReturnParam<EcoChainTypeConfiguration> p = PageBaseReturnParam.ok(page);
        return R.page(p);
    }
    @PostMapping("/listBySocialCreditCode")
    @ApiOperation("查询类型（根据社会信用代码）")
    public R<List<EcoChainTypeConfiguration>> listBySocialCreditCode(@RequestParam String param){
        List<EcoChainTypeConfiguration> result = ecoChainTypeConfigurationService.listBySocialCreditCode(param);
        return R.ok(result);
    }



}
