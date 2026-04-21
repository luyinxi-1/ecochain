package upc.c505.modular.ecochain.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import upc.c505.modular.ecochain.controller.EcoChainWechatPaymentController;
import upc.c505.modular.ecochain.entity.EcoChainWechatPayment;
import upc.c505.modular.ecochain.mapper.EcoChainWechatPaymentMapper;
import upc.c505.modular.ecochain.service.IEcoChainWechatPaymentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xth
 * @since 2024-11-13
 */
@Service
public class EcoChainWechatPaymentServiceImpl extends ServiceImpl<EcoChainWechatPaymentMapper, EcoChainWechatPayment> implements IEcoChainWechatPaymentService {
    @Autowired
    private EcoChainWechatPaymentMapper ecoChainWechatPaymentMapper;
    @Override
    public EcoChainWechatPayment getPaymentByoutTradeNo(String param) {
        return ecoChainWechatPaymentMapper.selectOne(new LambdaQueryWrapper<EcoChainWechatPayment>()
                .eq(EcoChainWechatPayment::getOutTradeNo, param));
    }
}
