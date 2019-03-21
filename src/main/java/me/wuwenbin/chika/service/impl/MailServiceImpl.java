package me.wuwenbin.chika.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.mail.MailAccount;
import cn.hutool.extra.mail.MailUtil;
import me.wuwenbin.chika.dao.ChiKaParamDao;
import me.wuwenbin.chika.model.constant.ChiKaKey;
import me.wuwenbin.chika.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Consumer;

/**
 * created by Wuwenbin on 2019-03-21 at 16:32
 *
 * @author wuwenbin
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class MailServiceImpl implements MailService {

    private final ChiKaParamDao paramDao;

    @Autowired
    public MailServiceImpl(ChiKaParamDao paramDao) {
        this.paramDao = paramDao;
    }


    @Override
    public <T> void sendMail(String subject, String targetMail, String content, boolean isHtml, Consumer<T> extraOperate, T t) {
        String host = paramDao.findByName(ChiKaKey.MAIL_SMPT_SERVER_ADDR.key()).getValue();
        String port = paramDao.findByName(ChiKaKey.MAIL_SMPT_SERVER_PORT.key()).getValue();
        String from = paramDao.findByName(ChiKaKey.MAIL_SERVER_ACCOUNT.key()).getValue();
        String user = paramDao.findByName(ChiKaKey.MAIL_SENDER_NAME.key()).getValue();
        String pass = paramDao.findByName(ChiKaKey.MAIL_SERVER_PASSWORD.key()).getValue();
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
            extraOperate.accept(t);
        }
    }
}
