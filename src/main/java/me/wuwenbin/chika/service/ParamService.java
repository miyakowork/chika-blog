package me.wuwenbin.chika.service;

import me.wuwenbin.chika.model.entity.CKParam;

/**
 * created by Wuwenbin on 2019/3/20 at 17:49
 */
public interface ParamService extends PublicService {

    /**
     * 是否设置了发送邮件服务器
     *
     * @return
     */
    boolean isSetSendMailServer();

    /**
     * 更新参数值
     *
     * @param value
     * @param name
     * @throws Exception
     */
    void updateValueByName(Object value, String name) throws Exception;

    /**
     * 查找参数对象
     *
     * @param name
     * @return
     */
    CKParam findByName(String name);

    /**
     * 插入一个新的参数
     *
     * @param param
     * @throws Exception
     */
    void insertParam(CKParam param) throws Exception;
}
