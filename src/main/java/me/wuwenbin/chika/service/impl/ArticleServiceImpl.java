package me.wuwenbin.chika.service.impl;

import me.wuwenbin.chika.model.entity.CKArticle;
import me.wuwenbin.chika.service.ArticleService;
import me.wuwenbin.data.jdbc.ancestor.AncestorDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * created by Wuwenbin on 2019/4/18 at 16:52
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ArticleServiceImpl implements ArticleService {

    private final AncestorDao dao;

    @Autowired
    public ArticleServiceImpl(AncestorDao dao) {
        this.dao = dao;
    }

    @Override
    public int insertArticle(CKArticle CKArticle) throws Exception {
        String sql = "insert into chika_article" +
                "(id,appreciable,approve_cnt,author_id,commented,content,cover,draft,md_content,post,summary,text_content,title,top,url_seq,view)" +
                " values(:id,:appreciable,:approveCnt,:authorId,:commented,:content,:cover,:draft,:mdContent,:post,:summary,:textContent,:title,:top,:urlSeq,:view)";
        return dao.executeBean(sql, CKArticle);
    }
}
