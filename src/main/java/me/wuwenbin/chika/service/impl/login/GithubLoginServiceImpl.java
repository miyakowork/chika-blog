package me.wuwenbin.chika.service.impl.login;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import me.wuwenbin.chika.model.bean.Result;
import me.wuwenbin.chika.model.bean.login.GithubLoginData;
import me.wuwenbin.chika.model.constant.CKConstant;
import me.wuwenbin.chika.model.constant.CKKey;
import me.wuwenbin.chika.model.entity.CKParam;
import me.wuwenbin.chika.model.entity.CKUser;
import me.wuwenbin.chika.service.AuthService;
import me.wuwenbin.chika.service.LoginService;
import me.wuwenbin.chika.service.ParamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * created by Wuwenbin on 2019/3/24 at 11:11
 */
@Slf4j
@Service("githubLoginService")
public class GithubLoginServiceImpl implements LoginService<Result, GithubLoginData> {

    private final ParamService paramService;
    @Autowired
    private AuthService authService;

    @Autowired
    public GithubLoginServiceImpl(ParamService paramService) {
        this.paramService = paramService;
    }

    @Override
    public Result doLogin(GithubLoginData data) {
        try {
            CKParam githubClientId = paramService.findByName(CKKey.GITHUB_CLIENT_ID.key());
            CKParam githubClientSecret = paramService.findByName(CKKey.GITHUB_CLIENT_SECRET.key());
            String accessTokenUrl = "https://github.com/login/oauth/access_token";
            Map<String, Object> accessTokenUrlParam = new HashMap<>();
            accessTokenUrlParam.put("state", CKConstant.GITHUB_AUTH_STATE);
            accessTokenUrlParam.put("code", data.getCode());
            accessTokenUrlParam.put("client_id", githubClientId.getValue());
            accessTokenUrlParam.put("client_secret", githubClientSecret.getValue());
            accessTokenUrlParam.put("redirect_uri", data.getCallbackDomain());
            String accessTokenResult = HttpUtil.post(accessTokenUrl, accessTokenUrlParam);
            String accessToken = accessTokenResult.substring(13, accessTokenResult.indexOf("&scope"));
            String userInfoUrl = "https://api.github.com/user?access_token={}";
            userInfoUrl = StrUtil.format(userInfoUrl, accessToken);
            String userInfoResult = HttpUtil.get(userInfoUrl);
            JSONObject jsonObject = JSONUtil.parseObj(userInfoResult);
            String login = jsonObject.getStr("login");
            String findGithubUserSql = "select * from chika_user where account_type=? and username = ? and enable = ?";
            CKUser githubUser = baseDao().findBeanByArray(findGithubUserSql, CKUser.class, "github", login, true);
            if (githubUser != null) {
                return Result.ok("授权成功！", "/").put(CKConstant.SESSION_USER_KEY, githubUser);
            } else {
                CKUser lockedUser = baseDao().findBeanByArray(findGithubUserSql, CKUser.class, "github", login, false);
                if (lockedUser != null) {
                    return Result.error("GITHUB登录授权失败，原因：用户已被锁定！");
                }
                String email = jsonObject.getStr("email");
                String avatar = jsonObject.getStr("avatar_url");
                String nickname = jsonObject.getStr("name");
                int cnt = authService.countNickname(nickname);
                nickname = cnt > 0 ? nickname + new java.util.Date().getTime() : nickname;
                CKUser registerUser = CKUser.builder()
                        .role(CKConstant.ROLE_USER).create(new Date())
                        .nickname(nickname).avatar(avatar).username(login)
                        .accountType(CKConstant.TYPE_GITHUB).enable(1)
                        .build();
                if (StrUtil.isNotEmpty(email)) {
                    registerUser.setEmail(email);
                }
                String registerUserSql = "insert into chika_user(role,`create`,nickname,avatar,username,account_type,enable) values(?,?,?,?,?,?,?)";
                int ru = baseDao().executeArray(registerUserSql,
                        CKConstant.ROLE_USER, new Date(), nickname,
                        avatar, login, CKConstant.TYPE_GITHUB, 1);
                if (ru == 1) {
                    return Result.ok("授权成功！", CKConstant.FRONTEND_INDEX).put(CKConstant.SESSION_USER_KEY, registerUser);
                } else {
                    return Result.error("GITHUB授权失败，原因：注册失败！");
                }
            }
        } catch (Exception e) {
            log.error("GITHUB登录授权失败，原因：{}", e);
            return Result.error("GITHUB登录授权失败，原因：{}", e.getMessage());
        }
    }
}
