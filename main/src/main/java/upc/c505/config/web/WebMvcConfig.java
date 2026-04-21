package upc.c505.config.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import upc.c505.config.web.interceptor.CountyRequestInterceptor;
import upc.c505.config.web.interceptor.PostResponseInterceptor;
import upc.c505.config.web.interceptor.RequestInterceptor;
import upc.c505.config.web.interceptor.SystemApplicationInterceptor;
import upc.c505.constant.DateFormatConst;
import upc.c505.constant.InterceptorConst;
import upc.c505.constant.SymbolConst;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static upc.c505.constant.InterceptorConst.*;
import static upc.c505.constant.InterceptorConst.WEIXIN_PUBLISH_EXCLUDE_PATH_PATTERNS;

/**
 * MVC的配置类
 *
 * @author qiutian
 */
@Configuration
@Slf4j
public class WebMvcConfig implements WebMvcConfigurer {
    @Bean
    public MethodValidationPostProcessor methodValidationPostProcessor() {
        return new MethodValidationPostProcessor();
    }

    @Bean
    public RequestInterceptor requestInterceptor() {
        return new RequestInterceptor();
    }

    @Bean
    public SystemApplicationInterceptor systemApplicationInterceptor() {
        return new SystemApplicationInterceptor();
    }

//    @Bean
//    public CountyRequestInterceptor countyRequestInterceptor() {
//        return new CountyRequestInterceptor();
//    }

    @Bean
    public PostResponseInterceptor postResponseInterceptor() {
        return new PostResponseInterceptor();
    }

    /**
     * 配置跨域过滤器，允许跨域访问
     */
    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOriginPattern(SymbolConst.ASTERISK);
        config.setAllowCredentials(true);
        config.addAllowedMethod(SymbolConst.ASTERISK);
        config.addAllowedHeader(SymbolConst.ASTERISK);
        UrlBasedCorsConfigurationSource configSource = new UrlBasedCorsConfigurationSource();
        configSource.registerCorsConfiguration(ALL_PATH_PATTERN, config);
        return new CorsFilter(configSource);
    }

    /**
     * 将前端传过来的日期映射为LocalDateTime或LocalDate
     * <br/>
     * <b>注意：这里不要换成lambda表达式</b>
     *
     * @param registry 注册器
     */
    @Override
    public void addFormatters(FormatterRegistry registry) {
//        添加LocalDateTime类的映射
        registry.addConverter(
                new Converter<String, LocalDateTime>() {
                    @Override
                    public LocalDateTime convert(String source) {
                        return LocalDateTime.parse(source, DateTimeFormatter.ofPattern(DateFormatConst.DATE_TIME_COMMON_FORMAT_PATTERN));
                    }
                }
        );

//        添加LocalDate类的映射
        registry.addConverter(
                new Converter<String, LocalDate>() {
                    @Override
                    public LocalDate convert(String source) {
                        return LocalDate.parse(source, DateTimeFormatter.ofPattern(DateFormatConst.DATE_COMMON_FORMAT_PATTERN));
                    }
                }
        );
    }

    /**
     * 静态资源映射
     *
     * @author qiutian
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //swagger增强的静态资源映射
        registry.addResourceHandler("doc.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
        //flowable设计器静态资源映射
        registry.addResourceHandler("/designer/**").addResourceLocations("classpath:/designer/");
        File files = new File("upload");
        String path = files.getAbsolutePath();
        registry.addResourceHandler("/upload/**").addResourceLocations("file:" + path + "/");
    }

    /**
     * 拦截器配置，用于在处理请求前或响应后统一处理信息
     *
     * @param registry 注册器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
//        校验普通的token
        registry.addInterceptor(requestInterceptor())
//    拦截路径
                .addPathPatterns(ALL_PATH_PATTERN)
//    放行路径
                .excludePathPatterns(EXCLUDE_PATH_PATTERNS)
                .excludePathPatterns(WEIXIN_PUBLISH_EXCLUDE_PATH_PATTERNS)
                .order(1);
//        拦截权限模块
//        registry.addInterceptor(systemApplicationInterceptor())
//                .addPathPatterns(BACK_STAGE_INTERCEPT_PATTERNS)
//                .excludePathPatterns(EXCLUDE_PATH_PATTERNS)
//                .excludePathPatterns(WEIXIN_PUBLISH_EXCLUDE_PATH_PATTERNS)
//                .excludePathPatterns(AUTH_PUBLIC_URL)
//                .order(2);
//        校验区县的请求
//        registry.addInterceptor(countyRequestInterceptor())
//                .addPathPatterns(ALL_PATH_PATTERN)
//                .excludePathPatterns(EXCLUDE_PATH_PATTERNS)
//                .excludePathPatterns(WEIXIN_PUBLISH_EXCLUDE_PATH_PATTERNS)
//                .order(3);
//        最后拦截并统一处理
        registry.addInterceptor(postResponseInterceptor())
                .addPathPatterns(ALL_PATH_PATTERN)
                .excludePathPatterns(EXCLUDE_PATH_PATTERNS)
                .excludePathPatterns(WEIXIN_PUBLISH_EXCLUDE_PATH_PATTERNS)
                .order(1000);
    }
}