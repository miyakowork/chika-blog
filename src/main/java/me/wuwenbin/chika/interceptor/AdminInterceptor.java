package me.wuwenbin.chika.interceptor;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import me.wuwenbin.chika.controller.BaseController;
import me.wuwenbin.chika.model.bean.Result;
import me.wuwenbin.chika.model.constant.CKConstant;
import me.wuwenbin.chika.model.entity.CKUser;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 后台管理拦截器
 * created by Wuwenbin on 2019/3/15 at 20:54
 */
public class AdminInterceptor extends BaseController implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        CKUser sessionUser = getSessionUser(request);
        if (sessionUser == null) {
            handleAjaxRequest(request, response);
            return false;
        } else if (sessionUser.getRole() == CKConstant.ROLE_ADMIN) {
            return true;
        } else {
            if (isAjaxRequest(request)) {
                JSONObject jsonObject = JSONUtil.createObj();
                jsonObject.putAll(Result.error("非法访问，即将跳转首页！", CKConstant.FRONTEND_INDEX));
                response.getWriter().write(jsonObject.toString());
            } else {
                response.sendRedirect(CKConstant.FRONTEND_INDEX);
            }
            return false;
        }
    }
}
