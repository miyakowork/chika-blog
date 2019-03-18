package me.wuwenbin.chika.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.beetl.sql.core.annotatoin.Table;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * created by Wuwenbin on 2019/3/13 at 13:17
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "chika_comment")
public class ChiKaComment implements Serializable {

    private Long id;
    private Integer enable;
    private Long articleId;
    private String clearComment;
    private String comment;
    private String ipAddr;
    private String ipCnAddr;
    private String userAgent;
    private Long userId;
    private LocalDateTime post;
}
