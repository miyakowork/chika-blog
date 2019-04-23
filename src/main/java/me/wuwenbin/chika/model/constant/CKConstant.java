package me.wuwenbin.chika.model.constant;

/**
 * 项目中用到的常量，非存储在数据库中的
 * created by Wuwenbin on 2019/3/14 at 16:22
 */
public interface CKConstant {

    /**
     * 上传文件路径的属性key
     */
    String UPLOAD_PATH_KEY = "chika.upload.path";

    /**
     * session中的user参数key
     */
    String SESSION_USER_KEY = "chika_session_user_key";

    /**
     * 发送验证码的key
     */
    String MAIL_CODE_KEY = "sendMailCodeKey";


    /**
     * github授权认证时候需要的state
     */
    String GITHUB_AUTH_STATE = "5047c2fe57f9b94a765e898cf54f63c7";

    /**
     * 用户表3个角色
     */
    int ROLE_ADMIN = 1;
    int ROLE_USER = 2;
    int ROLE_SYSTEM = 3;

    /**
     * 用户账户类型
     */
    String TYPE_SIMPLE = "simple";
    String TYPE_QQ = "qq";
    String TYPE_GITHUB = "github";

    /**
     * 网站一些页面的映射url地址
     */
    String INIT_URL = "/init";
    String LOGIN_URL = "/login";
    String FRONTEND_INDEX = "/";
    String MANAGEMENT_INDEX = "/management/index";
    String ERROR_PAGE = "/error/page";
    String ERROR_ROUTER = "/error/router";


}
