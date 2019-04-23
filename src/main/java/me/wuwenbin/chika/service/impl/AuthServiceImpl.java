package me.wuwenbin.chika.service.impl;

import cn.hutool.cache.Cache;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import me.wuwenbin.chika.model.bean.Result;
import me.wuwenbin.chika.model.constant.CKConstant;
import me.wuwenbin.chika.model.constant.CKKey;
import me.wuwenbin.chika.model.entity.CKUser;
import me.wuwenbin.chika.service.AuthService;
import me.wuwenbin.chika.service.MailService;
import me.wuwenbin.chika.service.ParamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * created by Wuwenbin on 2019/3/21 at 17:16
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class AuthServiceImpl implements AuthService {

    private final MailService mailService;
    private final ParamService paramService;
    private final Cache<String, String> codeCache;

    @Autowired
    public AuthServiceImpl(ParamService paramService,
                           MailService mailService, Cache<String, String> codeCache) {
        this.paramService = paramService;
        this.mailService = mailService;
        this.codeCache = codeCache;
    }

    @Override
    public void userRegister(String chiKaUser, String chiKaPass, String nickname) throws Exception {
        String sql = "insert into chika_user(email,role,`create`,password,nickname,enable,avatar,account_type) values(?,?,?,?,?,?,?,?)";
        baseDao().executeArray(sql,
                chiKaUser, CKConstant.ROLE_USER, new Date(), SecureUtil.md5(chiKaPass),
                nickname, 1, "/static/assets/img/favicon.png", CKConstant.TYPE_SIMPLE);
    }

    @Override
    public void sendMailCode(String email) {
        if (ReUtil.isMatch("^[A-Za-z\\d]+([-_.][A-Za-z\\d]+)*@([A-Za-z\\d]+[-.])+[A-Za-z\\d]{2,4}$", email)) {
            String websiteTitle = paramService.findByName(CKKey.WEBSITE_TITLE.key()).getValue();
            String subject = "【" + websiteTitle + "】注册验证码";
            String content = "您的验证码为<b>【{}】</b>，10分钟内有效。";
            String code = RandomUtil.randomString(6);
            content = StrUtil.format(content, code);
            mailService.sendMail(subject, email, content,
                    true);
            String key = email + "-" + CKConstant.MAIL_CODE_KEY;
            codeCache.remove(key);
            codeCache.put(key, code);
        }
    }

    @Override
    public Result resetPass(String email) throws Exception {
        if (ReUtil.isMatch("^[A-Za-z\\d]+([-_.][A-Za-z\\d]+)*@([A-Za-z\\d]+[-.])+[A-Za-z\\d]{2,4}$", email)) {
            String resetPass = SecureUtil.md5(SecureUtil.md5("123456"));
            String sql = "update chika_user set password = ? where  email = ?";
            int res = baseDao().executeArray(sql, resetPass, email);
            return res == 1 ?
                    Result.ok("重置密码成功，重置密码为【123456】") :
                    Result.error("重置密码失败，请稍后再试！");
        } else {
            return Result.error("重置密码失败，请正确填写邮箱！");
        }
    }

    @Override
    public int countEmailAndUsername(String userParam) {
        String sql = "select count(1) from chika_user where email=? or username?";
        return baseDao().queryNumberByArray(sql, Integer.class, userParam, userParam);
    }

    @Override
    public CKUser findLoginUser(String username, String password) {
        String sql = "select * from chika_user where enable = 1 and (email = ? or username = ?) and password = ?";
        return baseDao().findBeanByArray(sql, CKUser.class, username, username, password);
    }

    @Override
    public CKUser findByQqOpenId(String openId, Boolean enable) {
        String sql = "select * from chika_user where enable =? and qq_open_id = ? and account_type = ?";
        return baseDao().findBeanByArray(sql, CKUser.class, enable, openId, "qq");
    }

    @Override
    public int countNickname(String nickname) {
        String sql = "select count(1) from chika_user where nickname=?";
        return baseDao().queryNumberByArray(sql, Integer.class, nickname);
    }
}
