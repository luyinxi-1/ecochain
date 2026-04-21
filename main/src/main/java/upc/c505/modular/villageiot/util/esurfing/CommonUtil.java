package upc.c505.modular.villageiot.util.esurfing;

/**
 * @author: frd
 * @create-date: 2024/6/4 15:40
 */

public class CommonUtil {
    // 重要提示！！！！： 如果要加入自己的项目工程，这里填写的参数请一定写成配置模式，不要写成固定模式，不然代码安全扫描会异常

    /**
     * 合作方申请的appId
     * 这里 不是真实的app_id，请向相关部门人员申请后获取，并填写在这里 */
    public static final String APP_ID = "9644616933";

    /**
     合作方申请的appSecret
     * 这里 不是真实的app_Secret，请向相关部门人员申请后获取，并填写在这里 */
    public static final String APP_SECRET = "6b51ad19952a4127866230b6e521c1eb";

    /**  合作方申请手机号码，请自己填写这里处理 */
    public static final String Phone = "18906496658";

    /**
     * 服务端版本号，如v1.0  通常固定是v1.0
     */
    public static final String VERSION = "v1.0";

    /**
     * 接入端类型，可选值：
     * 0-IOS
     * 1-Android
     * 2-Web/WAP/H5
     * 3-PC
     * 4-服务端
     */
    public static final String CLINT_TYPE = "2";

    /** 合作方自己生成的RSA加密秘钥中，用于本地解密的 本地私钥,这里不是真实的，请自己生成后填写这里。*/

    /** 公钥 */
    public static final String publicKey  = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCKQVnW1eezKKyMoZm+Tu8fibRy50SiY+K78OYnf990aV8GM3ZcLC7H/W1Rg6CYiSgcGSSt4u0Ads/OreSsOvGsndOXwMYqL3qZgG+K4OlTGRHGckHU7QH5Ug00pEi0/3AlfEDbAUyNFUrBJIY0MakCX/oEXmckq9pjQ/WxIJWowQIDAQAB";

    /** 私钥 */
    public static final String privateKey ="MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAIpBWdbV57MorIyhmb5O7x+JtHLnRKJj4rvw5id/33RpXwYzdlwsLsf9bVGDoJiJKBwZJK3i7QB2z86t5Kw68ayd05fAxiovepmAb4rg6VMZEcZyQdTtAflSDTSkSLT/cCV8QNsBTI0VSsEkhjQxqQJf+gReZySr2mND9bEglajBAgMBAAECgYBEfmPXv1lutPPnKBhW5Bwrb7lqQ5EJMpsdqRcc8ojtz1Y4aD53I5x5j+Acxi1F5I1ufjZhv07TMAl7x0LG4NzhfLfqI6H4PzFJsUouIvCIUP72ZG4AnwIdXsio8sokZjSdWaSWIIcRXab0LJvbimpbVTX8z94tv9pu2T/saDfpkQJBAOrwhgOsAqP58Eg8hq7iLgmnM88OACelaQIPcTq5913rH69hUe+WuXZGZoP7iDsOF4cfUPBS7xxRgVM9TY14n3MCQQCWphUV2fFKBBuG89lLPOSFF9HotfOjsCuB7sDEdZxa1VLlDFJ/lPLGmHojRQBf5ecY7j0tNjH7FJPyjyeeTqH7AkBDUdZk2PYMvIKggE9rbz4X4ARVcUKUGa6kO36LgFwkWkpC+9T5nSvZe0TGzt1zZo9RVaTgqDMB30z6aBd/0S+HAkBGkTexS6zuOexXE+TGjJiTC6xlMM4W8BABRkCABoOePO0eo94FK1Vp18TmvozFXyEYmYiIpClXbrU62vn4+uZ/AkEAqXs9/ZW/vlE5YDA8uaI7ewH0S8+wHBzTs6CNg9YAYZBnHELgRqq/+jrtAZ/YfkFbrd3FJmY3HD/1y1afPn73YQ==";

}
