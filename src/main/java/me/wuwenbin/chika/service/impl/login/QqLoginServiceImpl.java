package me.wuwenbin.chika.service.impl.login;

import cn.hutool.core.map.MapUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import me.wuwenbin.chika.model.bean.Result;
import me.wuwenbin.chika.model.bean.login.QqLoginData;
import me.wuwenbin.chika.model.constant.CKConstant;
import me.wuwenbin.chika.model.constant.CKKey;
import me.wuwenbin.chika.model.entity.CKUser;
import me.wuwenbin.chika.service.AuthService;
import me.wuwenbin.chika.service.LoginService;
import me.wuwenbin.chika.service.ParamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

/**
 * created by Wuwenbin on 2019/3/19 at 15:49
 */
@Slf4j
@Service("qqLoginService")
public class QqLoginServiceImpl implements LoginService<Result, QqLoginData> {

    private final AuthService authService;
    private final ParamService paramService;

    @Autowired
    public QqLoginServiceImpl(AuthService authService, ParamService paramService) {
        this.authService = authService;
        this.paramService = paramService;
    }

    @Override
    public Result doLogin(QqLoginData data) {
        try {
            String appId = paramService.findByName(CKKey.QQ_APP_ID.key()).getValue();
            String appKey = paramService.findByName(CKKey.QQ_APP_KEY.key()).getValue();

            Map<String, Object> p1 = MapUtil.of("grant_type", "authorization_code");
            p1.put("client_id", appId);
            p1.put("client_secret", appKey);
            p1.put("code", data.getCode());
            p1.put("redirect_uri", data.getCallbackDomain());

            String resp1 = HttpUtil.get("https://graph.qq.com/oauth2.0/token", p1);
            String accessToken = resp1.substring(13, resp1.length() - 66);
            String callback = HttpUtil.get("https://graph.qq.com/oauth2.0/me", MapUtil.of("access_token", accessToken));
            String openId = callback.substring(45, callback.length() - 6);

            Map<String, Object> p2 = MapUtil.of("access_token", accessToken);
            p2.put("oauth_consumer_key", appId);
            p2.put("openid", openId);

            JSONObject json2 = JSONUtil.parseObj(HttpUtil.get("https://graph.qq.com/user/get_user_info", p2));
            if (json2.getInt("ret") == 0) {
                CKUser user = authService.findByQqOpenId(openId, true);
                if (user != null) {
                    return Result.ok("授权成功！", "/").put(CKConstant.SESSION_USER_KEY, user);
                } else {
                    CKUser lockedUser = authService.findByQqOpenId(openId, false);
                    if (lockedUser != null) {
                        return Result.error("QQ登录授权失败，原因：用户已被锁定！");
                    }
                    String nickname = json2.getStr("nickname");
                    int cnt = authService.countNickname(nickname);
                    nickname = cnt > 0 ? nickname + new java.util.Date().getTime() : nickname;
                    String avatar = json2.getStr("figureurl_qq_2").replace("http://", "https://");
                    CKUser registerUser = CKUser.builder()
                            .role(CKConstant.ROLE_USER).create(new Date())
                            .nickname(nickname).avatar(avatar).qqOpenId(openId)
                            .accountType(CKConstant.TYPE_QQ).enable(1)
                            .build();
                    String registerUserSql = "insert into chika_user(role,`create`,nickname,avatar,qq_open_id,account_type,enable) " +
                            "values(:role,:create,:nickname,:avatar,:qqOpenId,:accountType,:enable)";
                    CKUser registeredUser = baseDao().insertBeanAutoGenKeyReturnBean(registerUserSql, registerUser, CKUser.class, "id");
                    return Result.ok("授权成功！", "/").put(CKConstant.SESSION_USER_KEY, registerUser);
                }
            } else {
                String errorMsg = json2.getStr("msg");
                log.error("QQ登录授权失败，原因：{}", errorMsg);
                return Result.error("QQ登录授权失败，原因：{}", errorMsg);
            }
        } catch (StringIndexOutOfBoundsException e) {
            log.error("[accessToken] 返回值有误！");
            return Result.error("请求重复或返回 [accessToken] 值有误！");
        } catch (Exception e) {
            log.error("QQ登录授权失败，原因：{}", e);
            return Result.error("QQ登录授权失败，原因：{}", e.getMessage());
        }
    }
}
