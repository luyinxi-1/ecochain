package upc.c505.modular.ecochain.config;

import cn.hutool.core.io.resource.ClassPathResource;
import com.wechat.pay.contrib.apache.httpclient.WechatPayHttpClientBuilder;
import com.wechat.pay.contrib.apache.httpclient.auth.PrivateKeySigner;
import com.wechat.pay.contrib.apache.httpclient.auth.WechatPay2Credentials;
import com.wechat.pay.contrib.apache.httpclient.auth.WechatPay2Validator;
import com.wechat.pay.contrib.apache.httpclient.cert.CertificatesManager;
import com.wechat.pay.contrib.apache.httpclient.exception.HttpCodeException;
import com.wechat.pay.contrib.apache.httpclient.exception.NotFoundException;
import com.wechat.pay.contrib.apache.httpclient.util.PemUtil;
import org.apache.http.impl.client.CloseableHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import upc.c505.modular.ecochain.ecoConst.EcoChainWeChatConst;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.PrivateKey;
@Configuration
public class WxPayConfig {

    public PrivateKey loadPrivateKey(String privateKeyPath) {
        return PemUtil.loadPrivateKey(new ClassPathResource(privateKeyPath).getStream());
    }

    /**
     * 获取微信连接的客户端
     *
     * @param certificatesManager 签名验证管理器
    //     * @return
     * @throws NotFoundException 没有找到商户号
     */
    @Bean
    public CloseableHttpClient getWxPayClient(CertificatesManager certificatesManager)
            throws NotFoundException {
        PrivateKey privateKey = loadPrivateKey(EcoChainWeChatConst.PRIVATE_KEY_PATH);

        WechatPayHttpClientBuilder httpClientBuilder = WechatPayHttpClientBuilder.create()
                .withMerchant(EcoChainWeChatConst.MCHID, EcoChainWeChatConst.MERCHANT_SERIAL_NUMBER, privateKey)
                .withValidator(new WechatPay2Validator(certificatesManager.getVerifier(EcoChainWeChatConst.MCHID)));
        return httpClientBuilder.build();
    }

    @Bean
    public CertificatesManager certificatesManager() throws GeneralSecurityException, IOException, HttpCodeException {
        // 加载用户私钥
        PrivateKey privateKey = loadPrivateKey(EcoChainWeChatConst.PRIVATE_KEY_PATH);
        // 私钥签名对象
        PrivateKeySigner privateKeySigner = new PrivateKeySigner(EcoChainWeChatConst.MERCHANT_SERIAL_NUMBER, privateKey);
        // 身份认证对象
        WechatPay2Credentials wechatPay2Credentials = new WechatPay2Credentials(EcoChainWeChatConst.MCHID, privateKeySigner);

        // 获取证书管理器实例
        CertificatesManager certificatesManager = CertificatesManager.getInstance();
        // 向证书管理器增加需要自动更新平台证书的商户信息
        certificatesManager.putMerchant(EcoChainWeChatConst.MCHID, wechatPay2Credentials, EcoChainWeChatConst.API_V3_KEY.getBytes(StandardCharsets.UTF_8));
        return certificatesManager;
    }

}
