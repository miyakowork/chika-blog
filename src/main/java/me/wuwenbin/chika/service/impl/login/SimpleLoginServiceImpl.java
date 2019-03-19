package me.wuwenbin.chika.service.impl.login;

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

/**
 * created by Wuwenbin on 2019/3/19 at 15:30
 */
@Service("simpleLoginService")
@Transactional(rollbackFor = Exception.class)
public class SimpleLoginServiceImpl implements LoginService<Result, SimpleLoginModel> {

    private final ChiKaUserDao userDao;

    @Autowired
    public SimpleLoginServiceImpl(ChiKaUserDao userDao) {
        this.userDao = userDao;
    }


    @Override
    public Result doLogin(SimpleLoginModel param) {
        ChiKaUser loginUser = userDao.findLoginUser(param.getChiKaUser(), param.getChiKaPass());
        if (loginUser != null) {
            int role = loginUser.getRole();
            String successText = "登录成功！";
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
