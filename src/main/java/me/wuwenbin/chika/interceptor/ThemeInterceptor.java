package me.wuwenbin.chika.interceptor;

import me.wuwenbin.chika.dao.ChiKaParamDao;
import me.wuwenbin.chika.model.constant.ChiKaKey;
import me.wuwenbin.chika.model.entity.ChiKaParam;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * created by Wuwenbin on 2019/3/15 at 19:32
 */
public class ThemeInterceptor implements HandlerInterceptor {

    private ChiKaParamDao paramDao;

    public ThemeInterceptor(ChiKaParamDao paramDao) {
        this.paramDao = paramDao;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        String view = modelAndView.getViewName();
        ChiKaParam themeId = paramDao.selectValueByName(ChiKaKey.THEME_ID.key());
        String viewName;
        if (StringUtils.isEmpty(themeId.getValue())) {
            viewName = view;
        } else {
            viewName = "theme::" + themeId.getValue() + "/" + view;
        }
        modelAndView.setViewName(viewName);
    }
}