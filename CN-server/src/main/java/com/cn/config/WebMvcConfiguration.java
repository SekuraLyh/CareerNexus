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
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Contact;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.oas.annotations.EnableOpenApi;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

import java.util.Collections;
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
     * 注册 JWT 令牌校验拦截器
     * <p>
     * 拦截策略：除登录、注册、健康检查、Swagger 文档外，所有请求都必须携带有效 Token
     */
    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        log.info("注册 JWT 拦截器...");
        registry.addInterceptor(jwtTokenInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/auth/login",                 // 登录
                        "/auth/register/**",           // 注册
                        "/admin/health",               // 健康检查（首页）
                        // ===== Swagger / Knife4j =====
                        "/doc.html",
                        "/webjars/**",
                        "/v3/api-docs/**",
                        "/swagger-resources/**",
                        "/swagger-ui/**",
                        "/swagger-ui.html",
                        "/favicon.ico",
                        "/error"
                );
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
                .securitySchemes(Collections.singletonList(apiKey()))
                .securityContexts(Collections.singletonList(securityContext()))
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.cn.controller"))
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
                .securitySchemes(Collections.singletonList(apiKey()))
                .securityContexts(Collections.singletonList(securityContext()))
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.cn.controller.auth"))
                .paths(PathSelectors.any())
                .build();
    }

    /**
     * Swagger 全局 Bearer Token 认证方案 — 让 Knife4j 界面出现 "Authorize" 按钮
     */
    private ApiKey apiKey() {
        return new ApiKey("BearerToken", "Authorization", "header");
    }

    /**
     * 认证上下文：将所有接口纳入 BearerToken 安全方案
     */
    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .build();
    }

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope scope = new AuthorizationScope("global", "accessEverything");
        return Collections.singletonList(new SecurityReference("BearerToken", new AuthorizationScope[]{scope}));
    }

    /**
     * 设置静态资源映射（Knife4j / Swagger 文档页面）
     */
    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        log.info("开始进行静态资源映射...");
        registry.addResourceHandler("/doc.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
        registry.addResourceHandler("/swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/swagger-ui/**").addResourceLocations("classpath:/META-INF/resources/webjars/springfox-swagger-ui/");
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
