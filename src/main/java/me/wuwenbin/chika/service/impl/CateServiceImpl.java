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
}
