package me.wuwenbin.chika.service.impl.login;

import cn.hutool.core.map.MapUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import me.wuwenbin.chika.dao.ChiKaParamDao;
import me.wuwenbin.chika.dao.ChiKaUserDao;
import me.wuwenbin.chika.model.bean.Result;
import me.wuwenbin.chika.model.bean.login.QqLoginData;
import me.wuwenbin.chika.model.constant.ChiKaConstant;
import me.wuwenbin.chika.model.constant.ChiKaKey;
import me.wuwenbin.chika.model.entity.ChiKaUser;
import me.wuwenbin.chika.service.LoginService;
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

    private final ChiKaUserDao userDao;
    private final ChiKaParamDao paramDao;

    @Autowired
    public QqLoginServiceImpl(ChiKaUserDao userDao, ChiKaParamDao paramDao) {
        this.userDao = userDao;
        this.paramDao = paramDao;
    }

    @Override
    public Result doLogin(QqLoginData data) {
        try {
            String appId = paramDao.findByName(ChiKaKey.QQ_APP_ID.key()).getValue();
            String appKey = paramDao.findByName(ChiKaKey.QQ_APP_KEY.key()).getValue();

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
                ChiKaUser user = userDao.findByQqOpenId(openId, true);
                if (user != null) {
                    return Result.ok("授权成功！", "/").put(ChiKaConstant.SESSION_USER_KEY, user);
                } else {
                    ChiKaUser lockedUser = userDao.findByQqOpenId(openId, false);
                    if (lockedUser != null) {
                        return Result.error("QQ登录授权失败，原因：用户已被锁定！");
                    }
                    String nickname = json2.getStr("nickname");
                    int cnt = userDao.countNickname(nickname);
                    nickname = cnt > 0 ? nickname + new java.util.Date().getTime() : nickname;
                    String avatar = json2.getStr("figureurl_qq_2").replace("http://", "https://");
                    ChiKaUser registerUser = ChiKaUser.builder()
                            .role(ChiKaConstant.ROLE_USER).create(new Date())
                            .nickname(nickname).avatar(avatar).qqOpenId(openId)
                            .accountType(ChiKaConstant.TYPE_QQ).enable(1)
                            .build();
                    userDao.insertTemplate(registerUser, true);
                    if (registerUser.getId() != null) {
                        return Result.ok("授权成功！", "/").put(ChiKaConstant.SESSION_USER_KEY, registerUser);
                    } else {
                        return Result.error("QQ登录授权失败，原因：注册失败！");
                    }
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
