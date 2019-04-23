package me.wuwenbin.chika.controller.auth;

import cn.hutool.cache.Cache;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import me.wuwenbin.chika.controller.BaseController;
import me.wuwenbin.chika.model.bean.Result;
import me.wuwenbin.chika.model.bean.login.GithubLoginData;
import me.wuwenbin.chika.model.bean.login.QqLoginData;
import me.wuwenbin.chika.model.bean.login.SimpleLoginData;
import me.wuwenbin.chika.model.constant.CKConstant;
import me.wuwenbin.chika.model.constant.CKKey;
import me.wuwenbin.chika.model.entity.CKParam;
import me.wuwenbin.chika.model.entity.CKUser;
import me.wuwenbin.chika.service.AuthService;
import me.wuwenbin.chika.service.LoginService;
import me.wuwenbin.chika.service.ParamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * created by Wuwenbin on 2019/3/19 at 14:53
 *
 * @author wuwenbin
 */
@Controller
public class AuthController extends BaseController {

    private final HttpServletRequest request;
    private final ParamService paramService;
    private final AuthService authService;
    private final LoginService<Result, SimpleLoginData> simpleLoginService;
    private final LoginService<Result, QqLoginData> qqLoginService;
    private final LoginService<Result, GithubLoginData> githubLoginService;
    private final Cache<String, String> codeCache;

    @Autowired
    public AuthController(HttpServletRequest request,
                          @Qualifier("simpleLoginService") LoginService<Result, SimpleLoginData> simpleLoginService,
                          @Qualifier("qqLoginService") LoginService<Result, QqLoginData> qqLoginService,
                          ParamService paramService,
                          AuthService authService,
                          Cache<String, String> codeCache,
                          @Qualifier("githubLoginService") LoginService<Result, GithubLoginData> githubLoginService) {
        this.request = request;
        this.simpleLoginService = simpleLoginService;
        this.qqLoginService = qqLoginService;
        this.paramService = paramService;
        this.authService = authService;
        this.codeCache = codeCache;
        this.githubLoginService = githubLoginService;
    }


    @GetMapping("/register")
    public String register() {
        boolean isOpenRegister = paramService.isSetSendMailServer();
        request.setAttribute("isOpenRegister", isOpenRegister);
        return isOpenRegister ? "register" : "redirect:/";
    }

    @PostMapping("/sendMailCode")
    @ResponseBody
    public Result sendMailCode(String email) {
        try {
            authService.sendMailCode(email);
            return Result.ok("发送成功，请在您的邮箱中查收！");
        } catch (Exception e) {
            return Result.error("发送验证码发生错误，错误信息：" + e.getMessage());
        }
    }

    @PostMapping("/reset")
    @ResponseBody
    public Result forgot(String email) {
        try {
            return authService.resetPass(email);
        } catch (Exception e) {
            return Result.error("重置密码出错，错误信息：" + e.getMessage());
        }
    }

    @PostMapping("/registration")
    @ResponseBody
    public Result doRegister(String chiKaUser, String chiKaPass, String mailCode, String nickname) {
        int min = 4, minPass = 6, max = 20;
        if (StringUtils.isEmpty(chiKaUser) || StringUtils.isEmpty(chiKaPass) || StringUtils.isEmpty(mailCode) || StringUtils.isEmpty(nickname)) {
            return Result.error("所填信息不完整！");
        } else if (chiKaUser.length() < min || chiKaUser.length() > max || chiKaPass.length() < minPass) {
            return Result.error("所填信息不规范！");
        } else {
            String sessionMailCode = codeCache.get(chiKaUser + "-" + CKConstant.MAIL_CODE_KEY);
            if (mailCode.equalsIgnoreCase(sessionMailCode)) {
                if (authService.countEmailAndUsername(chiKaUser) == 0) {
                    try {
                        authService.userRegister(chiKaUser, chiKaPass, nickname);
                        return Result.ok("注册成功！", CKConstant.LOGIN_URL);
                    } catch (Exception e) {
                        return Result.error("注册失败，错误信息：" + e.getMessage());
                    }
                } else {
                    return Result.error("已存在此邮箱，请勿重复注册！");
                }
            } else {
                return Result.error("注册失败，验证码错误或过期！");
            }
        }
    }


    @GetMapping("/login")
    public String login() {
        CKUser sessionUser = getSessionUser(request);
        if (sessionUser != null) {
            if (sessionUser.getRole() == CKConstant.ROLE_ADMIN) {
                return "redirect:" + CKConstant.MANAGEMENT_INDEX;
            } else {
                return "redirect:" + CKConstant.FRONTEND_INDEX;
            }
        } else {
            CKParam appId = paramService.findByName(CKKey.QQ_APP_ID.key());
            CKParam githubClientId = paramService.findByName(CKKey.GITHUB_CLIENT_ID.key());
            request.setAttribute("isOpenRegister", paramService.isSetSendMailServer());
            request.setAttribute("isOpenForgot", paramService.isSetSendMailServer());
            request.setAttribute("isOpenQqLogin",
                    (appId != null && StrUtil.isNotEmpty(appId.getValue())));
            request.setAttribute("isOpenGithubLogin",
                    (githubClientId != null && StrUtil.isNotEmpty(githubClientId.getValue())));
            return "login";
        }
    }

    @RequestMapping("/api/qq")
    public String qqLogin() {
        String callbackDomain = basePath(request).concat("api/qqCallback");
        CKParam appId = paramService.findByName(CKKey.QQ_APP_ID.key());
        if (appId == null || StringUtils.isEmpty(appId.getValue())) {
            request.getSession().setAttribute("errorMessage", "未设置QQ登录相关参数！");
            return "redirect:/error?errorCode=403";
        } else {
            return "redirect:https://graph.qq.com/oauth2.0/authorize?response_type=code&client_id="
                    + appId.getValue() + "&redirect_uri=" + callbackDomain + "&state=" + System.currentTimeMillis();
        }
    }

    @RequestMapping("/api/github")
    public String githubLogin() {
        String callbackDomain = basePath(request).concat("api/githubCallback");
        CKParam githubClientId = paramService.findByName(CKKey.GITHUB_CLIENT_ID.key());
        if (githubClientId == null || StringUtils.isEmpty(githubClientId.getValue())) {
            request.getSession().setAttribute("errorMessage", "未设置GITHUB登录相关参数！");
            return "redirect:/error?errorCode=403";
        } else {
            return "redirect:https://github.com/login/oauth/authorize?response_type=code&client_id="
                    + githubClientId.getValue() + "&redirect_uri=" + callbackDomain + "&state=" + CKConstant.GITHUB_AUTH_STATE;
        }
    }


    @RequestMapping("/api/{callbackType}")
    public String qqCallback(String code, @PathVariable("callbackType") String callbackType) {
        String qq = "qqCallback", github = "githubCallback";
        Result r;
        if (qq.equals(callbackType)) {
            String callbackDomain = basePath(request).concat("api/qqCallback");
            r = qqLoginService.doLogin(QqLoginData.builder().callbackDomain(callbackDomain).code(code).build());
        } else if (github.equals(callbackType)) {
            String callbackDomain = basePath(request).concat("api/githubCallback");
            r = githubLoginService.doLogin(GithubLoginData.builder().callbackDomain(callbackDomain).code(code).build());
        } else {
            request.setAttribute("message", "暂未支持其他类型的登录！");
            return "redirect:/error?errorCode=404";
        }
        if (r.get(Result.CODE).equals(Result.SUCCESS)) {
            setSessionUser(request, (CKUser) r.get(CKConstant.SESSION_USER_KEY));
            return "redirect:" + r.get(Result.DATA);
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
