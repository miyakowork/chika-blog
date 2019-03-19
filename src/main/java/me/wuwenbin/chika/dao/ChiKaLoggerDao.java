package me.wuwenbin.chika.dao;

import me.wuwenbin.chika.annotation.BeetlSqlDao;
import me.wuwenbin.chika.model.entity.ChiKaLogger;
import org.beetl.sql.core.mapper.BaseMapper;

/**
 * created by Wuwenbin on 2019/3/19 at 16:32
 */
@BeetlSqlDao
public interface ChiKaLoggerDao extends BaseMapper<ChiKaLogger> {
}
