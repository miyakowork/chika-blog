package me.wuwenbin.chika.service.impl;

import cn.hutool.core.util.StrUtil;
import me.wuwenbin.chika.model.constant.CKKey;
import me.wuwenbin.chika.model.entity.CKParam;
import me.wuwenbin.chika.service.ParamService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * created by Wuwenbin on 2019/3/20 at 17:54
 *
 * @author wuwenbin
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ParamServiceImpl implements ParamService {

    @Override
    public boolean isSetSendMailServer() {
        CKParam account = findByName(CKKey.MAIL_SERVER_ACCOUNT.key());
        CKParam name = findByName(CKKey.MAIL_SENDER_NAME.key());
        CKParam password = findByName(CKKey.MAIL_SERVER_PASSWORD.key());
        CKParam stmpAddr = findByName(CKKey.MAIL_SMPT_SERVER_ADDR.key());
        CKParam stmpPort = findByName(CKKey.MAIL_SMPT_SERVER_PORT.key());
        return StrUtil.isNotEmpty(account.getValue()) &&
                StrUtil.isNotEmpty(password.getValue()) &&
                StrUtil.isNotEmpty(name.getValue()) &&
                StrUtil.isNotEmpty(stmpAddr.getValue()) &&
                StrUtil.isNotEmpty(stmpPort.getValue());
    }

    @Override
    @CacheEvict(value = "paramCache", key = "#name")
    public void updateValueByName(Object value, String name) throws Exception {
        String sql = "update chika_param set value = ? where name = ?";
        baseDao().executeArray(sql, value, name);
    }

    @Override
    @Cacheable(value = "paramCache", key = "#name")
    public CKParam findByName(String name) {
        String sql = "select * from chika_param where name = ?";
        return baseDao().findBeanByArray(sql, CKParam.class, name);
    }

    @Override
    public void insertParam(CKParam param) throws Exception {
        String sql = "insert into chika_param(name,level,remark,value) values(?,?,?,?)";
        baseDao().executeArray(sql, param.getName(),
                param.getLevel(), param.getRemark(), param.getValue());
    }
}
