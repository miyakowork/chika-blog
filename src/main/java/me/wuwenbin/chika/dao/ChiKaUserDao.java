package me.wuwenbin.chika.dao;

import me.wuwenbin.chika.annotation.BeetlSqlDao;
import me.wuwenbin.chika.model.entity.ChiKaUser;
import org.beetl.sql.core.mapper.BaseMapper;

/**
 * created by Wuwenbin on 2019/3/16 at 13:22
 */
@BeetlSqlDao
public interface ChiKaUserDao extends BaseMapper<ChiKaUser> {

    /**
     * 查找登录用户是否存在
     *
     * @param username
     * @param password
     * @return
     */
    ChiKaUser findLoginUser(String username, String password);

    /**
     * 根据openid查找用户
     *
     * @param openId
     * @return
     */
    ChiKaUser findByQqOpenId(String openId, Boolean enable);

    /**
     * 统计nickname的数量
     *
     * @param nickname
     * @return
     */
    int countNickname(String nickname);

    /**
     * 统计邮箱和用户名的数量
     *
     * @param userParam
     * @return
     */
    int countEmailAndUsername(String userParam);

    /**
     * 根据email账号更新密码
     *
     * @param password
     * @param email
     * @return
     */
    int updatePasswordByEmail(String password, String email);

    /**
     * 查找github用户
     *
     * @param login
     * @param enable
     * @return
     */
    ChiKaUser findGithubUser(String login,boolean enable);
}
