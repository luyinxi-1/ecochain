package upc.c505.constant;

/**
 * 存放应用中的一些常量字段
 * @author qiutian
 */
public class SystemConst {
    /**
     * token过期时间，单位：hour
     */
    public static final Integer EXPIRED_TIME = 6;
    /**
     * 请求头“token”的名称
     */
    public static String TOKEN_NAME = "token";

    /**
     * 向地区几个县发送的请求头名称
     */
    public static String FEIGN_TOKEN_NAME = "binzhou-token";

    /**
     * 区县访问需要携带的token名称
     */
    public static String COUNTY_TOKEN_NAME = "county-token";

    private SystemConst(){}
}
