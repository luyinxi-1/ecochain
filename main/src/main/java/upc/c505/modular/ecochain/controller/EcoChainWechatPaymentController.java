package upc.c505.modular.ecochain.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import upc.c505.common.responseparam.R;
import upc.c505.modular.ecochain.entity.EcoChainWechatPayment;
import upc.c505.modular.ecochain.service.IEcoChainWechatPaymentService;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author xth
 * @since 2024-11-13
 */
@RestController
@RequestMapping("/eco-chain-wechat-payment")
@Api(tags = "生态链订单")
public class EcoChainWechatPaymentController {
    @Autowired
    private IEcoChainWechatPaymentService ecoChainWechatPaymentService;
    @PostMapping("/getPaymentByoutTradeNo")
    @ApiOperation("获取订单根据订单号")
    public R<EcoChainWechatPayment> getPaymentByoutTradeNo(@RequestParam String param) {
        EcoChainWechatPayment result = ecoChainWechatPaymentService.getPaymentByoutTradeNo(param);
        return R.ok(result);
    }
}
