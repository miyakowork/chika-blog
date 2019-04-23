package me.wuwenbin.chika.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.mail.MailAccount;
import cn.hutool.extra.mail.MailUtil;
import me.wuwenbin.chika.model.constant.CKKey;
import me.wuwenbin.chika.service.MailService;
import me.wuwenbin.chika.service.ParamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * created by Wuwenbin on 2019-03-21 at 16:32
 *
 * @author wuwenbin
 */
@Service
@Transactional(rollbackFor = Exception.class, readOnly = true)
public class MailServiceImpl implements MailService {

    private final ParamService paramService;

    @Autowired
    public MailServiceImpl(ParamService paramService) {
        this.paramService = paramService;
    }

    @Override
    public void sendMail(String subject, String targetMail, String content, boolean isHtml) {
        String host = paramService.findByName(CKKey.MAIL_SMPT_SERVER_ADDR.key()).getValue();
        String port = paramService.findByName(CKKey.MAIL_SMPT_SERVER_PORT.key()).getValue();
        String from = paramService.findByName(CKKey.MAIL_SERVER_ACCOUNT.key()).getValue();
        String user = paramService.findByName(CKKey.MAIL_SENDER_NAME.key()).getValue();
        String pass = paramService.findByName(CKKey.MAIL_SERVER_PASSWORD.key()).getValue();
        if (StrUtil.isNotEmpty(host)
                && StrUtil.isNotEmpty(port)
                && StrUtil.isNotEmpty(from)
                && StrUtil.isNotEmpty(pass)) {
            MailAccount account = new MailAccount();
            account.setHost(host);
            account.setPort(Integer.valueOf(port));
            account.setAuth(true);
            account.setSslEnable(true);
            account.setFrom(from);
            account.setUser(user);
            account.setPass(pass);
            MailUtil.send(
                    account,
                    CollUtil.newArrayList(targetMail),
                    subject,
                    content,
                    isHtml);
        }
    }
}
