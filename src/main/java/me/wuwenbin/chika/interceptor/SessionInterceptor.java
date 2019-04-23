package me.wuwenbin.chika.interceptor;

import cn.hutool.core.map.MapUtil;
import lombok.extern.slf4j.Slf4j;
import me.wuwenbin.chika.controller.BaseController;
import me.wuwenbin.chika.model.constant.CKConstant;
import me.wuwenbin.chika.model.constant.CKKey;
import me.wuwenbin.chika.model.constant.CKValue;
import me.wuwenbin.chika.model.entity.CKLogger;
import me.wuwenbin.chika.model.entity.CKParam;
import me.wuwenbin.chika.model.entity.CKUser;
import me.wuwenbin.chika.service.ParamService;
import me.wuwenbin.chika.util.CKUtils;
import me.wuwenbin.data.jdbc.ancestor.AncestorDao;
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

    private ParamService paramService;

    public SessionInterceptor(ParamService paramService) {
        this.paramService = paramService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        CKParam param = paramService.findByName(CKKey.SYSTEM_INIT_STATE.key());
        if (param == null || !CKValue.ENABLE.strVal().equals(param.getValue())) {
            response.sendRedirect(CKConstant.INIT_URL);
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView mv) throws Exception {
        String username = "";
        if (mv != null) {
            CKUser user = getSessionUser(request);
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
        boolean develop = CKUtils.getBean(Environment.class).getProperty(key, Boolean.class, true);

        String ipAddr = getRemoteAddress(request);
        String res = paramService.findByName(CKKey.STATISTIC_ANALYSIS_ONOFF.key()).getValue();
        boolean openAnalysis = Integer.valueOf(res) == CKValue.ENABLE.intVal();
        if (openAnalysis) {
            CKLogger logger = CKLogger.builder()
                    .ipAddr(ipAddr)
                    .ipInfo(develop ? "开发中内网地址" : CKUtils.getIpInfo(ipAddr).getAddress())
                    .sessionId(request.getSession().getId())
                    .time(new Date())
                    .url(request.getRequestURL().toString())
                    .userAgent(request.getHeader("User-Agent"))
                    .username(username)
                    .requestMethod(request.getMethod())
                    .contentType(request.getContentType())
                    .build();
            String sql = "insert into(ip_addr,ip_info,session_id,time,url,user_agent,username,request_method,content_type)" +
                    "values(:ipAddr,:ipInfo,:sessionId,:time,:url,:userAgent,:username,:requestMethod,:contentType)";
            CKUtils.getBean(AncestorDao.class).executeBean(sql, logger);
        }
    }
}
