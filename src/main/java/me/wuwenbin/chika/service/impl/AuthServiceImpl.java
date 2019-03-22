package me.wuwenbin.chika.service.impl;

import cn.hutool.cache.Cache;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import me.wuwenbin.chika.dao.ChiKaParamDao;
import me.wuwenbin.chika.dao.ChiKaUserDao;
import me.wuwenbin.chika.model.bean.Result;
import me.wuwenbin.chika.model.constant.ChiKaConstant;
import me.wuwenbin.chika.model.constant.ChiKaKey;
import me.wuwenbin.chika.model.entity.ChiKaUser;
import me.wuwenbin.chika.service.AuthService;
import me.wuwenbin.chika.service.MailService;
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

    private final ChiKaUserDao userDao;
    private final MailService mailService;
    private final ChiKaParamDao paramDao;
    private final Cache<String, String> codeCache;

    @Autowired
    public AuthServiceImpl(ChiKaUserDao userDao, MailService mailService,
                           ChiKaParamDao paramDao, Cache<String, String> codeCache) {
        this.userDao = userDao;
        this.mailService = mailService;
        this.paramDao = paramDao;
        this.codeCache = codeCache;
    }

    @Override
    public void userRegister(String chiKaUser, String chiKaPass, String nickname) {
        ChiKaUser user = ChiKaUser.builder()
                .email(chiKaUser)
                .role(ChiKaConstant.ROLE_USER)
                .create(new Date())
                .password(SecureUtil.md5(chiKaPass))
                .nickname(nickname)
                .enable(1)
                .avatar("/static/assets/img/favicon.png")
                .build();
        userDao.insertTemplate(user);
    }

    @Override
    public void sendMailCode(String email) {
        if (ReUtil.isMatch("^[A-Za-z\\d]+([-_.][A-Za-z\\d]+)*@([A-Za-z\\d]+[-.])+[A-Za-z\\d]{2,4}$", email)) {
            String websiteTitle = paramDao.findByName(ChiKaKey.WEBSITE_TITLE.key()).getValue();
            String subject = "【" + websiteTitle + "】注册验证码";
            String content = "您的验证码为<b>【{}】</b>，10分钟内有效。";
            String code = RandomUtil.randomString(6);
            content = StrUtil.format(content, code);
            mailService.sendMail(subject, email, content,
                    true);
            String key = email + "-" + ChiKaConstant.MAIL_CODE_KEY;
            codeCache.remove(key);
            codeCache.put(key, code);
        }
    }

    @Override
    public Result resetPassword(String email) {
        if (ReUtil.isMatch("^[A-Za-z\\d]+([-_.][A-Za-z\\d]+)*@([A-Za-z\\d]+[-.])+[A-Za-z\\d]{2,4}$", email)) {
            String resetPass = SecureUtil.md5(SecureUtil.md5("123456"));
            int res = userDao.updatePasswordByEmail(resetPass, email);
            return res == 1 ?
                    Result.ok("重置密码成功，重置密码为【123456】") :
                    Result.error("重置密码失败，请稍后再试！");
        } else {
            return Result.error("重置密码失败，请正确填写邮箱！");
        }
    }
}
