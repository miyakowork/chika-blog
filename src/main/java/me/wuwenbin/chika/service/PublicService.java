package me.wuwenbin.chika.service;

import cn.hutool.core.util.StrUtil;
import me.wuwenbin.chika.annotation.SqlTable;
import me.wuwenbin.chika.util.CKUtils;
import me.wuwenbin.data.jdbc.ancestor.AncestorDao;
import me.wuwenbin.data.jdbc.support.Page;

/**
 * created by Wuwenbin on 2019/4/19 at 8:43
 */
public interface PublicService<T> {

    /**
     * 获取通用Dao对象
     *
     * @return
     */
    default AncestorDao baseDao() {
        return CKUtils.getBean(AncestorDao.class);
    }


    /**
     * 默认的分页查询，没有查询条件地查询所有记录
     *
     * @param page
     * @param clazz
     * @return
     */
    default Page<T> findPageInfo(Page<T> page, Class<T> clazz) {
        String sql = "select * from {}";
        sql = StrUtil.format(sql, clazz.getAnnotation(SqlTable.class).value());
        return baseDao().findPageListBeanByArray(sql, clazz, page);
    }

}
