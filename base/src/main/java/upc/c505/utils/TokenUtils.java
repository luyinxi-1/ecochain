package upc.c505.utils;

import cn.hutool.core.lang.Dict;
import cn.hutool.jwt.JWTUtil;

import java.nio.charset.StandardCharsets;

/**
 * 生成token的工具类，生成的token用于通过县区的校验
 *
 * @author qiutian
 */
public class TokenUtils {
    /**
     * 参与jwt生成的密钥
     */
    private static final String KEY = "qiutian";

    /**
     * 生成token
     *
     * @return token
     */
    public static String generateToken(Long id, String userCode) {
        return JWTUtil.createToken(
                Dict.create()
                        .set("id", id)
                        .set("userCode", userCode)
                        .set("sign", "binzhou"),
                KEY.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 用于验证token是否合法
     *
     * @return 是否合法
     */
    public static boolean verifyToken(String token, String key) {
        return JWTUtil.verify(token, key.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 用于验证token是否合法，KEY为默认值
     * @param token 要验证的token
     * @return 是否合法
     */
    public static boolean verifyToken(String token) {
        return verifyToken(token, KEY);
    }
}
