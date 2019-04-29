package me.wuwenbin.chika.service.impl;

import me.wuwenbin.chika.model.entity.CKCate;
import me.wuwenbin.chika.service.CateService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * created by Wuwenbin on 2019/4/18 at 13:30
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class CateServiceImpl implements CateService {

    @Override
    public long insertCate(CKCate cate) throws Exception {
        String sql = "insert into chika_cate(cn_name,name,font_icon,order_index,type) values(:cnName,:name,:fontIcon,:orderIndex,:type)";
        return baseDao().insertBeanAutoGenKeyReturnKey(sql, cate);
    }

    @Override
    public boolean cateExist(CKCate ckCate) {
        String sql = "select count(1) from chika_cate where (name = :name or cn_name = :cnName) and type = :type";
        int cnt = baseDao().queryNumberByBean(sql, Integer.class, ckCate);
        return cnt >= 1;
    }

    @Override
    public boolean updateCategory(CKCate ckCate) throws Exception {
        String sql = "update chika_cate set name = :name, cn_name = :cnName,font_icon=:fontIcon,order_index = :orderIndex where id = :id";
        return baseDao().executeBean(sql, ckCate) == 1;
    }
}
