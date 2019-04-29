package me.wuwenbin.chika.service;

import me.wuwenbin.chika.model.entity.CKCate;

/**
 * created by Wuwenbin on 2019/4/18 at 13:30
 */
public interface CateService extends PublicService<CKCate> {

    /**
     * 插入一个新的类别
     *
     * @param CkCate
     * @return
     * @throws Exception
     */
    long insertCate(CKCate CkCate) throws Exception;

    /**
     * 判断cate是否已经存在
     *
     * @param ckCate
     * @return
     */
    boolean cateExist(CKCate ckCate);

    /**
     * 更新分类信息
     *
     * @param ckCate
     * @return
     * @throws Exception
     */
    boolean updateCategory(CKCate ckCate) throws Exception;
}
