package upc.c505.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

import java.util.Optional;
import java.util.function.Function;

/**
 * @author qiutian
 */
@Configuration
@EnableSwagger2WebMvc
public class Knife4jConfig {
    /**
     * 定义分隔符
     */
    private static final String SPLIT = ";";

    @Bean(value = "mainPhrDocument")
    public Docket mainPhrDocument() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(new ApiInfoBuilder()
                        .title("人房户管理")
                        .description("未来乡村项目")
                        .version("1.0")
                        .build())
                //分组名称
                .groupName("人房户管理")
                .select()
                //这里指定Controller扫描包路径
                .apis(RequestHandlerSelectors.basePackage("upc.c505.modular.phr.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    @Bean(value = "mainMoneyDocument")
    public Docket mainMoneyDocument() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(new ApiInfoBuilder()
                        .title("财务提款申请")
                        .description("未来乡村项目")
                        .version("1.0")
                        .build())
                //分组名称
                .groupName("财务提款申请")
                .select()
                //这里指定Controller扫描包路径
                .apis(RequestHandlerSelectors.basePackage("upc.c505.modular.money.controller"))
                .paths(PathSelectors.any())
                .build();
    }
    @Bean(value = "mainVillageDocument")
    public Docket mainVillageDocument() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(new ApiInfoBuilder()
                        .title("美丽乡村建设")
                        .description("未来乡村项目")
                        .version("1.0")
                        .build())
                //分组名称
                .groupName("美丽乡村建设")
                .select()
                //这里指定Controller扫描包路径
                .apis(RequestHandlerSelectors.basePackage("upc.c505.modular.village.controller"))
                .paths(PathSelectors.any())
                .build();
    }
    @Bean(value = "mainPartyBuildingDocument")
    public Docket mainPartyBuildingDocument() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(new ApiInfoBuilder()
                        .title("数字党建")
                        .description("未来乡村项目")
                        .version("1.0")
                        .build())
                //分组名称
                .groupName("数字党建")
                .select()
                //这里指定Controller扫描包路径
                .apis(RequestHandlerSelectors.basePackage("upc.c505.modular.partybuilding.controller"))
                .paths(PathSelectors.any())
                .build();
    }
    @Bean(value = "mainProsperityDocument")
    public Docket mainProsperityDocument() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(new ApiInfoBuilder()
                        .title("产业共富")
                        .description("未来乡村项目")
                        .version("1.0")
                        .build())
                //分组名称
                .groupName("产业共富")
                .select()
                //这里指定Controller扫描包路径
                .apis(RequestHandlerSelectors.basePackage("upc.c505.modular.prosperity.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    @Bean(value = "mainGrassrootsDocument")
    public Docket mainGrassrootsDocument() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(new ApiInfoBuilder()
                        .title("基层治理")
                        .description("未来乡村项目")
                        .version("1.0")
                        .build())
                //分组名称
                .groupName("基层治理")
                .select()
                //这里指定Controller扫描包路径
                .apis(RequestHandlerSelectors.basePackage("upc.c505.modular.grassroots.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    @Bean(value = "mainGovserviceDocument")
    public Docket mainGovserviceDocument() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(new ApiInfoBuilder()
                        .title("政务服务")
                        .description("未来乡村项目")
                        .version("1.0")
                        .build())
                //分组名称
                .groupName("政务服务")
                .select()
                //这里指定Controller扫描包路径
                .apis(RequestHandlerSelectors.basePackage("upc.c505.modular.govservice.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    @Bean(value = "authenticationDocument")
    public Docket auth() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(new ApiInfoBuilder()
                        .title("权限部分")
                        .description("未来乡村项目")
                        .version("1.0")
                        .build())
                //分组名称
                .groupName("权限部分")
                .select()
                //这里指定Controller扫描包路径
                .apis(RequestHandlerSelectors.basePackage("upc.c505.modular.auth.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    @Bean(value = "baseDocument")
    public Docket base() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(new ApiInfoBuilder()
                        .title("基础功能")
                        .description("未来乡村项目")
                        .version("1.0")
                        .build())
                //分组名称
                .groupName("基础功能")
                .select()
                //这里指定Controller扫描包路径
                .apis(basePackage("upc.c505.modular.dict.controller" + SPLIT + "upc.c505.modular.fileupload.controller" + SPLIT
                        + "upc.c505.modular.cascade.controller" + SPLIT + "upc.c505.modular.filemanage.controller" + SPLIT))
                .paths(PathSelectors.any())
                .build();
    }

    @Bean(value = "peopleManagement")
    public Docket peopleManagement() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(new ApiInfoBuilder()
                        .title("人员管理")
                        .description("未来乡村项目")
                        .version("1.0")
                        .build())
                //分组名称
                .groupName("人员管理")
                .select()
                //这里指定Controller扫描包路径
                .apis(RequestHandlerSelectors.basePackage("upc.c505.modular.people.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    @Bean(value = "employeeTrain")
    public Docket employeeTrain() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(new ApiInfoBuilder()
                        .title("企业人员培训")
                        .description("未来乡村项目")
                        .version("1.0")
                        .build())
                //分组名称
                .groupName("企业人员培训")
                .select()
                //这里指定Controller扫描包路径
                .apis(RequestHandlerSelectors.basePackage("upc.c505.modular.employeetrain.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    @Bean(value = "mainVillageIot")
    public Docket mainVillageIot(){
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(new ApiInfoBuilder()
                        .title("乡村IOT")
                        .description("未来乡村项目")
                        .version("1.0")
                        .build())
                //分组名称
                .groupName("乡村IOT")
                .select()
                //这里指定Controller扫描包路径
                .apis(RequestHandlerSelectors.basePackage("upc.c505.modular.villageiot.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    @Bean(value = "supenterpriseDocument")
    public Docket supenterpriseDocument() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(new ApiInfoBuilder()
                        .title("市场主体")
                        .description("未来乡村项目")
                        .version("1.0")
                        .build())
                //分组名称
                .groupName("市场主体")
                .select()
                //这里指定Controller扫描包路径
                .apis(RequestHandlerSelectors.basePackage("upc.c505.modular.supenterprise.controller"))
                .paths(PathSelectors.any())
                .build();
    }
    // 测试新地址
    @Bean(value = "visualsceneDocument")
    public Docket visualsceneDocument() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(new ApiInfoBuilder()
                        .title("可视化场景")
                        .description("未来乡村项目")
                        .version("1.0")
                        .build())
                //分组名称
                .groupName("可视化场景")
                .select()
                //这里指定Controller扫描包路径
                .apis(RequestHandlerSelectors.basePackage("upc.c505.modular.visualscene.controller"))
                .paths(PathSelectors.any())
                .build();
    }
    @Bean(value = "gridGridDocument")
    public Docket gridGridDocument() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(new ApiInfoBuilder()
                        .title("地图网格配置")
                        .description("未来乡村项目")
                        .version("1.0")
                        .build())
                //分组名称
                .groupName("地图网格配置")
                .select()
                //这里指定Controller扫描包路径
                .apis(RequestHandlerSelectors.basePackage("upc.c505.modular.mapgridconfig.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    @Bean(value = "maintainDocument")
    public Docket maintainDocument() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(new ApiInfoBuilder()
                        .title("万善乡管护模块")
                        .description("未来乡村项目")
                        .version("1.0")
                        .build())
                //分组名称
                .groupName("万善乡管护模块")
                .select()
                //这里指定Controller扫描包路径
                .apis(RequestHandlerSelectors.basePackage("upc.c505.modular.maintain.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    @Bean(value = "ecoChainDocument")
    public Docket ecoChainDocument() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(new ApiInfoBuilder()
                        .title("生态链管理")
                        .description("未来乡村项目")
                        .version("1.0")
                        .build())
                //分组名称
                .groupName("生态链管理")
                .select()
                //这里指定Controller扫描包路径
                .apis(RequestHandlerSelectors.basePackage("upc.c505.modular.ecochain.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    @Bean(value = "deepseekDocument")
    public Docket deepseekDocument() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(new ApiInfoBuilder()
                        .title("DeepSeek管理")
                        .description("未来乡村项目")
                        .version("1.0")
                        .build())
                //分组名称
                .groupName("DeepSeek管理")
                .select()
                //这里指定Controller扫描包路径
                .apis(RequestHandlerSelectors.basePackage("upc.c505.modular.deepseek.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    @Bean(value = "miniprogramDocument")
    public Docket miniprogramDocument() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(new ApiInfoBuilder()
                        .title("小程序绑定管理")
                        .description("未来乡村项目")
                        .version("1.0")
                        .build())
                //分组名称
                .groupName("小程序绑定管理")
                .select()
                //这里指定 Controller 扫描包路径
                .apis(RequestHandlerSelectors.basePackage("upc.c505.modular.miniprogram.controller"))
                .paths(PathSelectors.any())
                .build();
    }
    
    @Bean(value = "forumDocument")
    public Docket forumDocument() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(new ApiInfoBuilder()
                        .title("论坛管理")
                        .description("未来乡村项目")
                        .version("1.0")
                        .build())
                //分组名称
                .groupName("论坛管理")
                .select()
                //这里指定 Controller 扫描包路径
                .apis(RequestHandlerSelectors.basePackage("upc.c505.modular.forum.controller"))
                .paths(PathSelectors.any())
                .build();
    }



    /**
     * @param basePackage 所有包路径
     * @return Predicate<RequestHandler>
     * @author chenchen
     * @description 重写basePackage方法，使能够实现多包访问
     */
    public static java.util.function.Predicate<RequestHandler> basePackage(final String basePackage) {
        return input -> declaringClass(input).map(handlerPackage(basePackage)).orElse(true);
    }

    /**
     * @param basePackage 所有包路径
     * @return Function<Class < ?>, Boolean>
     * @author chenchen
     * @description 重写basePackage方法，使能够实现多包访问
     */
    private static Function<Class<?>, Boolean> handlerPackage(final String basePackage) {
        return input -> {
            // 循环判断匹配
            for (String strPackage : basePackage.split(SPLIT)) {
                assert input != null;
                boolean isMatch = input.getPackage().getName().startsWith(strPackage);
                if (isMatch) {
                    return true;
                }
            }
            return false;
        };
    }
    /**
     * @param input
     * @return Optional<? extends Class < ?>>
     * @author chenchen
     * @description 重写basePackage方法，使能够实现多包访问
     */
    private static Optional<Class<?>> declaringClass(RequestHandler input) {
        return Optional.ofNullable(input.declaringClass());
    }
    @Bean(value = "collectionDocument")
    public Docket collectionDocument() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(new ApiInfoBuilder()
                        .title("采集与登记模块")
                        .description("未来乡村项目-数据统计相关")
                        .version("1.0")
                        .build())

                .groupName("采集与登记模块")
                .select()

                .apis(RequestHandlerSelectors.basePackage("upc.c505.modular.collection.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    /**
     * 采集管理模块文档分组
     */
    @Bean(value = "collect")
    public Docket collect() {
        return new Docket(DocumentationType.SWAGGER_2)
                // 关键点：在这里直接 new 一个新的 ApiInfo，完全独立，不调用外部方法
                .apiInfo(new ApiInfoBuilder()
                        .title("采集管理模块接口文档")
                        .description("采集管理相关HTTP接口")
                        .version("1.0")
                        .build())
                .groupName("采集管理")
                .select()
                // 扫描你自己的包路径
                .apis(RequestHandlerSelectors.basePackage("upc.c505.modular.collect.controller"))
                .paths(PathSelectors.any())
                .build();
    }

}


