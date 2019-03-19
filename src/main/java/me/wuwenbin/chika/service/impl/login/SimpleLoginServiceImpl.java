package me.wuwenbin.chika.service.impl.login;

import cn.hutool.cache.Cache;
import me.wuwenbin.chika.dao.ChiKaUserDao;
import me.wuwenbin.chika.model.bean.Result;
import me.wuwenbin.chika.model.bean.login.SimpleLoginModel;
import me.wuwenbin.chika.model.constant.ChiKaConstant;
import me.wuwenbin.chika.model.constant.ChikaValue;
import me.wuwenbin.chika.model.entity.ChiKaUser;
import me.wuwenbin.chika.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * created by Wuwenbin on 2019/3/19 at 15:30
 */
@Service("simpleLoginService")
@Transactional(rollbackFor = Exception.class)
public class SimpleLoginServiceImpl implements LoginService<Result, SimpleLoginModel> {

    private final ChiKaUserDao userDao;
    private final Cache<String, AtomicInteger> passwordRetryCache;

    @Autowired
    public SimpleLoginServiceImpl(ChiKaUserDao userDao, Cache<String, AtomicInteger> passwordRetryCache) {
        this.userDao = userDao;
        this.passwordRetryCache = passwordRetryCache;
    }


    @Override
    public Result doLogin(SimpleLoginModel param) {
        String username = param.getChiKaUser();
        //retry count + 1
        AtomicInteger retryCount = passwordRetryCache.get(username);
        if (retryCount == null) {
            retryCount = new AtomicInteger(0);
            passwordRetryCache.put(username, retryCount);
        }
        if (retryCount.incrementAndGet() > 5) {
            //if retry count > 5 throw
            return Result.error("密码输入错误次数过多");
        }
        ChiKaUser loginUser = userDao.findLoginUser(username, param.getChiKaPass());
        if (loginUser != null) {
            int role = loginUser.getRole();
            String successText = "登录成功！";
            //clear retry count
            passwordRetryCache.remove(username);
            if (role == ChiKaConstant.ROLE_ADMIN) {
                return Result.ok(successText, ChikaValue.MANAGEMENT_INDEX.strVal());
            } else {
                return Result.ok(successText, ChikaValue.FRONTEND_INDEX.strVal());
            }
        } else {
            return Result.error("用户名不存在或密码错误或用户已被锁定！");
        }
    }

}
