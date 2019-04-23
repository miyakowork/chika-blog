package me.wuwenbin.chika.service;

import me.wuwenbin.chika.model.entity.CKCate;

/**
 * created by Wuwenbin on 2019/4/18 at 13:30
 */
public interface CateService extends PublicService {

    /**
     * 插入一个新的类别
     *
     * @param CKCate
     * @return
     * @throws Exception
     */
    long insertCate(CKCate CKCate) throws Exception;
}
