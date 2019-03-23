package me.wuwenbin.chika.controller.auth;

import cn.hutool.cache.Cache;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import me.wuwenbin.chika.controller.BaseController;
import me.wuwenbin.chika.dao.ChiKaParamDao;
import me.wuwenbin.chika.dao.ChiKaUserDao;
import me.wuwenbin.chika.model.bean.Result;
import me.wuwenbin.chika.model.bean.login.QqLoginData;
import me.wuwenbin.chika.model.bean.login.SimpleLoginData;
import me.wuwenbin.chika.model.constant.ChiKaConstant;
import me.wuwenbin.chika.model.constant.ChiKaKey;
import me.wuwenbin.chika.model.constant.ChikaValue;
import me.wuwenbin.chika.model.entity.ChiKaParam;
import me.wuwenbin.chika.model.entity.ChiKaUser;
import me.wuwenbin.chika.service.AuthService;
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
import java.util.HashMap;
import java.util.Map;

/**
 * created by Wuwenbin on 2019/3/19 at 14:53
 *
 * @author wuwenbin
 */
@Controller
public class AuthController extends BaseController {

    private final HttpServletRequest request;
    private final ChiKaParamDao paramDao;
    private final ChiKaUserDao userDao;
    private final ParamService paramService;
    private final AuthService authService;
    private final LoginService<Result, SimpleLoginData> simpleLoginService;
    private final LoginService<Result, QqLoginData> qqLoginService;
    private final Cache<String, String> codeCache;

    @Autowired
    public AuthController(HttpServletRequest request,
                          @Qualifier("simpleLoginService") LoginService<Result, SimpleLoginData> simpleLoginService,
                          @Qualifier("qqLoginService") LoginService<Result, QqLoginData> qqLoginService,
                          ChiKaParamDao paramDao,
                          ParamService paramService,
                          AuthService authService,
                          Cache<String, String> codeCache,
                          ChiKaUserDao userDao) {
        this.request = request;
        this.simpleLoginService = simpleLoginService;
        this.qqLoginService = qqLoginService;
        this.paramDao = paramDao;
        this.paramService = paramService;
        this.authService = authService;
        this.codeCache = codeCache;
        this.userDao = userDao;
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
        return authService.resetPassword(email);
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
            String sessionMailCode = codeCache.get(chiKaUser + "-" + ChiKaConstant.MAIL_CODE_KEY);
            if (mailCode.equalsIgnoreCase(sessionMailCode)) {
                if (userDao.countEmailAndUsername(chiKaUser) == 0) {
                    authService.userRegister(chiKaUser, chiKaPass, nickname);
                    return Result.ok("注册成功！", ChikaValue.LOGIN_URL.strVal());
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
        ChiKaUser sessionUser = getSessionUser(request);
        if (sessionUser != null) {
            if (sessionUser.getRole() == ChiKaConstant.ROLE_ADMIN) {
                return "redirect:" + ChikaValue.MANAGEMENT_INDEX.strVal();
            } else {
                return "redirect:" + ChikaValue.FRONTEND_INDEX.strVal();
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

    @RequestMapping("/api/github")
    public String githubLogin() {
        String callbackDomain = basePath(request).concat("api/githubCallback");
        return "redirect:https://github.com/login/oauth/authorize?response_type=code&client_id="
                + "2ecaad2a302d76470ab5" + "&redirect_uri=" + callbackDomain + "&state=6655";
    }

    @RequestMapping("/api/githubCallback")
    public String githubCallback(HttpServletRequest request, String code) {
        String callbackDomain = basePath(request).concat("api/githubCallback");
        String url = "https://github.com/login/oauth/access_token";
        Map<String, Object> pMap = new HashMap<>();
        pMap.put("client_id", "2ecaad2a302d76470ab5");
        pMap.put("client_secret", "de9da207effcdabe09279371e10c08a8c5af2b0d");
        pMap.put("code", code);
        pMap.put("redirect_uri", callbackDomain);
        pMap.put("state", "6655");
        String res = HttpUtil.post(url, pMap);
        String accessToken = res.substring(13, res.indexOf("&scope"));
        String userInfoUrl = "https://api.github.com/user?access_token={}";
        userInfoUrl = StrUtil.format(userInfoUrl, accessToken);
        String s = HttpUtil.get(userInfoUrl);
       JSONObject jsonObject= JSONUtil.parseObj(s);
       String username=jsonObject.getStr("login");
       String email=jsonObject.getStr("email");
       String avatar=jsonObject.getStr("avatar_url");
       String nickname=jsonObject.getStr("name");
        return "";
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