package upc.c505.config.web.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import upc.c505.context.LoginContextHolder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 最后一个拦截器，如果没有认证会拦截所有请求；在postHandle中也可以做一些关闭资源的操作（如删除ThreadLocal）
 * @author qiutian
 */
public class PostResponseInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (LoginContextHolder.getIsLogined()) {
            return true;
        }
        sendUnauthorizedMessage(response);
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        LoginContextHolder.clear();
    }

    private void sendUnauthorizedMessage(HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-type", "text/html; charset=utf-8");
        response.setHeader("errorMsg", "未登录");
        response.sendError(401, "未登录或未授权");
    }
}
