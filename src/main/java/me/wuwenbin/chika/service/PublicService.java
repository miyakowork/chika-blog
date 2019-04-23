package me.wuwenbin.chika.service;

import me.wuwenbin.chika.util.CKUtils;
import me.wuwenbin.data.jdbc.ancestor.AncestorDao;

/**
 * created by Wuwenbin on 2019/4/19 at 8:43
 */
public interface PublicService {

    /**
     * 获取通用Dao对象
     *
     * @return
     */
    default AncestorDao baseDao() {
        return CKUtils.getBean(AncestorDao.class);
    }

}
