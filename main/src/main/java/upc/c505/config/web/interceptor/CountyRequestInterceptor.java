package upc.c505.config.web.interceptor;

import cn.hutool.jwt.JWTPayload;
import cn.hutool.jwt.JWTUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import upc.c505.common.UserInfoToRedis;
import upc.c505.common.UserUtils;
import upc.c505.constant.SystemConst;
import upc.c505.context.LoginContextHolder;
import upc.c505.utils.TokenUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

/**
 * 用于拦截区县请求
 * @author qiutian
 */
public class CountyRequestInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (LoginContextHolder.getIsLogined()) {
            return true;
        }
        String countyToken = request.getHeader(SystemConst.COUNTY_TOKEN_NAME);
        if (StringUtils.isBlank(countyToken)) {
            return true;
        }
        if (TokenUtils.verifyToken(countyToken)) {
            LoginContextHolder.setLogined(true);
            saveToUserUtils(countyToken);
            return true;
        }
        return true;
    }

    /**
     * 向UserUtils填充信息
     * @param countyToken token
     */
    private void saveToUserUtils(String countyToken) {
        JWTPayload payload = JWTUtil.parseToken(countyToken).getPayload();
        Object userId = payload.getClaim("id");
        System.out.println(userId);
        Object userName = payload.getClaim("userCode");
        UserInfoToRedis userInfoToRedis = new UserInfoToRedis()
                .setId(Long.valueOf(userId.toString()))
                .setUserCode(Objects.toString(userName));
        UserUtils.set(userInfoToRedis);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        LoginContextHolder.clear();
        UserUtils.clear();
    }
}
