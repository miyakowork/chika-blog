package me.wuwenbin.chika.service.impl;

import cn.hutool.crypto.SecureUtil;
import lombok.extern.slf4j.Slf4j;
import me.wuwenbin.chika.dao.ChiKaParamDao;
import me.wuwenbin.chika.dao.ChiKaUserDao;
import me.wuwenbin.chika.exception.SystemInitException;
import me.wuwenbin.chika.model.bean.Result;
import me.wuwenbin.chika.model.constant.ChiKaConstant;
import me.wuwenbin.chika.model.constant.ChiKaKey;
import me.wuwenbin.chika.model.constant.ChikaValue;
import me.wuwenbin.chika.model.entity.ChiKaUser;
import me.wuwenbin.chika.service.InitService;
import me.wuwenbin.chika.util.ChiKaKit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * created by Wuwenbin on 2019/3/16 at 12:37
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class InitServiceImpl implements InitService {

    private static final String usernameKey = "_username";
    private static final String passwordKey = "_password";
    private static final String emailKey = "_email";
    private static final String uploadTypeKey = "upload_type";

    private final ChiKaParamDao paramDao;
    private final ChiKaUserDao userDao;

    @Autowired
    public InitServiceImpl(ChiKaParamDao paramDao, ChiKaUserDao userDao) {
        this.paramDao = paramDao;
        this.userDao = userDao;
    }

    @Override
    public Result initSystem(Map<String, String[]> param) {
        Map<String, Object> paramMap = ChiKaKit.getParameterMap(param);
        try {
            Map<String, Object> pm = initUser(paramMap);
            Map<String, Object> pm2 = initUpload(pm);
            for (Map.Entry<String, Object> next : pm2.entrySet()) {
                String key = next.getKey();
                if (!StringUtils.isEmpty(next.getValue())) {
                    Object value = next.getValue();
                    paramDao.updateValueByName(value, key);
                }
            }
            return Result.ok("初始化设置成功！");
        } catch (Exception e) {
            log.error("系统初始化设置异常", e);
            throw new SystemInitException("系统初始化设置出错！");
        }
    }


    private Map<String, Object> initUser(Map<String, Object> paramMap) {
        String password = paramMap.get(passwordKey).toString();
        String username = paramMap.get(usernameKey).toString();
        String email = paramMap.get(emailKey).toString();
        ChiKaUser initUser = ChiKaUser.builder()
                .avatar("/static/assets/img/avatar.png")
                .role(ChiKaConstant.ROLE_ADMIN)
                .nickname("千夏博客用户")
                .password(SecureUtil.md5(password))
                .username(username)
                .email(email)
                .create(LocalDateTime.now())
                .enable(ChikaValue.ENABLE.intVal())
                .build();
        userDao.insertTemplate(initUser);
        paramDao.updateValueByName(ChikaValue.ENABLE.intVal(), ChiKaKey.ADMIN_SET_STATE.key());
        paramMap.remove(passwordKey);
        paramMap.remove(emailKey);
        return paramMap;
    }

    private Map<String, Object> initUpload(Map<String, Object> paramMap) {
        String uploadType = paramMap.get(uploadTypeKey).toString();
        paramDao.updateValueByName(uploadType, ChiKaKey.UPLOAD_TYPE.key());
        paramDao.updateValueByName(ChikaValue.ENABLE.intVal(), ChiKaKey.OSS_UPLOAD_ONOFF.key());
        paramMap.remove(uploadTypeKey);
        return paramMap;
    }
}
