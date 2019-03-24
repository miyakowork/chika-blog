package me.wuwenbin.chika.interceptor;

import cn.hutool.core.map.MapUtil;
import lombok.extern.slf4j.Slf4j;
import me.wuwenbin.chika.controller.BaseController;
import me.wuwenbin.chika.dao.ChiKaLoggerDao;
import me.wuwenbin.chika.dao.ChiKaParamDao;
import me.wuwenbin.chika.model.constant.ChiKaConstant;
import me.wuwenbin.chika.model.constant.ChiKaKey;
import me.wuwenbin.chika.model.constant.ChikaValue;
import me.wuwenbin.chika.model.entity.ChiKaLogger;
import me.wuwenbin.chika.model.entity.ChiKaParam;
import me.wuwenbin.chika.model.entity.ChiKaUser;
import me.wuwenbin.chika.util.ChiKaKit;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

/**
 * created by Wuwenbin on 2019/3/15 at 20:37
 */
@Slf4j
public class SessionInterceptor extends BaseController implements HandlerInterceptor {

    private ChiKaParamDao paramDao;

    public SessionInterceptor(ChiKaParamDao paramDao) {
        this.paramDao = paramDao;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        ChiKaParam param = paramDao.findByName(ChiKaKey.SYSTEM_INIT_STATE.key());
        if (param == null || !ChikaValue.ENABLE.strVal().equals(param.getValue())) {
            response.sendRedirect(ChiKaConstant.INIT_URL);
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView mv) {
        String username = "";
        if (mv != null) {
            ChiKaUser user = getSessionUser(request);
            if (user == null) {
                //sessionUser为空
                mv.getModelMap().addAttribute("su", null);
            } else {
                username = user.getUsername();
                Map<Object, Object> userMap = MapUtil.of(new Object[][]{
                        {"id", user.getId()},
                        {"username", user.getUsername()},
                        {"nickname", user.getNickname()},
                        {"avatar", user.getAvatar()},
                        {"role", user.getRole()}});
                //统一设置会话用户的值，不需要再controller中设置
                mv.getModelMap().addAttribute("su", userMap);
            }
        }

        final String key = "chika.develop";
        boolean develop = ChiKaKit.getBean(Environment.class).getProperty(key, Boolean.class, true);

        String ipAddr = getRemoteAddress(request);
        String res = paramDao.findByName(ChiKaKey.STATISTIC_ANALYSIS_ONOFF.key()).getValue();
        boolean openAnalysis = Integer.valueOf(res) == ChikaValue.ENABLE.intVal();
        if (openAnalysis) {
            ChiKaLogger logger = ChiKaLogger.builder()
                    .ipAddr(ipAddr)
                    .ipInfo(develop ? "开发中内网地址" : ChiKaKit.getIpInfo(ipAddr).getAddress())
                    .sessionId(request.getSession().getId())
                    .time(new Date())
                    .url(request.getRequestURL().toString())
                    .userAgent(request.getHeader("User-Agent"))
                    .username(username)
                    .requestMethod(request.getMethod())
                    .contentType(request.getContentType())
                    .build();
            ChiKaKit.getBean(ChiKaLoggerDao.class).insertTemplate(logger);
        }
    }
}
