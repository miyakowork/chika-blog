package me.wuwenbin.chika.model.constant;

/**
 * created by Wuwenbin on 2019/3/13 at 14:23
 */
public enum ChikaValue {

    /**
     * 以下表示两个对立的值
     * eg：开启/关闭、是/否、有/没有、显示/不显示等
     */
    ENABLE(1),
    DISABLE(0),

    /**
     * 一些数值
     * 分页的数值、文章摘要的数值、评论的内容长度等
     */
    DEFAULT_PAGE_SIZE(12),
    DEFAULT_SUMMARY_LENGTH(243),
    COMMENT_LENGTH(200),

    /**
     * 主题信息
     * ID和NAME
     */
    DEFAULT_THEME_ID(""),
    DEFAULT_THEME_NAME("默认主题"),

    /**
     * level级别
     * 分为最高、极高、高、中等、低、极低、最低
     */
    HIGHEST(300),
    HIGHER(200),
    HIGH(100),
    MID(0),
    LOW(-100),
    LOWER(-200),
    LOWEST(-300),


    /**
     * 文件相关值
     * 文件类型访问url前缀等
     */
    IMG("/img"),
    FILE("/file"),
    FILE_URL_PREFIX("/chika_files"),

    /**
     * 服务器上传文件方式
     */
    LOCAL_SERVER("local_server"),
    QINIU_SERVER("qiniu_server"),

    /**
     * 导航栏菜单值
     */
    MENU_HOME_NAME("主页"),
    MENU_NOTE_NAME("笔记"),
    MENU_PROJECT_NAME("作品"),
    MENU_FILE_NAME("文件"),
    MENU_PROFILE_NAME("关于"),
    MENU_SEARCH_NAME("搜索"),

    /**
     * 网站的一些初始值
     */
    WEBSITE_TITLE("千夏博客"),
    WEBSITE_LOGO_WORDS("LOGO WORD"),
    WEBSITE_LOGO_SMALL_WORDS("这是一个小标题"),
    FOOTER_WORDS("此处一般可写一些备案号之类的文字"),
    INDEX_TOP_WORDS("写下你的座右铭吧"),
    COMMENT_TOP_NOTICE("遵守国家法律法规，请勿回复无意义内容，请不要回复嵌套过多的楼层！"),
    PROJECT_TOP_WORDS("此页为本人的一些小项目，欢迎大家留言在github star我。"),
    MESSAGE_TOP_NOTICE("欢迎大家留言，有什么问题、建议、意见或者疑问可随时提出，qq群：<a href=\"https://jq.qq.com/?_wv=1027&k=5ypf8jR\" target=\"_blank\">697053454</a>。<span style=\"color:red;\">请不要回复嵌套过多的楼层！</span>"),
    INFO_LABEL_CONTENT("网站的一些信息");

    Object value;

    ChikaValue(Object obj) {
        this.value = obj;
    }

    public Object val() {
        return this.value;
    }

    public String strVal() {
        return val().toString();
    }

    public int intVal() {
        return Integer.valueOf(strVal());
    }

    public double doubleVal() {
        return Double.valueOf(strVal());
    }

}
