package upc.c505.modular.ecochain.controller;


import org.springframework.web.bind.annotation.RequestMapping;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import upc.c505.common.responseparam.R;
import upc.c505.modular.ecochain.entity.EcoChainEnterprisePublicity;
import upc.c505.modular.ecochain.service.IEcoChainEnterprisePublicityService;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author la
 * @since 2024-09-24
 */
@RestController
@RequestMapping("/eco-chain-enterprise-publicity")
@Api(tags = "企业主体宣传")
public class EcoChainEnterprisePublicityController {
    @Autowired
    IEcoChainEnterprisePublicityService ecoChainEnterprisePublicityService;

    @ApiOperation("新增企业主体宣传信息")
    @PostMapping("/insertEnterprisePublicity")
    public R<Boolean> insertEnterprisePublicity(@RequestBody EcoChainEnterprisePublicity param){
        return R.ok(ecoChainEnterprisePublicityService.save(param));
    }

    @ApiOperation("删除企业主主体宣传信息")
    @PostMapping("/deleteEnterprisePublicity")
    public R deleteEnterprisePublicity(@RequestBody EcoChainEnterprisePublicity ecoChainEnterprisePublicity){
        return R.ok(ecoChainEnterprisePublicityService.removeById(ecoChainEnterprisePublicity));
    }

    @ApiOperation("修改企业主体宣传信息")
    @PostMapping("/updateEnterprisePublicity")
    public R updateEnterprisePublicity(@RequestBody EcoChainEnterprisePublicity ecoChainEnterprisePublicity){
        return R.ok(ecoChainEnterprisePublicityService.updateById(ecoChainEnterprisePublicity));
    }

    @ApiOperation("查询企业主体宣传信息")
    @PostMapping("/selectEnterprisePublicity")
    public R selectEnterprisePublicity(@RequestBody EcoChainEnterprisePublicity ecoChainEnterprisePublicity){
        return R.ok(ecoChainEnterprisePublicityService.getById(ecoChainEnterprisePublicity));
    }

}
