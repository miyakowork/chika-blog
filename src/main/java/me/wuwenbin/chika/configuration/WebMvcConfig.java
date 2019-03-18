package me.wuwenbin.chika.configuration;

import me.wuwenbin.chika.dao.ChiKaParamDao;
import me.wuwenbin.chika.interceptor.AdminInterceptor;
import me.wuwenbin.chika.interceptor.SessionInterceptor;
import me.wuwenbin.chika.interceptor.ThemeInterceptor;
import me.wuwenbin.chika.interceptor.UserInterceptor;
import me.wuwenbin.chika.model.constant.ChikaValue;
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

    private final ChiKaParamDao paramDao;
    private final Environment env;

    @Autowired
    public WebMvcConfig(ChiKaParamDao paramDao, Environment env) {
        this.paramDao = paramDao;
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
        registry.addResourceHandler(ChikaValue.FILE_URL_PREFIX.strVal() + "/**").addResourceLocations(uploadPath);
    }

    /**
     * 全局拦截器
     * 顺序：系统初始化-->用户是否登录-->后台管理用户验证-->视图主题渲染
     *
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        List<String> excludePaths = Arrays.asList("/static/**", "/error/**", "/init/**");
        registry.addInterceptor(new SessionInterceptor(paramDao)).addPathPatterns("/**").excludePathPatterns(excludePaths);
        registry.addInterceptor(new UserInterceptor()).addPathPatterns("/token/**");
        registry.addInterceptor(new AdminInterceptor()).addPathPatterns("/management/**");
        registry.addInterceptor(new ThemeInterceptor(paramDao)).addPathPatterns("/**").excludePathPatterns(excludePaths);
    }
}
