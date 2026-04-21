package upc.c505.common;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.stereotype.Service;

/**
 * 用户基础信息
 * @author ly
 */
@Data
@Accessors(chain = true)
public class UserUtils {

    private static final ThreadLocal<UserInfoToRedis> LOCAL = new ThreadLocal<>();

    public static void set(UserInfoToRedis data) {
        LOCAL.set(data);
    }

    public static UserInfoToRedis get() {
        return LOCAL.get();
    }

    public static void clear() {
        LOCAL.remove();
    }
}
