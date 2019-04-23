package me.wuwenbin.chika.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

import static cn.hutool.core.util.RandomUtil.randomInt;
import static java.lang.Boolean.TRUE;

/**
 * created by Wuwenbin on 2019/3/13 at 13:05
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CKArticle implements Serializable {

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
    private Date modify;
    private Date post;
}
