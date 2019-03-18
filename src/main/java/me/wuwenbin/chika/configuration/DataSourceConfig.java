package me.wuwenbin.chika.configuration;

import cn.hutool.core.util.StrUtil;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import lombok.extern.slf4j.Slf4j;
import org.beetl.sql.core.Interceptor;
import org.beetl.sql.core.SQLManager;
import org.beetl.sql.ext.DebugInterceptor;
import org.beetl.sql.ext.SimpleCacheInterceptor;
import org.beetl.sql.ext.spring4.BeetlSqlDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

/**
 * 访问数据库的一些配置
 * created by Wuwenbin on 2018/7/14 at 9:26
 *
 * @author wuwenbin
 */
@Slf4j
@Configuration
public class DataSourceConfig {

    private final Environment env;

    @Autowired
    public DataSourceConfig(Environment env) {
        this.env = env;
    }

    /**
     * 配置数据源
     * 读取application-chika.properties中的配置
     * 若没有检测到配置值，则使用缺省默认值
     *
     * @return
     */
    @Bean("druidDS")
    public DataSource dataSource() {
        try {
            String ip = env.getProperty("db.ip", "127.0.0.1");
            String port = env.getProperty("db.port", "3306");
            String name = env.getProperty("db.name", "chika");
            String user = env.getProperty("db.username", "root");
            String pass = env.getProperty("db.password", "123456");
            DruidDataSource druidDataSource = DruidDataSourceBuilder.create().build();
            String url = StrUtil.format("jdbc:mysql://{}:{}/{}?useUnicode=true&characterEncoding=UTF-8&useSSL=true&serverTimezone=GMT%2B8", ip, port, name);
            druidDataSource.setUrl(url);
            druidDataSource.setUsername(user);
            druidDataSource.setPassword(pass);
            return druidDataSource;
        } catch (Exception e) {
            log.error("初始化数据源出错，错误信息：{}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * beetlsql 的数据源配置
     *
     * @param dataSource
     * @return
     */
    @Bean("beetlDS")
    public BeetlSqlDataSource beetlSqlDataSource(@Qualifier("druidDS") DataSource dataSource) {
        BeetlSqlDataSource source = new BeetlSqlDataSource();
        source.setMasterSource(dataSource);
        return source;
    }

    /**
     * 增加beetlsql的缓存功能
     *
     * @param defaultSqlManager
     * @return
     */
    @Bean
    public SQLManager sqlManager(SQLManager defaultSqlManager) {
        List<String> lcs = new ArrayList<>();
        lcs.add("chiKaParam");
        defaultSqlManager.setInters(new Interceptor[]{new DebugInterceptor(), new SimpleCacheInterceptor(lcs)});
        return defaultSqlManager;
    }

}
