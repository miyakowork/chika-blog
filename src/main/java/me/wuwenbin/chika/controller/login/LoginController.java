package me.wuwenbin.chika.controller.login;

import cn.hutool.crypto.SecureUtil;
import com.google.code.kaptcha.Constants;
import me.wuwenbin.chika.controller.BaseController;
import me.wuwenbin.chika.dao.ChiKaParamDao;
import me.wuwenbin.chika.model.bean.Result;
import me.wuwenbin.chika.model.bean.login.QqLoginModel;
import me.wuwenbin.chika.model.bean.login.SimpleLoginModel;
import me.wuwenbin.chika.model.constant.ChiKaConstant;
import me.wuwenbin.chika.model.constant.ChiKaKey;
import me.wuwenbin.chika.model.entity.ChiKaParam;
import me.wuwenbin.chika.model.entity.ChiKaUser;
import me.wuwenbin.chika.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * created by Wuwenbin on 2019/3/19 at 14:53
 */
@Controller
public class LoginController extends BaseController {

    private final HttpServletRequest request;
    private final ChiKaParamDao paramDao;
    private final LoginService<Result, SimpleLoginModel> simpleLoginService;
    private final LoginService<Result, QqLoginModel> qqLoginService;

    @Autowired
    public LoginController(HttpServletRequest request,
                           @Qualifier("simpleLoginService") LoginService<Result, SimpleLoginModel> simpleLoginService,
                           @Qualifier("qqLoginService") LoginService<Result, QqLoginModel> qqLoginService, ChiKaParamDao paramDao) {
        this.request = request;
        this.simpleLoginService = simpleLoginService;
        this.qqLoginService = qqLoginService;
        this.paramDao = paramDao;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @RequestMapping("/api/qq")
    public String qqLogin() {
        String callbackDomain = basePath(request).concat("api/qqCallback");
        ChiKaParam appId = paramDao.findByName(ChiKaKey.QQ_APP_ID.key());
        if (appId == null || StringUtils.isEmpty(appId.getValue())) {
            return "redirect:/error?errorCode=404";
        } else {
            return "redirect:https://graph.qq.com/oauth2.0/authorize?response_type=code&client_id="
                    + appId.getValue() + "&redirect_uri=" + callbackDomain + "&state=" + System.currentTimeMillis();
        }
    }

    @RequestMapping("/api/qqCallback")
    public String qqCallback(HttpServletRequest request, String code) {
        String callbackDomain = basePath(request).concat("api/qqCallback");
        Result r = qqLoginService.doLogin(QqLoginModel.builder().callbackDomain(callbackDomain).code(code).build());
        if (r.get("code").equals(200)) {
            setSessionUser((ChiKaUser) r.get(ChiKaConstant.SESSION_USER_KEY));
            return "redirect:" + r.get("data");
        } else {
            return "redirect:/error?errorCode=404";
        }
    }

    @PostMapping("/login")
    @ResponseBody
    public Result login(SimpleLoginModel model) {
        if (StringUtils.isEmpty(model.getChiKaCode())) {
            return Result.error("验证码为空！");
        } else {
            String code = (String) request.getSession().getAttribute(Constants.KAPTCHA_SESSION_KEY);
            if (code == null) {
                return Result.custom(-1, "请刷新页面");
            }
            if (!code.equalsIgnoreCase(model.getChiKaCode())) {
                return Result.error("验证码错误！");
            }
        }
        model.setChiKaPass(SecureUtil.md5(model.getChiKaPass()));
        return simpleLoginService.doLogin(model);
    }

    @GetMapping(value = {"/token/logout", "/management/logout"})
    public String logout() {
        invalidSessionUser(request);
        return "redirect:/";
    }

}
