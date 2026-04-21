package upc.c505.config.web.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;
import upc.c505.common.UserUtils;
import upc.c505.context.LoginContextHolder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 这个拦截器用于拦截非管理员用户访问
 * @author qiutian
 */
public class SystemApplicationInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (!LoginContextHolder.getIsLogined()) {
            return false;
        }
        return UserUtils.get().getRoleCodeList().contains("admin");
    }
}
