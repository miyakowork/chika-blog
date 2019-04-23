package me.wuwenbin.chika.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * created by Wuwenbin on 2019/3/13 at 13:17
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CKComment implements Serializable {

    private Long id;
    private Integer enable;
    private Long articleId;
    private String clearComment;
    private String comment;
    private String ipAddr;
    private String ipCnAddr;
    private String userAgent;
    private Long userId;
    private Date post;
}
