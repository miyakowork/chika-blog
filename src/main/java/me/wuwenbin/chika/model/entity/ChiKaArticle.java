package me.wuwenbin.chika.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.beetl.sql.core.annotatoin.AssignID;
import org.beetl.sql.core.annotatoin.Table;

import java.io.Serializable;
import java.time.LocalDateTime;

import static cn.hutool.core.util.RandomUtil.randomInt;
import static java.lang.Boolean.TRUE;

/**
 * created by Wuwenbin on 2019/3/13 at 13:05
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "chika_article")
public class ChiKaArticle implements Serializable {

    @AssignID
    private String id;
    @Builder.Default
    private Boolean appreciable = Boolean.FALSE;
    @Builder.Default
    private Integer approveCnt = randomInt(6, 169);
    @Builder.Default
    private Boolean commented = Boolean.FALSE;
    @Builder.Default
    private Boolean draft = TRUE;
    @Builder.Default
    private Integer top = 0;
    @Builder.Default
    private Integer view = randomInt(666, 1609);
    private Long authorId;
    private String content;
    private String cover;
    private String mdContent;
    private String summary;
    private String textContent;
    private String title;
    private String urlSeq;
    private LocalDateTime modify;
    private LocalDateTime post;
}
