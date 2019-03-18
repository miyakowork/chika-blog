package me.wuwenbin.chika.interceptor;

import me.wuwenbin.chika.dao.ChiKaParamDao;
import me.wuwenbin.chika.model.constant.ChiKaKey;
import me.wuwenbin.chika.model.constant.ChikaValue;
import me.wuwenbin.chika.model.entity.ChiKaParam;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * created by Wuwenbin on 2019/3/15 at 20:37
 */
public class SessionInterceptor implements HandlerInterceptor {

    private ChiKaParamDao paramDao;

    public SessionInterceptor(ChiKaParamDao paramDao) {
        this.paramDao = paramDao;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        ChiKaParam param = paramDao.selectValueByName(ChiKaKey.SYSTEM_INIT_STATE.key());
        if (param == null || !ChikaValue.ENABLE.strVal().equals(param.getValue())) {
            response.sendRedirect(ChikaValue.INIT_URL.strVal());
        }
        return false;
    }

}
