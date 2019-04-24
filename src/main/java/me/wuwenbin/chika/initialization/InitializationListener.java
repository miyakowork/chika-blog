package me.wuwenbin.chika.initialization;

import cn.hutool.core.lang.ObjectId;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import me.wuwenbin.chika.configuration.TableConfig;
import me.wuwenbin.chika.exception.PropertyErrorException;
import me.wuwenbin.chika.model.constant.CKConstant;
import me.wuwenbin.chika.model.constant.CKKey;
import me.wuwenbin.chika.model.constant.CKValue;
import me.wuwenbin.chika.model.constant.CateType;
import me.wuwenbin.chika.model.entity.CKArticle;
import me.wuwenbin.chika.model.entity.CKCate;
import me.wuwenbin.chika.model.entity.CKParam;
import me.wuwenbin.chika.model.entity.CKUser;
import me.wuwenbin.chika.service.ArticleService;
import me.wuwenbin.chika.service.CateService;
import me.wuwenbin.chika.service.ParamService;
import me.wuwenbin.chika.util.CKUtils;
import me.wuwenbin.data.jdbc.ancestor.AncestorDao;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * 数据库表初始化
 * created by Wuwenbin on 2019/3/10 at 14:13
 */
@Slf4j
@Component
public class InitializationListener implements ApplicationListener<ContextRefreshedEvent> {

    private final JdbcTemplate jdbcTemplate;
    private final TableConfig tableConfig;
    private final ParamService paramService;
    private final CateService cateService;
    private final ArticleService articleService;
    private final AncestorDao dao;
    private final Environment env;

    @Autowired
    public InitializationListener(JdbcTemplate jdbcTemplate, TableConfig tableConfig,
                                  ParamService paramService, Environment env, AncestorDao dao, CateService cateService, ArticleService articleService) {
        this.jdbcTemplate = jdbcTemplate;
        this.tableConfig = tableConfig;
        this.paramService = paramService;
        this.env = env;
        this.dao = dao;
        this.cateService = cateService;
        this.articleService = articleService;
    }


    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        log.info("「千夏博客」正在检测数据库状态，请稍后...");
        ApplicationHome home = new ApplicationHome(getClass());
        File jarFile = home.getDir();
        File file = new File(jarFile.getAbsolutePath() + "/chika.installed");
        File failedFile = new File(jarFile.getAbsolutePath() + "/chika.installed.failed");
        try {
            if (!file.exists() || failedFile.exists()) {
                initDBTableSchema();
                log.info("「千夏博客」正在检测初始化环境，请稍后...");
                String hasSystemInitStateProp = "select count(1) from chika_param where name = ?";
                long cnt = dao.queryNumberByArray(hasSystemInitStateProp, Integer.class, CKKey.SYSTEM_INIT_STATE.key());
                CKParam param = paramService.findByName(CKKey.SYSTEM_INIT_STATE.key());
                if (cnt == 0 || !CKValue.ENABLE.val().equals(param.getValue())) {
                    log.info("「千夏博客」开始初始化，请稍后...");
                    truncateAllaTables();
                    setUpParams();
                    setUpUpload();
                    setUpHelloWorld();
                    log.info("「千夏博客」初始化完成，请稍后...");
                } else {
                    log.info("「千夏博客」已完成初始化，略过初始化步骤，请稍后...");
                }
                setUpSystemStartedTime();
                log.info("「千夏博客」启动成功！");
                file.createNewFile();
                failedFile.delete();
            } else {
                log.info("「千夏博客」不需要初始化数据库，启动成功！");
            }
        } catch (Exception e) {
            try {
                failedFile.createNewFile();
            } catch (IOException ignored) {
            }
            log.error("「千夏博客」初始化数据库发生异常，请检测原因之后重试！", e);
            throw new RuntimeException("初始化数据库失败！");
        }
    }

    /**
     * 初始化数据库表
     */
    private void initDBTableSchema() {
        log.info("「千夏博客」未初始化数据库，开始执行数据库初始化，请稍后...");
        Field[] fields = ReflectUtil.getFields(TableConfig.class);
        for (Field field : fields) {
            Object executeSql = ReflectUtil.getFieldValue(tableConfig, field);
            String dropSql = "DROP TABLE IF EXISTS `{}`;";
            String tableName = CKUtils.camel2Underline(field.getName());
            jdbcTemplate.execute(StrUtil.format(dropSql, tableName));
            log.info("正在创建表「{}」，SQL：「{}」", tableName, executeSql);
            jdbcTemplate.execute(executeSql.toString());
        }
        log.info("「千夏博客」数据库初始化完成，总计{}张数据表，准备进行下一步...", fields.length);
    }

    /**
     * 删除所有表中的数据
     */
    private void truncateAllaTables() {
        Field[] fields = ReflectUtil.getFields(TableConfig.class);
        for (Field field : fields) {
            String tableName = CKUtils.camel2Underline(field.getName());
            String truncateSql = "TRUNCATE {}";
            jdbcTemplate.execute(StrUtil.format(truncateSql, tableName));
        }
    }

    /**
     * 系统参数表的一些默认值插入
     */
    private void setUpParams() throws Exception {
        log.info("「千夏博客」正在初始化系统参数，请稍后...");
        CKKey[] chiKaKeys = CKKey.values();
        for (CKKey chiKaKey : chiKaKeys) {
            CKParam p = CKParam.builder()
                    .name(chiKaKey.key())
                    .level(chiKaKey.getLevel())
                    .remark(chiKaKey.getDesc())
                    .value(chiKaKey.getVal())
                    .build();
            paramService.insertParam(p);
        }
        log.info("「千夏博客」初始化系统参数完毕，准备进行下一步...");
    }

    /**
     * 创建上传目录文件夹
     */
    private void setUpUpload() {
        log.info("「千夏博客」正在初始化文件上传目录，请稍后...");
        CKParam param = paramService.findByName(CKKey.UPLOAD_TYPE.key());
        String value = param.getValue();
        if (CKValue.LOCAL_SERVER.strVal().equalsIgnoreCase(value)) {
            String path = env.getProperty(CKConstant.UPLOAD_PATH_KEY);
            if (!StringUtils.isEmpty(path)) {
                log.info("「千夏博客」文件上传目录设置为：「{}」", path);
                path = path.replace("file:", "");
                File filePath = new File(path + "file/");
                File imgPath = new File(path + "img/");
                boolean f = false, i = false;
                if (!filePath.exists() && !filePath.isDirectory()) {
                    f = filePath.mkdirs();
                }
                if (!imgPath.exists() && !imgPath.isDirectory()) {
                    i = imgPath.mkdirs();
                }
                if (f && i) {
                    log.info("「千夏博客」创建上传目录成功：「{}」和「{}」", path + "file/", path + "img/");
                } else if (f) {
                    log.info("「千夏博客」创建上传目录失败，失败目录：「{}」，原因：「{}」 ", (path + "img/"), "已存在文件夹或文件夹创建失败");
                } else if (i) {
                    log.info("「千夏博客」创建上传目录失败，失败目录：「{}」，原因：「{}」 ", (path + "file/"), "已存在文件夹或文件夹创建失败");
                } else {
                    log.info("「千夏博客」创建上传目录失败，失败目录：「{}」和「{}」，原因：「{}」 ", (path + "img/"), (path + "file/"), "已存在文件夹或文件夹创建失败");
                }
            } else {
                log.error("上传路径未正确设置");
                throw new PropertyErrorException("上传路径未正确设置，原因 --> 文件「application-chika.properties」中属性「chika.upload.path」未设置或设置有误！");
            }
        }
    }

    /**
     * 设置系统欢迎内容
     */
    private void setUpHelloWorld() throws Exception {
        log.info("「千夏博客」正在初始化基本内容，请稍后...");
        CKCate cate = CKCate.builder()
                .cnName("默认分类")
                .name("def_cate")
                .fontIcon("fa fa-sliders")
                .orderIndex(0)
                .type(CateType.article.name())
                .build();
        long cateId = cateService.insertCate(cate);
        CKUser user = CKUser.builder()
                .role(CKConstant.ROLE_SYSTEM)
                .nickname("千夏博客系统")
                .enable(CKValue.ENABLE.intVal())
                .build();
        String sql = "insert into chika_user(role,nickname,enable) values(:role,:nickname,:enable)";
        long userId = dao.insertBeanAutoGenKeyReturnKey(sql, user);
        final String title = "欢迎使用「千夏博客」（ChiKa Blog）";
        final String content = "";
        final String textContent = "";
        final String mdContent = "";
        final String summary = "";
        CKArticle article = CKArticle.builder()
                .id(ObjectId.next())
                .authorId(userId)
                .cover("/static/assets/img/cover.png")
                .content(content)
                .textContent(textContent)
                .summary(summary)
                .mdContent(mdContent)
                .post(new Date())
                .draft(false)
                .appreciable(true)
                .title(title)
                .build();
        int insertArticleCnt = articleService.insertArticle(article);
        if (insertArticleCnt == 0) {
            log.error("初始化Hello World 文章失败！");
        }
        String cateRefer = "insert into chika_cate_refer(cate_id,refer_id) values(?,?)";
        dao.executeArray(cateRefer, cateId, article.getId());
        log.info("「千夏博客」初始化基本内容完毕。");
    }

    /**
     * 插入系统的启动时间
     */
    private void setUpSystemStartedTime() throws Exception {
        LocalDateTime now = LocalDateTime.now();
        paramService.updateValueByName(now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                CKKey.SYSTEM_STARTED_DATETIME.key());
    }


    private static class InitStrUtil {

        /**
         * 是否包含Controller或者RestController
         * 此处的ultimateTargetClass方法是用来获取被spring的cglib代理类的原始类，这样才能获取到类上面的 注解（因为cglib代理类的原理是继承原始的类成成一个子类来操作）
         *
         * @param bean
         * @return
         */
        private static boolean isControllerPresent(Object bean) {
            return AopProxyUtils.ultimateTargetClass(bean).isAnnotationPresent(RestController.class) || AopProxyUtils.ultimateTargetClass(bean).isAnnotationPresent(Controller.class);
        }


        /**
         * 获取Controller上的RequestMapping中的value值，即url的前缀部分
         *
         * @param bean
         * @return
         */
        private static String[] getPrefixUrl(Object bean) {
            String[] prefixes = new String[0];
            if (AopProxyUtils.ultimateTargetClass(bean).isAnnotationPresent(RequestMapping.class)) {
                String[] temp = AopProxyUtils.ultimateTargetClass(bean).getAnnotation(RequestMapping.class).value();
                prefixes = ArrayUtil.addAll(prefixes, temp);
            } else if (AopProxyUtils.ultimateTargetClass(bean).isAnnotationPresent(GetMapping.class)) {
                String[] temp = AopProxyUtils.ultimateTargetClass(bean).getAnnotation(GetMapping.class).value();
                prefixes = ArrayUtil.addAll(prefixes, temp);
            } else if (AopProxyUtils.ultimateTargetClass(bean).isAnnotationPresent(PostMapping.class)) {
                String[] temp = AopProxyUtils.ultimateTargetClass(bean).getAnnotation(PostMapping.class).value();
                prefixes = ArrayUtil.addAll(prefixes, temp);
            } else if (AopProxyUtils.ultimateTargetClass(bean).isAnnotationPresent(PutMapping.class)) {
                String[] temp = AopProxyUtils.ultimateTargetClass(bean).getAnnotation(PutMapping.class).value();
                prefixes = ArrayUtil.addAll(prefixes, temp);
            } else if (AopProxyUtils.ultimateTargetClass(bean).isAnnotationPresent(DeleteMapping.class)) {
                String[] temp = AopProxyUtils.ultimateTargetClass(bean).getAnnotation(DeleteMapping.class).value();
                prefixes = ArrayUtil.addAll(prefixes, temp);
            }
            return prefixes;
        }


        /**
         * 获取Controller内的方法上的RequestMapping的value值，即url的后缀部分
         *
         * @param method
         * @return
         */
        private static String[] getLastUrl(Method method) {
            String[] lasts = new String[0];
            if (method.isAnnotationPresent(RequestMapping.class)) {
                String[] temp = method.getAnnotation(RequestMapping.class).value();
                lasts = ArrayUtil.addAll(lasts, temp);
            } else if (method.isAnnotationPresent(GetMapping.class)) {
                String[] temp = method.getAnnotation(GetMapping.class).value();
                lasts = ArrayUtil.addAll(lasts, temp);
            } else if (method.isAnnotationPresent(PostMapping.class)) {
                String[] temp = method.getAnnotation(PostMapping.class).value();
                lasts = ArrayUtil.addAll(lasts, temp);
            } else if (method.isAnnotationPresent(PutMapping.class)) {
                String[] temp = method.getAnnotation(PutMapping.class).value();
                lasts = ArrayUtil.addAll(lasts, temp);
            } else if (method.isAnnotationPresent(DeleteMapping.class)) {
                String[] temp = method.getAnnotation(DeleteMapping.class).value();
                lasts = ArrayUtil.addAll(lasts, temp);
            }
            return lasts;
        }

        /**
         * 根据prefix和last获取完整url
         *
         * @param prefix
         * @param last
         * @return
         */
        private static String getCompleteUrl(String prefix, String last) {
            last = last.startsWith("/") ? last : "/".concat(last);
            return (prefix.startsWith("/") ? prefix : "/".concat(prefix)).concat("/".equals(last) ? "" : last).replaceAll("//", "/");
        }


    }
}

