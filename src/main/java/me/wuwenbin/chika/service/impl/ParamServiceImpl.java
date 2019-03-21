package me.wuwenbin.chika.service.impl;

import cn.hutool.core.util.StrUtil;
import me.wuwenbin.chika.dao.ChiKaParamDao;
import me.wuwenbin.chika.model.constant.ChiKaKey;
import me.wuwenbin.chika.model.entity.ChiKaParam;
import me.wuwenbin.chika.service.ParamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * created by Wuwenbin on 2019/3/20 at 17:54
 * @author wuwenbin
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ParamServiceImpl implements ParamService {

    private final ChiKaParamDao paramDao;

    @Autowired
    public ParamServiceImpl(ChiKaParamDao paramDao) {
        this.paramDao = paramDao;
    }

    @Override
    public boolean isSetSendMailServer() {
        ChiKaParam account = paramDao.findByName(ChiKaKey.MAIL_SERVER_ACCOUNT.key());
        ChiKaParam name = paramDao.findByName(ChiKaKey.MAIL_SENDER_NAME.key());
        ChiKaParam password = paramDao.findByName(ChiKaKey.MAIL_SERVER_PASSWORD.key());
        ChiKaParam stmpAddr = paramDao.findByName(ChiKaKey.MAIL_SMPT_SERVER_ADDR.key());
        ChiKaParam stmpPort = paramDao.findByName(ChiKaKey.MAIL_SMPT_SERVER_PORT.key());
        return StrUtil.isNotEmpty(account.getValue()) &&
                StrUtil.isNotEmpty(password.getValue()) &&
                StrUtil.isNotEmpty(name.getValue()) &&
                StrUtil.isNotEmpty(stmpAddr.getValue()) &&
                StrUtil.isNotEmpty(stmpPort.getValue());
    }
}
