package me.wuwenbin.chika.service;

import javax.servlet.http.HttpServletRequest;

/**
 * created by Wuwenbin on 2019/3/21 at 16:51
 */
public interface RegisterService {

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
     * @param request
     */
    void sendMailCode(String email, HttpServletRequest request);
}
