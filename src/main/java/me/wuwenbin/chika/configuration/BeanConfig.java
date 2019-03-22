package me.wuwenbin.chika.configuration;

import cn.hutool.cache.Cache;
import cn.hutool.cache.CacheUtil;
import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * created by Wuwenbin on 2019/3/19 at 16:52
 */
@Configuration
public class BeanConfig {


    /**
     * 验证码配置
     *
     * @return
     */
    @Bean
    @Qualifier("captchaProducer")
    public DefaultKaptcha kaptcha() {
        DefaultKaptcha kaptcha = new DefaultKaptcha();
        Properties properties = new Properties();
        properties.setProperty(Constants.KAPTCHA_BORDER, "yes");
        properties.setProperty(Constants.KAPTCHA_BORDER_COLOR, "220,220,220");
        properties.setProperty(Constants.KAPTCHA_TEXTPRODUCER_FONT_COLOR, "38,29,12");
        properties.setProperty(Constants.KAPTCHA_IMAGE_WIDTH, "147");
        properties.setProperty(Constants.KAPTCHA_IMAGE_HEIGHT, "34");
        properties.setProperty(Constants.KAPTCHA_TEXTPRODUCER_FONT_SIZE, "25");
        properties.setProperty(Constants.KAPTCHA_SESSION_KEY, "code");
        properties.setProperty(Constants.KAPTCHA_TEXTPRODUCER_CHAR_LENGTH, "4");
        properties.setProperty(Constants.KAPTCHA_TEXTPRODUCER_FONT_NAMES, "Arial");
        properties.setProperty(Constants.KAPTCHA_NOISE_COLOR, "164,128,55");
        properties.setProperty(Constants.KAPTCHA_OBSCURIFICATOR_IMPL, "com.google.code.kaptcha.impl.ShadowGimpy");
        properties.setProperty(Constants.KAPTCHA_TEXTPRODUCER_CHAR_STRING, "0123456789");
        Config config = new Config(properties);
        kaptcha.setConfig(config);
        return kaptcha;
    }


    /**
     * 密码输入错误次数缓存
     *
     * @return
     */
    @Bean
    public Cache<String, AtomicInteger> passwordRetryCache() {
        return CacheUtil.newTimedCache(10 * 60 * 1000);
    }

    /**
     * 验证码缓存，10分钟有效
     *
     * @return
     */
    @Bean
    public Cache<String, String> mailCodeCache() {
        return CacheUtil.newTimedCache(10 * 60 * 1000);
    }


    /**
     * 添加全局拦截的error移除处理类
     *
     * @return
     */
    @Bean
    public WebServerFactoryCustomizer<ConfigurableServletWebServerFactory> containerCustomizer() {
        return factory -> factory.addErrorPages(
                new ErrorPage(HttpStatus.NOT_FOUND, "/error?errorCode=404"),
                new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/error?errorCode=500"),
                new ErrorPage(Throwable.class, "/error?errorCode=500"));
    }

}
