package me.wuwenbin.chika.service;

import me.wuwenbin.chika.model.entity.CKArticle;

/**
 * created by Wuwenbin on 2019/4/18 at 16:52
 */
public interface ArticleService extends PublicService {

    /**
     * 插入博客文章
     *
     * @param CKArticle
     * @return
     * @throws Exception
     */
    int insertArticle(CKArticle CKArticle) throws Exception;
}
