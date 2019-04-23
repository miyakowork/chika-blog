package me.wuwenbin.chika.util;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import me.wuwenbin.chika.model.bean.IpInfo;
import me.wuwenbin.data.jdbc.ancestor.AncestorDao;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 项目中使用到的一些通用工具方法
 * created by Wuwenbin on 2019/3/11 at 15:51
 */
@Slf4j
@Component
public class CKUtils implements ApplicationContextAware {

    private static ApplicationContext applicationContext = null;

    /**
     * 驼峰转下划线
     *
     * @param camelChar
     * @return
     */
    public static String camel2Underline(String camelChar) {
        return String.join("_", camelChar.replaceAll("([A-Z])", ",$1").split(",")).toLowerCase();
    }

    /**
     * 获取ip地理信息位置
     *
     * @param ip
     * @return
     */
    public static IpInfo getIpInfo(String ip) {
        String url = "http://whois.pconline.com.cn/ip.jsp?ip={}";
        url = StrUtil.format(url, ip);
        String resp = HttpUtil.get(url);
        log.info("获取 ip详细地址，参数：{}", ip);
        try {
            JSON json = JSONUtil.parse(resp);
            String result = json.toString();
            String[] res = result.split("\\s+");
            return res.length > 1 ?
                    IpInfo.builder().address(res[0]).line(res[1]).build() :
                    IpInfo.builder().address(res[0]).build();
        } catch (Exception e) {
            log.error("获取ip地理位置信息失败", e);
            return IpInfo.builder().address("地球").build();
        }
    }

    /**
     * 返回值类型为Map<String, Object>
     *
     * @param properties
     * @return
     */
    public static Map<String, Object> getParameterMap(Map<String, String[]> properties) {
        Map<String, Object> returnMap = new HashMap<>(16);
        Iterator<Map.Entry<String, String[]>> iterator = properties.entrySet().iterator();
        String name;
        String value = "";
        while (iterator.hasNext()) {
            Map.Entry<String, String[]> entry = iterator.next();
            name = entry.getKey();
            Object valueObj = entry.getValue();
            if (null == valueObj) {
                value = "";
            } else {
                String[] values = (String[]) valueObj;
                //用于请求参数中有多个相同名称
                for (String value1 : values) {
                    value = value1 + ",";
                }
                value = value.substring(0, value.length() - 1);
            }
            returnMap.put(name, value);
        }
        return returnMap;
    }

    /**
     * 获取配置文件的属性值
     *
     * @param key
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T getEnvPropertyByKey(String key, Class<T> clazz) {
        return getBean(Environment.class).getProperty(key, clazz);
    }

    /**
     * 获取Bean
     *
     * @param tClass
     * @param <T>
     * @return
     */
    public static <T> T getBean(Class<T> tClass) {
        return applicationContext.getBean(tClass);
    }

    /**
     * 获取通用Dao
     * @return
     */
    public static AncestorDao baseDao() {
        return applicationContext.getBean(AncestorDao.class);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        CKUtils.applicationContext = applicationContext;
    }
}
