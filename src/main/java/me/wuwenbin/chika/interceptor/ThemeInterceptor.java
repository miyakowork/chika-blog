package me.wuwenbin.chika.interceptor;

import cn.hutool.core.util.StrUtil;
import me.wuwenbin.chika.controller.BaseController;
import me.wuwenbin.chika.model.constant.CKKey;
import me.wuwenbin.chika.model.entity.CKParam;
import me.wuwenbin.chika.service.ParamService;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * created by Wuwenbin on 2019/3/15 at 19:32
 */
public class ThemeInterceptor extends BaseController implements HandlerInterceptor {

    private ParamService paramService;

    public ThemeInterceptor(ParamService paramService) {
        this.paramService = paramService;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView mav) {
        if (!isAjaxRequest(request)) {
            if (mav != null && !mav.isEmpty() && mav.hasView()) {
                String view = mav.getViewName();
                CKParam themeId = paramService.findByName(CKKey.THEME_ID.key());
                String viewName;
                CKParam homeThemeUrl = paramService.findByName(CKKey.THEME_HOMEPAGE_ONOFF.key());
                if ("/".equals(request.getRequestURI())) {
                    viewName = StrUtil.isNotEmpty(homeThemeUrl.getValue()) ? "homepage/" + view : view;
                } else if (StringUtils.isEmpty(themeId.getValue())) {
                    viewName = view;
                } else {
                    viewName = themeId.getValue() + "/" + view;
                }
                mav.setViewName(viewName);
            }
        }
    }
}
