package me.wuwenbin.chika.model.constant;

/**
 * 项目中用到的常量
 * created by Wuwenbin on 2019/3/14 at 16:22
 */
public interface ChiKaConstant {

    /**
     * 上传文件路径的属性key
     */
    String UPLOAD_PATH_KEY = "chika.upload.path";

    /**
     * 用户表3个角色
     */
    int ROLE_ADMIN = 1;
    int ROLE_USER = 2;
    int ROLE_SYSTEM = 3;

    /**
     * session中的user参数key
     */
    String SESSION_USER_KEY = "chika_session_user_key";


}
