package upc.c505.modular.ecochain.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import upc.c505.common.responseparam.PageBaseReturnParam;
import upc.c505.common.responseparam.R;
import upc.c505.modular.ecochain.controller.param.*;
import upc.c505.modular.ecochain.entity.EcoChainEnterpriseAlliance;
import upc.c505.modular.ecochain.service.IEcoChainEnterpriseAllianceService;

import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author byh
 * @since 2024-09-25
 */
@Api(tags = { "生态链合作联盟" })
@RestController
@RequestMapping("/eco-chain-enterprise-alliance")
public class EcoChainEnterpriseAllianceController {

    @Autowired
    private IEcoChainEnterpriseAllianceService ecoChainEnterpriseAllianceService;

    @PostMapping("/insertEnterpriseAlliance")
    @ApiOperation("新增合作联盟")
    @Transactional
    public R<Void> insertEnterpriseAlliance(@RequestBody EcoChainEnterpriseAlliance param) {
        ecoChainEnterpriseAllianceService.insertEnterpriseAlliance(param);
        return R.ok();
    }

    @PostMapping("/removeEnterpriseAlliance")
    @ApiOperation("批量删除合作联盟")
    public R<Void> removeEnterpriseAlliance(@RequestParam List<Long> idList) {
        ecoChainEnterpriseAllianceService.removeBatchByIds(idList);
        return R.ok();
    }

    @PostMapping("/updateEnterpriseAlliance")
    @ApiOperation("更新合作联盟")
    public R<Boolean> updateEnterpriseAlliance(@RequestBody EcoChainEnterpriseAlliance param) {
        boolean result = ecoChainEnterpriseAllianceService.updateById(param);
        return R.ok(result);
    }

    @PostMapping("/getEnterpriseBySocialCreditCode")
    @ApiOperation("根据社会信用代码查询企业信息")
    public R<List<EcoChainGetEnterpriseBySocialCreditCodeReturnParam>> getEnterpriseBySocialCreditCode(
            @RequestParam(required = false) String socialCreditCode,
            @RequestParam(required = false) String enterpriseName) {
        return R.ok(
                ecoChainEnterpriseAllianceService.getEnterpriseBySocialCreditCode(socialCreditCode, enterpriseName));
    }

    @PostMapping("/selectPageEnterpriseAlliance")
    @ApiOperation("分页查询合作联盟")
    public R<PageBaseReturnParam<EcoChainEnterpriseAllianceReturnParam>> selectPageBuildWarehouse(
            @RequestBody EcoChainEnterpriseAlliancePageSearchParam param) {
        Page<EcoChainEnterpriseAllianceReturnParam> page = ecoChainEnterpriseAllianceService
                .selectPageEnterpriseAlliance(param);
        PageBaseReturnParam<EcoChainEnterpriseAllianceReturnParam> p = PageBaseReturnParam.ok(page);
        return R.page(p);
    }

}
