package upc.c505.constant;

/**
 * 拦截其中使用的常量类
 *
 * @author qiutian
 */
public class InterceptorConst {
        /**
         * 拦截器中排除的路径(这里的路径通常都是swagger资源等，自定义的排除路径应写在别的常量中)，这些路径不会经过拦截器
         */
        public static final String[] EXCLUDE_PATH_PATTERNS = {
                        "/", "/csrf", "/upload/public/**", "/doc.html", "/doc.html/**", "/sys-user/login",
                        "/download/**", "/webjars/**",
                        "/swagger-resources/**", "/v2/**", "/swagger-ui.html/**", "/error/**", "/new-sys-user/login",
                        "/sys-user/addUser", "/upload/train/**",
                        "/upload/cut/**", "/eco-chain-wechat/callback", "/eco-chain-dict-type/getPage",
                        "/sys-dict-data/selectDictDataByDictType", "/eco-chain-distributor/integrateRegistration",
                        "/eco-chain-data-statistics/insertDataStatistics"
        };

        /**
         * 匹配所有路径
         */
        public static final String ALL_PATH_PATTERN = "/**";

        /**
         * 权限部分（auth模块）路径前缀
         */
        public static final String[] BACK_STAGE_INTERCEPT_PATTERNS = {
                        "/sys-area/**", "/sys-auth/**", "/sys-department/**", "/sys-job/**", "/sys-role-auth/**",
                        "/sys-role/**", "/sys-user/**", "/sys-user-role/**"
        };

        /**
         * 权限部分要放行的路径
         */
        public static final String[] AUTH_PUBLIC_URL = {
                        "/sys-user/getUserAuthTree", "/sys-user/getUserInfo",
                        "/new-sys-user/getUserAuthTree", "/new-sys-user/getUserInfo",
        };

        /**
         * 自定义的放行路径
         */
        public static final String[] WEIXIN_PUBLISH_EXCLUDE_PATH_PATTERNS = {
                        // 微信公示放行的接口
                        "/social-publicity/getIndex",
                        // 视频上传获取token接口
                        "/video-api/getToken",
                        // 2023.6.12 公文-根据id获取详细信息
                        "/social-publicity/getById",
                        // 小程序要求放行的路径
                        "/sys-user/getUserOpenId",
                        "/sys-user/wechatLogin",
                        // 美丽乡村建设者、企业人员的注册，放行
                        "/village-construction-builder/insertVillageConstructionBuilder",
                        "/people-enterprise/insertPeopleEnterprise",
                        // 区域管理要求放行的接口
                        "/sys-area/getAreaPage",
                        // 小程序绑定信息获取
                        "/miniprogram/getByAppId"

        };
}
