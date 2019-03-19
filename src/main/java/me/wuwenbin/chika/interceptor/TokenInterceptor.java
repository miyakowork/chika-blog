package me.wuwenbin.chika.interceptor;

import lombok.extern.slf4j.Slf4j;
import me.wuwenbin.chika.controller.BaseController;
import me.wuwenbin.chika.model.entity.ChiKaUser;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 前台用户需要登录的操作url拦截器
 * /token开头的
 * created by Wuwenbin on 2019/3/15 at 20:58
 */
@Slf4j
public class TokenInterceptor extends BaseController implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        ChiKaUser sessionUser = getSessionUser(request);
        if (sessionUser == null) {
            if (isAjaxRequest(request)) {
                handleAjaxRequest(request, response);
            }
            log.info("未登录用户");
            return false;
        } else {
            log.info("已登录用户，用户昵称（账号）：「{}({})」", sessionUser.getNickname(), sessionUser.getUsername());
            return true;
        }
    }
}
