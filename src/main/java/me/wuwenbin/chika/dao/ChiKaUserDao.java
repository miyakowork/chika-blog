package me.wuwenbin.chika.dao;

import me.wuwenbin.chika.annotation.BeetlSqlDao;
import me.wuwenbin.chika.model.entity.ChiKaUser;
import org.beetl.sql.core.mapper.BaseMapper;

/**
 * created by Wuwenbin on 2019/3/16 at 13:22
 */
@BeetlSqlDao
public interface ChiKaUserDao extends BaseMapper<ChiKaUser> {
}
