package me.wuwenbin.chika.service.impl;

import cn.hutool.crypto.SecureUtil;
import lombok.extern.slf4j.Slf4j;
import me.wuwenbin.chika.exception.SystemInitException;
import me.wuwenbin.chika.model.bean.Result;
import me.wuwenbin.chika.model.constant.CKConstant;
import me.wuwenbin.chika.model.constant.CKKey;
import me.wuwenbin.chika.model.constant.CKValue;
import me.wuwenbin.chika.service.InitService;
import me.wuwenbin.chika.service.ParamService;
import me.wuwenbin.chika.util.CKUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
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

    private final ParamService paramService;
    private final Environment env;

    @Autowired
    public InitServiceImpl(ParamService paramService, Environment env) {
        this.paramService = paramService;
        this.env = env;
    }

    @Override
    public Result initSystem(Map<String, String[]> param) {
        Map<String, Object> paramMap = CKUtils.getParameterMap(param);
        if (paramMap.get(uploadTypeKey).equals(CKValue.LOCAL_SERVER.strVal())) {
            if (StringUtils.isEmpty(env.getProperty(CKConstant.UPLOAD_PATH_KEY))) {
                return Result.error("配置文件「application-chika.properties」中「chika.upload.path」未正确配置！");
            }
        }
        try {
            Map<String, Object> pm = initUser(paramMap);
            Map<String, Object> pm2 = initUpload(pm);
            for (Map.Entry<String, Object> next : pm2.entrySet()) {
                String key = next.getKey();
                if (!StringUtils.isEmpty(next.getValue())) {
                    Object value = next.getValue();
                    paramService.updateValueByName(value, key);
                }
            }
            paramService.updateValueByName(CKValue.ENABLE.intVal(), CKKey.SYSTEM_INIT_STATE.key());
            return Result.ok("初始化设置成功！");
        } catch (Exception e) {
            log.error("系统初始化设置异常", e);
            throw new SystemInitException("系统初始化设置出错！");
        }
    }


    private Map<String, Object> initUser(Map<String, Object> paramMap) throws Exception {
        String password = paramMap.get(passwordKey).toString();
        String username = paramMap.get(usernameKey).toString();
        String email = paramMap.get(emailKey).toString();
        String initUserSql = "insert into chika_user(avatar,role,nickname,password,username,email,`create`,enable) values(?,?,?,?,?,?,?,?)";
        baseDao().executeArray(initUserSql,
                "/static/assets/img/avatar.png",
                CKConstant.ROLE_ADMIN,
                "千夏博客用户",
                SecureUtil.md5(password),
                username,
                email,
                new Date(),
                CKValue.ENABLE.intVal()
        );
        paramService.updateValueByName(CKValue.ENABLE.intVal(), CKKey.ADMIN_SET_STATE.key());
        paramMap.remove(passwordKey);
        paramMap.remove(emailKey);
        return paramMap;
    }

    private Map<String, Object> initUpload(Map<String, Object> paramMap) throws Exception {
        String uploadType = paramMap.get(uploadTypeKey).toString();
        paramService.updateValueByName(uploadType, CKKey.UPLOAD_TYPE.key());
        paramService.updateValueByName(CKValue.ENABLE.intVal(), CKKey.OSS_UPLOAD_ONOFF.key());
        paramMap.remove(uploadTypeKey);
        return paramMap;
    }
}
