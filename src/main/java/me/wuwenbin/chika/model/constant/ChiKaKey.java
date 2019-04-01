package me.wuwenbin.chika.model.constant;

import cn.hutool.core.util.StrUtil;

import static me.wuwenbin.chika.model.constant.ChikaValue.*;

/**
 * 数据库中的设置常量
 * created by Wuwenbin on 2019/3/13 at 14:21
 */
public enum ChiKaKey {

    /**
     * 系统参数是否初始化
     * 0：否，1：是
     */
    SYSTEM_INIT_STATE(DISABLE.val(), HIGHEST.intVal(), "系统参数是否初始化"),

    /**
     * 管理员是否设置
     */
    ADMIN_SET_STATE(DISABLE.val(), HIGHEST.intVal(), "管理员是否设置"),

    /**
     * 页面上的一些文字设置，包含：
     * 网站标题、
     * 网站的LOGO文字、
     * 网站LOGO旁的小字、
     * 页脚的文字
     */
    WEBSITE_TITLE(ChikaValue.WEBSITE_TITLE.val(), MID.intVal(), "网站标题"),
    WEBSITE_LOGO_WORDS(ChikaValue.WEBSITE_LOGO_WORDS.val(), MID.intVal(), "网站的LOGO文字"),
    WEBSITE_LOGO_SMALL_WORDS(ChikaValue.WEBSITE_LOGO_SMALL_WORDS.val(), MID.intVal(), "网站LOGO旁的小字"),
    FOOTER_WORDS(ChikaValue.FOOTER_WORDS.val(), MID.intVal(), "页脚的文字"),

    /**
     * 一些页面上置顶一些公告文字设置，包含如下：
     * 首页顶部的一些文字、
     * 评论面板上方的公告、
     * 项目页面顶部的文字、
     * 留言页面顶部的文字
     */
    INDEX_TOP_WORDS(ChikaValue.INDEX_TOP_WORDS.val(), MID.intVal(), "首页顶部的一些文字"),
    COMMENT_TOP_NOTICE(ChikaValue.COMMENT_TOP_NOTICE.val(), MID.intVal(), "评论面板上方的公告"),
    PROJECT_TOP_WORDS(ChikaValue.PROJECT_TOP_WORDS.val(), MID.intVal(), "项目页面顶部的文字"),
    MESSAGE_TOP_NOTICE(ChikaValue.MESSAGE_TOP_NOTICE.val(), MID.intVal(), "留言页面顶部的文字"),

    /**
     * 网站相关开关设置，包含：
     * 全局评论开放开关、
     * 是否开启留言功能、
     * 开启云文件上传（此处为七牛云）、
     * qq登录开关、
     * 访问统计开关
     */
    GLOBAL_COMMENT_ONOFF(DISABLE.val(), MID.intVal(), "全局评论开放开关"),
    MESSAGE_OPEN_ONOFF(DISABLE.val(), MID.intVal(), "是否开启留言功能"),
    OSS_UPLOAD_ONOFF(DISABLE.val(), MID.intVal(), "开启云文件上传"),
    QQ_LOGIN_ONFF(DISABLE.val(), MID.intVal(), "qq登录开关"),
    STATISTIC_ANALYSIS_ONOFF(DISABLE.val(), MID.intVal(), "访问统计开关"),

    /**
     * 导航栏功能菜单相关设置
     */
    MENU_HOME_NAME(ChikaValue.MENU_HOME_NAME.val(), MID.intVal(), StrUtil.EMPTY),
    MENU_HOME_ONOFF(ENABLE.val(), MID.intVal(), StrUtil.EMPTY),

    MENU_NOTE_NAME(ChikaValue.MENU_NOTE_NAME.val(), MID.intVal(), StrUtil.EMPTY),
    MENU_NOTE_ONOFF(DISABLE.val(), MID.intVal(), StrUtil.EMPTY),

    MENU_LINK_NAME(StrUtil.EMPTY, MID.intVal(), StrUtil.EMPTY),
    MENU_LINK_ICON(StrUtil.EMPTY, MID.intVal(), StrUtil.EMPTY),
    MENU_LINK_HREF(StrUtil.EMPTY, MID.intVal(), StrUtil.EMPTY),
    MENU_LINK_ONFF(ChikaValue.DISABLE.val(), MID.intVal(), StrUtil.EMPTY),

    MENU_PROJECT_NAME(ChikaValue.MENU_PROJECT_NAME.val(), MID.intVal(), StrUtil.EMPTY),
    MENU_PROJECT_ONOFF(ChikaValue.DISABLE.val(), MID.intVal(), StrUtil.EMPTY),

    MENU_FILE_NAME(ChikaValue.MENU_FILE_NAME.val(), MID.intVal(), StrUtil.EMPTY),
    MENU_FILE_ONFF(ChikaValue.DISABLE.val(), MID.intVal(), StrUtil.EMPTY),

    MENU_PROFILE_NAME(ChikaValue.MENU_PROFILE_NAME.val(), MID.intVal(), StrUtil.EMPTY),
    MENU_PROFILE_ONOFF(ChikaValue.DISABLE.val(), MID.intVal(), StrUtil.EMPTY),

    MENU_SEARCH_NAME(ChikaValue.MENU_SEARCH_NAME.val(), MID.intVal(), StrUtil.EMPTY),
    MENU_SEARCH_ONOFF(ChikaValue.DISABLE.val(), MID.intVal(), StrUtil.EMPTY),

    /**
     * 支付二维码
     */
    ALIPAY(StrUtil.EMPTY, MID.intVal(), "支付宝二维码"),
    WECHAT_PAY(StrUtil.EMPTY, MID.intVal(), "微信支付二维码"),

    /**
     * 第三方QQ API设置
     */
    QQ_APP_ID(StrUtil.EMPTY, LOW.intVal(), "QQ登录API APP ID"),
    QQ_APP_KEY(StrUtil.EMPTY, LOW.intVal(), "QQ登录API APP KEY"),

    /**
     * 第三方GITHUB登录API
     */
    GITHUB_CLIENT_ID(StrUtil.EMPTY, LOW.intVal(), "github登录API Client ID"),
    GITHUB_CLIENT_SECRET(StrUtil.EMPTY, LOW.intVal(), "github登录API Client Secret"),

    /**
     * 第三方七牛 API设置
     */
    QINIU_ACCESS_KEY(StrUtil.EMPTY, LOW.intVal(), "七牛上传 ACCESS KEY"),
    QINIU_SECRET_KEY(StrUtil.EMPTY, LOW.intVal(), "七牛上传 SECRET KEY"),
    QINIU_BUCKET(StrUtil.EMPTY, LOW.intVal(), "七牛上传 BUCKET"),
    QINIU_DOMAIN(StrUtil.EMPTY, LOW.intVal(), "七牛上传 DOMAIN"),

    /**
     * 邮件相关设置
     */
    MAIL_SMPT_SERVER_ADDR(StrUtil.EMPTY, LOW.intVal(), "SMTP服务器"),
    MAIL_SMPT_SERVER_PORT(StrUtil.EMPTY, LOW.intVal(), "SMTP端口号"),
    MAIL_SERVER_ACCOUNT(StrUtil.EMPTY, LOW.intVal(), "发件人邮箱"),
    MAIL_SENDER_NAME(StrUtil.EMPTY, LOW.intVal(), "发件人邮箱帐号（一般为@前面部分）"),
    MAIL_SERVER_PASSWORD(StrUtil.EMPTY, LOW.intVal(), "邮箱登入密码"),


    /**
     * 其他一些设置，如
     * 信息板内容、
     * 文章摘要文字长度、
     * 博文首页分页的pageSize大小、
     * 使用主题名称/ID
     * 是否有注定的访问首页主题（默认为空，即访问默认的博客主题首页）
     * 上传类型（七牛还是本地）
     */
    INFO_LABEL_CONTENT(ChikaValue.INFO_LABEL_CONTENT.val(), MID.intVal(), "信息板内容"),
    ARTICLE_SUMMARY_WORDS_LENGTH(DEFAULT_SUMMARY_LENGTH.val(), MID.intVal(), "文章摘要文字长度"),
    BLOG_INDEX_PAGE_SIZE(DEFAULT_PAGE_SIZE.val(), MID.intVal(), "博客列表文章数目"),
    COMMENT_LENGTH(ChikaValue.COMMENT_LENGTH.val(), MID.intVal(), "评论文字长度"),
    THEME_NAME(DEFAULT_THEME_NAME.val(), HIGHEST.intVal(), "使用的主题名字"),
    THEME_ID(DEFAULT_THEME_ID.val(), HIGHEST.intVal(), "使用的主题的唯一编号id"),
    THEME_HOMEPAGE_ONOFF(StrUtil.EMPTY, HIGHEST.intVal(), "使用的主题的默认首页url"),
    UPLOAD_TYPE(LOCAL_SERVER.val(), HIGHEST.intVal(), "文件上传方式"),

    /**
     * 系统后台菜单
     */
    ADMIN_MENU(StrUtil.EMPTY, HIGHER.intVal(), "后台管理菜单"),

    /**
     * 系统启动的时间
     */
    SYSTEM_STARTED_DATETIME(StrUtil.EMPTY, HIGHER.intVal(), "系统最近一次的启动时间");


    private Object val;
    private int level;
    private String desc;

    ChiKaKey(Object val, int level, String desc) {
        this.val = val;
        this.level = level;
        this.desc = desc;
    }

    public String getVal() {
        return this.val.toString();
    }

    public String getDesc() {
        return this.desc;
    }

    public int getLevel() {
        return this.level;
    }

    /**
     * 获取枚举属性的值
     *
     * @return 枚举属性的小写形式
     */
    public String key() {
        return this.name().toLowerCase();
    }


}
