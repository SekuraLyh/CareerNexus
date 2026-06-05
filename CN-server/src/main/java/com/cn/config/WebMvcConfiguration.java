package com.cn.config;

import com.cn.interceptor.JwtTokenInterceptor;
import com.cn.json.JacksonObjectMapper;
import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import springfox.documentation.oas.annotations.EnableOpenApi;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.List;

/**
 * Web MVC 配置类：注册拦截器、Knife4j 接口文档、JSON 消息转换器
 */
@Configuration
@EnableOpenApi
@EnableKnife4j
@Slf4j
public class WebMvcConfiguration extends WebMvcConfigurationSupport {

    @Autowired
    private JwtTokenInterceptor jwtTokenInterceptor;

    /**
     * 注册自定义拦截器
     */
    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        log.info("开始注册自定义拦截器...");
        // 管理端接口拦截（排除登录接口）
        registry.addInterceptor(jwtTokenInterceptor)
                .addPathPatterns("/admin/**")
                .excludePathPatterns("/admin/health");
        // 用户端接口拦截（按需扩展）
        // registry.addInterceptor(jwtTokenUserInterceptor)
        //         .addPathPatterns("/user/**")
        //         .excludePathPatterns("/user/user/login");
    }

    /**
     * 通过 Knife4j 生成接口文档 — 管理端
     */
    @Bean
    public Docket adminDocket() {
        log.info("开始创建 Knife4j 管理端接口文档...");
        ApiInfo apiInfo = new ApiInfoBuilder()
                .title("CareerNexus 接口文档")
                .version("1.0")
                .description("CareerNexus 招聘求职平台接口文档")
                .contact(new Contact("CareerNexus", "", ""))
                .build();
        return new Docket(DocumentationType.OAS_30)
                .groupName("管理端接口")
                .apiInfo(apiInfo)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.cn.controller.admin"))
                .paths(PathSelectors.any())
                .build();
    }

    /**
     * 通过 Knife4j 生成接口文档 — 用户端
     */
    @Bean
    public Docket userDocket() {
        log.info("开始创建 Knife4j 用户端接口文档...");
        ApiInfo apiInfo = new ApiInfoBuilder()
                .title("CareerNexus 接口文档")
                .version("1.0")
                .description("CareerNexus 招聘求职平台接口文档")
                .contact(new Contact("CareerNexus", "", ""))
                .build();
        return new Docket(DocumentationType.OAS_30)
                .groupName("用户端接口")
                .apiInfo(apiInfo)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.cn.controller.user"))
                .paths(PathSelectors.any())
                .build();
    }

    /**
     * 设置静态资源映射（Knife4j 文档页面）
     */
    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        log.info("开始进行静态资源映射...");
        registry.addResourceHandler("/doc.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    /**
     * 扩展消息转换器：使用 JacksonObjectMapper 处理 Java 8 时间类型
     */
    @Override
    protected void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        log.info("扩展消息转换器...");
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setObjectMapper(new JacksonObjectMapper());
        converters.add(0, converter);
    }
}
