package me.wuwenbin.chika.service.impl;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import me.wuwenbin.chika.dao.ChiKaParamDao;
import me.wuwenbin.chika.dao.ChiKaUserDao;
import me.wuwenbin.chika.model.constant.ChiKaConstant;
import me.wuwenbin.chika.model.constant.ChiKaKey;
import me.wuwenbin.chika.model.entity.ChiKaUser;
import me.wuwenbin.chika.service.MailService;
import me.wuwenbin.chika.service.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;

/**
 * created by Wuwenbin on 2019/3/21 at 17:16
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class RegisterServiceImpl implements RegisterService {

    private final ChiKaUserDao userDao;
    private final MailService mailService;
    private final ChiKaParamDao paramDao;

    @Autowired
    public RegisterServiceImpl(ChiKaUserDao userDao, MailService mailService, ChiKaParamDao paramDao) {
        this.userDao = userDao;
        this.mailService = mailService;
        this.paramDao = paramDao;
    }

    @Override
    public void userRegister(String chiKaUser, String chiKaPass, String nickname) {
        ChiKaUser user = ChiKaUser.builder()
                .email(chiKaUser)
                .role(ChiKaConstant.ROLE_USER)
                .password(SecureUtil.md5(chiKaPass))
                .nickname(nickname)
                .enable(1)
                .avatar("/static/assets/img/favicon.png")
                .build();
        userDao.insertTemplate(user);
    }

    @Override
    public void sendMailCode(String email, HttpServletRequest request) {
        if (ReUtil.isMatch("^[A-Za-z\\d]+([-_.][A-Za-z\\d]+)*@([A-Za-z\\d]+[-.])+[A-Za-z\\d]{2,4}$", email)) {
            String websiteTitle = paramDao.findByName(ChiKaKey.WEBSITE_TITLE.key()).getValue();
            String subject = websiteTitle + "注册验证码";
            String content = "您注册的验证码为<b>【{}】</b>。";
            String code = RandomUtil.randomString(6);
            content = StrUtil.format(content, code);
            mailService.sendMail(subject, email, content,
                    true,
                    req -> req.getSession().setAttribute(ChiKaConstant.MAIL_CODE_KEY, code),
                    request);
        }
    }
}
