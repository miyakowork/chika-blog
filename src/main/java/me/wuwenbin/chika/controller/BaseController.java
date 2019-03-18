package me.wuwenbin.chika.controller;

import cn.hutool.core.util.StrUtil;
import me.wuwenbin.chika.model.constant.ChiKaConstant;
import me.wuwenbin.chika.model.entity.ChiKaUser;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

import static org.springframework.util.StringUtils.isEmpty;

/**
 * created by Wuwenbin on 2019/3/15 at 21:06
 */
public abstract class BaseController {

    /**
     * 获取当前请求request
     *
     * @return
     */
    protected HttpServletRequest getRequest() {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return Objects.requireNonNull(servletRequestAttributes).getRequest();
    }

    /**
     * 判断是否为ajax请求
     *
     * @param request
     * @return
     */
    protected boolean isRouter(HttpServletRequest request) {
        String headerAccept = request.getHeader("Accept");
        return !isEmpty(headerAccept) && headerAccept.contains("text/html") && !isJson(request) && isAjaxRequest(request) && isGetRequest(request);
    }

    /**
     * 是否为 json 请求
     *
     * @param request
     * @return
     */
    protected boolean isJson(HttpServletRequest request) {
        String headerAccept = request.getHeader("Accept");
        return !isEmpty(headerAccept) && headerAccept.contains("application/json");
    }

    /**
     * 判断是否为ajax请求
     *
     * @param request
     * @return
     */
    protected boolean isAjaxRequest(HttpServletRequest request) {
        return StrUtil.isNotBlank(request.getHeader("x-requested-with")) && "XMLHttpRequest".equals(request.getHeader("x-requested-with"));
    }

    /**
     * 是否为get请求
     *
     * @param request
     * @return
     */
    protected boolean isGetRequest(HttpServletRequest request) {
        String method = request.getMethod();
        return "GET".equalsIgnoreCase(method);
    }

    protected void setSessionUser(ChiKaUser user) {
        getRequest().getSession().setAttribute(ChiKaConstant.SESSION_USER_KEY, user);
    }

    protected ChiKaUser getSessionUser() {
        return (ChiKaUser) getRequest().getSession().getAttribute(ChiKaConstant.SESSION_USER_KEY);
    }

    protected void invalidSessionUser() {
        getRequest().getSession().removeAttribute(ChiKaConstant.SESSION_USER_KEY);
        getRequest().getSession().invalidate();
    }
}
