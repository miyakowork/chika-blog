package me.wuwenbin.chika.configuration;

import me.wuwenbin.chika.interceptor.AdminInterceptor;
import me.wuwenbin.chika.interceptor.SessionInterceptor;
import me.wuwenbin.chika.interceptor.ThemeInterceptor;
import me.wuwenbin.chika.interceptor.TokenInterceptor;
import me.wuwenbin.chika.model.constant.CKValue;
import me.wuwenbin.chika.service.ParamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.List;

/**
 * created by Wuwenbin on 2019/3/15 at 21:21
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final ParamService paramService;
    private final Environment env;

    @Autowired
    public WebMvcConfig(ParamService paramService, Environment env) {
        this.paramService = paramService;
        this.env = env;
    }

    /**
     * 添加一些虚拟路径的映射
     * 静态资源路径和上传文件的路径
     * 如果配置了七牛云上传，则上传路径无效
     *
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations(env.getProperty("spring.resources.static-locations"));
        String uploadPath = env.getProperty("chika.upload.path");
        registry.addResourceHandler(CKValue.FILE_URL_PREFIX.strVal() + "/**").addResourceLocations(uploadPath);
    }

    /**
     * 全局拦截器
     * 顺序：系统初始化/访问日志-->用户是否登录-->后台管理用户验证-->视图主题渲染
     *
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        List<String> excludePaths = Arrays.asList("/static/**", "/error/**", "/init/**");
        List<String> excludePaths2 = Arrays.asList("/api/qq", "/api/qqCallback", "/api/github", "/api/githubCallback");
        String excludePath3 = "/management/**";
        registry.addInterceptor(new SessionInterceptor(paramService)).addPathPatterns("/**")
                .excludePathPatterns(excludePaths);
        registry.addInterceptor(new TokenInterceptor()).addPathPatterns("/token/**");
        registry.addInterceptor(new AdminInterceptor()).addPathPatterns("/management/**");
        registry.addInterceptor(new ThemeInterceptor(paramService)).addPathPatterns("/**")
                .excludePathPatterns(excludePaths).excludePathPatterns(excludePaths2)
                .excludePathPatterns(excludePath3);
    }
}
