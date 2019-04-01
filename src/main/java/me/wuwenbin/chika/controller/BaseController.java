package me.wuwenbin.chika.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import me.wuwenbin.chika.model.bean.Result;
import me.wuwenbin.chika.model.constant.ChiKaConstant;
import me.wuwenbin.chika.model.entity.ChiKaUser;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

import static org.springframework.util.StringUtils.isEmpty;

/**
 * created by Wuwenbin on 2019/3/15 at 21:06
 */
public abstract class BaseController {

    private static final String UNKNOWN = "unknown";

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
     * 获取实际ip地址
     *
     * @param request
     * @return
     */
    protected static String getRemoteAddress(HttpServletRequest request) {
        String remoteAddress;
        try {
            remoteAddress = request.getHeader("x-forwarded-for");
            if (StringUtils.isEmpty(remoteAddress) || UNKNOWN.equalsIgnoreCase(remoteAddress)) {
                remoteAddress = request.getHeader("Proxy-Client-IP");
            }
            if (StringUtils.isEmpty(remoteAddress) || remoteAddress.length() == 0 || UNKNOWN.equalsIgnoreCase(remoteAddress)) {
                remoteAddress = request.getHeader("WL-Proxy-Client-IP");
            }
            if (StringUtils.isEmpty(remoteAddress) || UNKNOWN.equalsIgnoreCase(remoteAddress)) {
                remoteAddress = request.getHeader("HTTP_CLIENT_IP");
            }
            if (StringUtils.isEmpty(remoteAddress) || UNKNOWN.equalsIgnoreCase(remoteAddress)) {
                remoteAddress = request.getHeader("HTTP_X_FORWARDED_FOR");
            }
            if (StringUtils.isEmpty(remoteAddress) || UNKNOWN.equalsIgnoreCase(remoteAddress)) {
                remoteAddress = request.getRemoteAddr();
            }
        } catch (Exception var3) {
            remoteAddress = request.getRemoteAddr();
        }
        return remoteAddress;
    }

    /**
     * 统一处理ajax的请求返回信息
     *
     * @param request
     * @param response
     * @throws IOException
     */
    protected void handleAjaxRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //用户未登录或登录时效过期，请重新登录！
        final String message = "\u7528\u6237\u672a\u767b\u5f55\u6216\u767b\u5f55\u65f6\u6548\u8fc7\u671f\uff0c\u8bf7\u91cd\u65b0\u767b\u5f55\uff01";
        response.setHeader("Content-type", "text/html;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        if (isRouter(request)) {
            JSONObject jsonObject = JSONUtil.createObj();
            jsonObject.putAll(Result.custom(Result.LOGIN_INVALID, message, ChiKaConstant.LOGIN_URL));
            response.getWriter().write(jsonObject.toString());
        } else if (isAjaxRequest(request) && !isRouter(request)) {
            JSONObject jsonObject = JSONUtil.createObj();
            jsonObject.putAll(Result.custom(Result.LOGIN_INVALID, message, ChiKaConstant.LOGIN_URL));
            response.getWriter().write(jsonObject.toString());
        } else {
            response.sendRedirect(ChiKaConstant.LOGIN_URL);
        }
    }

    /**
     * 基路径
     *
     * @param request
     * @return
     */
    protected static String basePath(HttpServletRequest request) {
        return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
        //        return "http://wuwenbin.me/";
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

    protected void setSessionUser(HttpServletRequest request, ChiKaUser user) {
        request.getSession().setAttribute(ChiKaConstant.SESSION_USER_KEY, user);
        //30分钟
        request.getSession().setMaxInactiveInterval(30 * 60);
    }

    protected ChiKaUser getSessionUser(HttpServletRequest request) {
        return (ChiKaUser) request.getSession().getAttribute(ChiKaConstant.SESSION_USER_KEY);
    }

    protected void invalidSessionUser(HttpServletRequest request) {
        request.getSession().removeAttribute(ChiKaConstant.SESSION_USER_KEY);
        request.getSession().invalidate();
    }
}
