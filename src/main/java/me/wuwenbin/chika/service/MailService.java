package me.wuwenbin.chika.service;

import java.util.function.Consumer;

/**
 * created by Wuwenbin on 2019-03-21 at 16:25
 * @author wuwenbin
 */
public interface MailService {

    /**
     * 发送邮件
     * @param subject 邮件主题
     * @param targetMail 发送给谁，目标邮件
     * @param content 邮件内容
     * @param isHtml 是否发送富文本
     * @param extraOperate  发送邮件之外的额外操作
     * @param t
     * @return
     */
   <T> void sendMail(String subject,String targetMail, String content, boolean isHtml, Consumer<T> extraOperate,T t);
}
