package me.wuwenbin.chika.configuration;

import cn.hutool.core.util.StrUtil;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import lombok.extern.slf4j.Slf4j;
import me.wuwenbin.data.jdbc.ancestor.AncestorDao;
import me.wuwenbin.data.jdbc.factory.DaoFactory;
import me.wuwenbin.data.jdbc.factory.business.DataSourceX;
import me.wuwenbin.data.jdbc.factory.business.DbType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 访问数据库的一些配置
 * created by Wuwenbin on 2018/7/14 at 9:26
 *
 * @author wuwenbin
 */
@Slf4j
@Configuration
public class DSConfig {

    private final Environment env;

    @Autowired
    public DSConfig(Environment env) {
        this.env = env;
    }

    /**
     * 配置数据源
     * 读取application-chika.properties中的配置
     * 若没有检测到配置值，则使用缺省默认值
     *
     * @return
     */
    @Bean("druidDs")
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


    @Bean
    public DataSourceX dataSourceX(@Qualifier("druidDs") DataSource dataSource) {
        DataSourceX dataSourceX = new DataSourceX();
        dataSourceX.setDataSource(dataSource);
        dataSourceX.setInitDbType(DbType.Mysql);
        return dataSourceX;
    }


    @Bean
    public DaoFactory daoFactory(DataSourceX dataSourceX) {
        DaoFactory daoFactory = new DaoFactory();
        Map<String, DataSourceX> multiDao = new ConcurrentHashMap<>(2);
        multiDao.put("ckb", dataSourceX);
        daoFactory.setDataSourceMap(multiDao);
        daoFactory.setDefaultDao(dataSourceX);
        return daoFactory;
    }


    @Bean
    public AncestorDao publicDao(DaoFactory daoFactory) {
        return daoFactory.defaultDao;
    }
}
