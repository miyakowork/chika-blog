package me.wuwenbin.chika.interceptor;

import me.wuwenbin.chika.controller.BaseController;
import me.wuwenbin.chika.model.constant.ChiKaConstant;
import me.wuwenbin.chika.model.entity.ChiKaUser;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 后台管理拦截器
 * created by Wuwenbin on 2019/3/15 at 20:54
 */
public class AdminInterceptor extends BaseController implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        ChiKaUser user = getSessionUser();
        return user != null && user.getRole() == ChiKaConstant.ROLE_ADMIN;
    }
}
