package me.wuwenbin.chika.interceptor;

import me.wuwenbin.chika.controller.BaseController;
import me.wuwenbin.chika.model.constant.ChiKaConstant;
import me.wuwenbin.chika.model.entity.ChiKaUser;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 前台用户需要登录的操作url拦截器
 * /token开头的
 * created by Wuwenbin on 2019/3/15 at 20:58
 */
public class UserInterceptor extends BaseController implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        ChiKaUser user = getSessionUser();
        return user != null
                && (user.getRole() == ChiKaConstant.ROLE_ADMIN || user.getRole() == ChiKaConstant.ROLE_USER);
    }
}
