package upc.c505.modular.ecochain.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import upc.c505.common.responseparam.PageBaseReturnParam;
import upc.c505.common.responseparam.R;
import upc.c505.modular.ecochain.controller.param.EcoChainBuildWarehouseReturnParam;
import upc.c505.modular.ecochain.controller.param.EcoChainDistributorPageReturnParam;
import upc.c505.modular.ecochain.controller.param.EcoChainDistributorPageSearchParam;
import upc.c505.modular.ecochain.controller.param.EcoChainRegistrationParam;
import upc.c505.modular.ecochain.entity.EcoChainDistributor;
import upc.c505.modular.ecochain.service.IEcoChainDistributorService;
import upc.c505.modular.people.controller.param.PeopleGovernmentPageSearchParam;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author byh
 * @since 2024-11-14
 */
@RestController
@RequestMapping("/eco-chain-distributor")
@Api(tags = "分销商模块")
public class EcoChainDistributorController {

    @Autowired
    private IEcoChainDistributorService ecoChainDistributorService;

    @PostMapping("/insertDistributor")
    @ApiOperation("新增分销商")
    public R<Boolean> insertDistributor(@RequestBody EcoChainDistributor param){
        return R.ok(ecoChainDistributorService.insertDistributor(param));
    }

    @PostMapping("/selectDistributorPage")
    @ApiOperation("分页查询分销商")
    public R<PageBaseReturnParam<EcoChainDistributorPageReturnParam>> selectDistributorPage(@RequestBody EcoChainDistributorPageSearchParam param){
        Page<EcoChainDistributorPageReturnParam> page = ecoChainDistributorService.selectDistributorPage(param);
        PageBaseReturnParam<EcoChainDistributorPageReturnParam> p = PageBaseReturnParam.ok(page);
        return R.page(p);
    }

    @PostMapping("/exportDistributor")
    @ApiOperation("导出分销商数据")
    public void exportDistributor(HttpServletResponse response, @RequestBody EcoChainDistributorPageSearchParam param){
        ecoChainDistributorService.exportDistributor(response, param);
    }

    @PostMapping("/integrateRegistration")
    @ApiOperation("注册接口整合")
    public R<Boolean> integrateRegistration(@Valid @RequestBody EcoChainRegistrationParam param){
        return R.ok(ecoChainDistributorService.integrateRegistration(param));
    }
}
