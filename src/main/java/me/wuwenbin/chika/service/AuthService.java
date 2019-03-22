package me.wuwenbin.chika.service;

import me.wuwenbin.chika.model.bean.Result;

/**
 * created by Wuwenbin on 2019/3/21 at 16:51
 */
public interface AuthService {

    /**
     * 注册用户
     *
     * @param chiKaUser
     * @param chiKaPass
     * @param nickname
     */
    void userRegister(String chiKaUser, String chiKaPass, String nickname);

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
    Result resetPassword(String email);
}
