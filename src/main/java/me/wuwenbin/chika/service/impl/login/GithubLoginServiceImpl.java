package me.wuwenbin.chika.service.impl.login;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import me.wuwenbin.chika.dao.ChiKaParamDao;
import me.wuwenbin.chika.dao.ChiKaUserDao;
import me.wuwenbin.chika.model.bean.Result;
import me.wuwenbin.chika.model.bean.login.GithubLoginData;
import me.wuwenbin.chika.model.constant.ChiKaConstant;
import me.wuwenbin.chika.model.constant.ChiKaKey;
import me.wuwenbin.chika.model.entity.ChiKaParam;
import me.wuwenbin.chika.model.entity.ChiKaUser;
import me.wuwenbin.chika.service.LoginService;
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

    private final ChiKaParamDao paramDao;
    private final ChiKaUserDao userDao;

    @Autowired
    public GithubLoginServiceImpl(ChiKaParamDao paramDao, ChiKaUserDao userDao) {
        this.paramDao = paramDao;
        this.userDao = userDao;
    }

    @Override
    public Result doLogin(GithubLoginData data) {
        try {
            ChiKaParam githubClientId = paramDao.findByName(ChiKaKey.GITHUB_CLIENT_ID.key());
            ChiKaParam githubClientSecret = paramDao.findByName(ChiKaKey.GITHUB_CLIENT_SECRET.key());
            String accessTokenUrl = "https://github.com/login/oauth/access_token";
            Map<String, Object> accessTokenUrlParam = new HashMap<>();
            accessTokenUrlParam.put("state", ChiKaConstant.GITHUB_AUTH_STATE);
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
            ChiKaUser githubUser = userDao.findGithubUser(login, true);
            if (githubUser != null) {
                return Result.ok("授权成功！", "/").put(ChiKaConstant.SESSION_USER_KEY, githubUser);
            } else {
                ChiKaUser lockedUser = userDao.findGithubUser(login, false);
                if (lockedUser != null) {
                    return Result.error("GITHUB登录授权失败，原因：用户已被锁定！");
                }
                String email = jsonObject.getStr("email");
                String avatar = jsonObject.getStr("avatar_url");
                String nickname = jsonObject.getStr("name");
                int cnt = userDao.countNickname(nickname);
                nickname = cnt > 0 ? nickname + new java.util.Date().getTime() : nickname;
                ChiKaUser registerUser = ChiKaUser.builder()
                        .role(ChiKaConstant.ROLE_USER).create(new Date())
                        .nickname(nickname).avatar(avatar).username(login)
                        .accountType(ChiKaConstant.TYPE_GITHUB).enable(1)
                        .build();
                if (StrUtil.isNotEmpty(email)) {
                    registerUser.setEmail(email);
                }
                userDao.insertTemplate(registerUser, true);
                if (registerUser.getId() != null) {
                    return Result.ok("授权成功！", ChiKaConstant.FRONTEND_INDEX).put(ChiKaConstant.SESSION_USER_KEY, registerUser);
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
