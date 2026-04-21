package upc.c505.config.web.interceptor;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import upc.c505.common.UserInfoToRedis;
import upc.c505.common.UserUtils;
import upc.c505.constant.SystemConst;
import upc.c505.context.LoginContextHolder;
import upc.c505.exception.BusinessErrorEnum;
import upc.c505.exception.BusinessException;
import upc.c505.utils.RedisUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * 校验token的拦截器，如果token校验通过就把信息存到redis
 *
 * @author ly
 * 拦截器
 */
@Slf4j
public class RequestInterceptor implements HandlerInterceptor {

    @Autowired
    private RedisUtils redisUtils;


    /**
     * preHandle方法是进行处理器拦截用的，该方法将在Controller处理之前进行调用
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        if (LoginContextHolder.getIsLogined()) {
            return true;
        }
//        取出请求头的token
        String token = request.getHeader(SystemConst.TOKEN_NAME);
//        验证常规token
        if (StringUtils.isBlank(token)) {
            return true;
        }
        //当token过期redis中取不到数据会抛异常
        try {
            Map<String, Object> map = redisUtils.getCacheObject(token);
            UserInfoToRedis userInfoToRedis = JSON.parseObject(JSON.toJSONString(map), UserInfoToRedis.class);
            if (ObjectUtils.isEmpty(userInfoToRedis)) {
                throw new BusinessException(BusinessErrorEnum.PLEASE_LOGIN);
            }
            UserUtils.set(userInfoToRedis);
            LoginContextHolder.setLogined(true);
            return true;
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
            return true;
        }
    }

    /**
     * 该方法也是需要当前对应的拦截器的preHandle方法的返回值为true时才会执行。
     * 该方法将在整个请求完成之后，也就是DispatcherServlet渲染了视图执行。
     * （这个方法的主要作用是用于清理资源的）
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        UserUtils.clear();
        LoginContextHolder.clear();
    }

}
