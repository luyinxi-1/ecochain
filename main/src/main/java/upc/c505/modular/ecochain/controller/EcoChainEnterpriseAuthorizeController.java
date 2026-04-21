package upc.c505.modular.ecochain.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import upc.c505.common.responseparam.PageBaseReturnParam;
import upc.c505.common.responseparam.R;
import upc.c505.exception.BusinessErrorEnum;
import upc.c505.exception.BusinessException;
import upc.c505.modular.ecochain.controller.param.EcoChainEnterpriseAuthorizePageSearchParam;
import upc.c505.modular.ecochain.controller.param.EcoChainEnterpriseAuthorizeReturnParam;
import upc.c505.modular.ecochain.entity.EcoChainEnterpriseAuthorize;
import upc.c505.modular.ecochain.entity.EcoChainVedioMonitorConfig;
import upc.c505.modular.ecochain.service.IEcoChainEnterpriseAuthorizeService;

import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author mjh
 * @since 2024-09-25
 */
@RestController
@RequestMapping("/eco-chain-enterprise-authorize")
@Api(tags = "企业授权管理")
public class EcoChainEnterpriseAuthorizeController {
    @Autowired
    private IEcoChainEnterpriseAuthorizeService ecoChainEnterpriseAuthorizeService;

    @PostMapping("/insert")
    @ApiOperation("添加授权请求")
    public R<Boolean> insert(@RequestBody EcoChainEnterpriseAuthorize param) {
        param.setAuthorizeStatus(0);
        return R.ok(ecoChainEnterpriseAuthorizeService.save(param));
    }

    @PostMapping("/updateAuthorize")
    @ApiOperation("更新授权请求")
    public R<Boolean> update(@RequestBody EcoChainEnterpriseAuthorize param) {
        return R.ok(ecoChainEnterpriseAuthorizeService.updateAuthorize(param));
    }

    @PostMapping("/removeBatch")
    @ApiOperation("根据id批量删除授权请求")
    @Transactional
    public R<Boolean> removeBatch(@RequestParam List<Long> idList) {
        return R.ok(ecoChainEnterpriseAuthorizeService.removeBatchByIds(idList));
    }

    @PostMapping("/removeBatchBySocialCreditCode")
    @ApiOperation("根据信用代码批量删除授权请求")
    @Transactional
    public R<String> removeBySocialCreditCodeBatch(@RequestParam List<String> socialCreditCodeList) {
        return R.ok(ecoChainEnterpriseAuthorizeService.removeBySocialCreditCodeBatch(socialCreditCodeList));
    }

    @GetMapping("/selectById")
    @ApiOperation("根据id查询授权请求")
    public R<EcoChainEnterpriseAuthorizeReturnParam> selectById(Long id) {
        return R.ok(ecoChainEnterpriseAuthorizeService.selectById(id));
    }

    @GetMapping("/selectByCreditCode")
    @ApiOperation("根据信用代码查询授权请求")
    public R<EcoChainEnterpriseAuthorizeReturnParam> selectByCreditCode(String creditCode) {
        EcoChainEnterpriseAuthorize one = ecoChainEnterpriseAuthorizeService.getOne(
                new LambdaQueryWrapper<EcoChainEnterpriseAuthorize>().eq(
                        EcoChainEnterpriseAuthorize::getCreditCode, creditCode)
        );
        if (ObjectUtils.isEmpty(one)) {
            throw new BusinessException(BusinessErrorEnum.NO_EXIT, "企业不存在");
        }
        Long id = one.getId();
        return R.ok(ecoChainEnterpriseAuthorizeService.selectById(id));
    }

    @PostMapping("/selectPage")
    @ApiOperation("分页查询授权请求")
    public R<PageBaseReturnParam<EcoChainEnterpriseAuthorizeReturnParam>> selectAuthorizeRequestPage(@RequestBody EcoChainEnterpriseAuthorizePageSearchParam param) {
        Page<EcoChainEnterpriseAuthorizeReturnParam> authorizePage = ecoChainEnterpriseAuthorizeService.selectPage(param);
        PageBaseReturnParam<EcoChainEnterpriseAuthorizeReturnParam> page = PageBaseReturnParam.ok(authorizePage);
        return R.page(page);
    }
}

