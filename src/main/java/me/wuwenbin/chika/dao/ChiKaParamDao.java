package me.wuwenbin.chika.dao;

import me.wuwenbin.chika.annotation.BeetlSqlDao;
import me.wuwenbin.chika.model.entity.ChiKaParam;
import org.beetl.sql.core.mapper.BaseMapper;

/**
 * created by Wuwenbin on 2019/3/13 at 14:30
 */
@BeetlSqlDao
public interface ChiKaParamDao extends BaseMapper<ChiKaParam> {

    /**
     * 查找系统初始化状态
     *
     * @return
     */
    ChiKaParam selectValueByName(String name);

    /**
     * 根据key更新name
     *
     * @param value
     * @param name
     * @return
     */
    int updateValueByName(Object value, String name);
}
