package me.wuwenbin.chika.service;

/**
 * created by Wuwenbin on 2019/3/19 at 15:11
 */
public interface LoginService<R, P> extends PublicService {

    /**
     * 登录方法
     *
     * @param param
     * @return
     */
    R doLogin(P param);
}
