package me.wuwenbin.chika.service;

import me.wuwenbin.chika.model.bean.Result;
import me.wuwenbin.chika.model.entity.CKUser;

/**
 * created by Wuwenbin on 2019/3/21 at 16:51
 */
public interface AuthService extends PublicService {

    /**
     * 注册用户
     *
     * @param chiKaUser
     * @param chiKaPass
     * @param nickname
     */
    void userRegister(String chiKaUser, String chiKaPass, String nickname) throws Exception;

    /**
     * 发送注册验证邮件
     *
     * @param email
     */
    void sendMailCode(String email);

    /**
     * 重置密码至初始123456
     *
     * @param email
     * @return
     */
    Result resetPass(String email) throws Exception;

    /**
     * 统计邮箱和用户名的数量
     *
     * @param userParam
     * @return
     */
    int countEmailAndUsername(String userParam);


    /**
     * 查找登录用户是否存在
     *
     * @param username
     * @param password
     * @return
     */
    CKUser findLoginUser(String username, String password);

    /**
     * 根据openid查找用户
     *
     * @param openId
     * @return
     */
    CKUser findByQqOpenId(String openId, Boolean enable);

    /**
     * 统计nickname
     *
     * @param nickname
     * @return
     */
    int countNickname(String nickname);

}
