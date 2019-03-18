package me.wuwenbin.chika.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.beetl.sql.core.annotatoin.Table;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * created by Wuwenbin on 2019/3/13 at 13:14
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "chika_note")
public class ChiKaNote implements Serializable {

    private Long id;
    private Integer show;
    private Integer top;
    private String clearContent;
    private String content;
    private String mdContent;
    private String title;
    private LocalDateTime modify;
    private LocalDateTime post;
}
