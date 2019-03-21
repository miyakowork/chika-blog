package me.wuwenbin.chika.controller.login;

import cn.hutool.crypto.SecureUtil;
import me.wuwenbin.chika.controller.BaseController;
import me.wuwenbin.chika.dao.ChiKaParamDao;
import me.wuwenbin.chika.model.bean.Result;
import me.wuwenbin.chika.model.bean.login.QqLoginData;
import me.wuwenbin.chika.model.bean.login.SimpleLoginData;
import me.wuwenbin.chika.model.constant.ChiKaConstant;
import me.wuwenbin.chika.model.constant.ChiKaKey;
import me.wuwenbin.chika.model.constant.ChikaValue;
import me.wuwenbin.chika.model.entity.ChiKaParam;
import me.wuwenbin.chika.model.entity.ChiKaUser;
import me.wuwenbin.chika.service.LoginService;
import me.wuwenbin.chika.service.ParamService;
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
 * @author wuwenbin
 */
@Controller
public class LoginController extends BaseController {

    private final HttpServletRequest request;
    private final ChiKaParamDao paramDao;
    private final ParamService paramService;
    private final LoginService<Result, SimpleLoginData> simpleLoginService;
    private final LoginService<Result, QqLoginData> qqLoginService;

    @Autowired
    public LoginController(HttpServletRequest request,
                           @Qualifier("simpleLoginService") LoginService<Result, SimpleLoginData> simpleLoginService,
                           @Qualifier("qqLoginService") LoginService<Result, QqLoginData> qqLoginService,
                           ChiKaParamDao paramDao,
                           ParamService paramService) {
        this.request = request;
        this.simpleLoginService = simpleLoginService;
        this.qqLoginService = qqLoginService;
        this.paramDao = paramDao;
        this.paramService = paramService;
    }


    @GetMapping("/register")
    public String register() {
        boolean isOpenRegister = paramService.isSetSendMailServer();
        return isOpenRegister ? "register" : "redirect:/";
    }

    @GetMapping("/login")
    public String login() {
        ChiKaUser sessionUser = getSessionUser(request);
        if (sessionUser != null) {
            if (sessionUser.getRole() == ChiKaConstant.ROLE_ADMIN) {
                return "redirect:" + ChikaValue.MANAGEMENT_INDEX;
            } else {
                return "redirect:" + ChikaValue.FRONTEND_INDEX;
            }
        } else {
            request.setAttribute("isOpenRegister", paramService.isSetSendMailServer());
            request.setAttribute("isOpenForgot", paramService.isSetSendMailServer());
            return "login";
        }
    }

    @RequestMapping("/api/qq")
    public String qqLogin() {
        String callbackDomain = basePath(request).concat("api/qqCallback");
        ChiKaParam appId = paramDao.findByName(ChiKaKey.QQ_APP_ID.key());
        if (appId == null || StringUtils.isEmpty(appId.getValue())) {
            request.setAttribute("message", "未设置QQ登录相关参数！");
            return "redirect:/error?errorCode=404";
        } else {
            return "redirect:https://graph.qq.com/oauth2.0/authorize?response_type=code&client_id="
                    + appId.getValue() + "&redirect_uri=" + callbackDomain + "&state=" + System.currentTimeMillis();
        }
    }

    @RequestMapping("/api/qqCallback")
    public String qqCallback(HttpServletRequest request, String code) {
        String callbackDomain = basePath(request).concat("api/qqCallback");
        Result r = qqLoginService.doLogin(QqLoginData.builder().callbackDomain(callbackDomain).code(code).build());
        if (r.get(Result.CODE).equals(Result.SUCCESS)) {
            setSessionUser(request, (ChiKaUser) r.get(ChiKaConstant.SESSION_USER_KEY));
            return "redirect:" + r.get("data");
        } else {
            return "redirect:/error?errorCode=404";
        }
    }

    @PostMapping("/login")
    @ResponseBody
    public Result login(SimpleLoginData data) {
        int nameMin = 4;
        int nameMax = 20;
        int passMin = 6;
        if (StringUtils.isEmpty(data.getChiKaUser()) || StringUtils.isEmpty(data.getChiKaPass())) {
            return Result.error("邮箱/账号和密码不能为空！");
        } else if (data.getChiKaUser().length() < nameMin || data.getChiKaUser().length() > nameMax) {
            return Result.error("账号不能过长或过短！");
        } else if (data.getChiKaPass().length() < passMin) {
            return Result.error("密码填写不当！");
        } else {
            data.setChiKaPass(SecureUtil.md5(data.getChiKaPass()));
            data.setRequest(request);
            return simpleLoginService.doLogin(data);
        }
    }

    @GetMapping(value = {"/token/logout", "/management/logout"})
    public String logout() {
        invalidSessionUser(request);
        return "redirect:/";
    }

}
