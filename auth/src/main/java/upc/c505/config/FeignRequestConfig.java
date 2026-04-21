package upc.c505.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextListener;
import upc.c505.common.UserInfoToRedis;
import upc.c505.common.UserUtils;
import upc.c505.constant.SystemConst;
import upc.c505.utils.TokenUtils;

/**
 * feign的配置类，可以设置请求头等信息
 * @author wzy
 */
@Configuration
//@EnableFeignClients(basePackages = "upc.c505.modular.countydata.feign")
public class FeignRequestConfig implements RequestInterceptor {

    @Bean
    public RequestContextListener requestContextListener() {
        return new RequestContextListener();
    }

    /**
     * 设置每次feign调用请求头中的token
     */
    @Override
    public void apply(RequestTemplate requestTemplate) {
//        UserInfoToRedis userInfo = UserUtils.get();
////        使用用户id和账号生成token
//        String token = TokenUtils.generateToken(userInfo.getId(), userInfo.getUserCode());
//        requestTemplate.header(SystemConst.FEIGN_TOKEN_NAME, token);
    }
}