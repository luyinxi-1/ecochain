package upc.c505.modular.ecochain.service;

import upc.c505.modular.ecochain.entity.EcoChainWechatPayment;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xth
 * @since 2024-11-13
 */
public interface IEcoChainWechatPaymentService extends IService<EcoChainWechatPayment> {

    EcoChainWechatPayment getPaymentByoutTradeNo(String param);
}
