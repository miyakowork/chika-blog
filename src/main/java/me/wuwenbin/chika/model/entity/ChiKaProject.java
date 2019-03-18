package me.wuwenbin.chika.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.beetl.sql.core.annotatoin.Table;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * created by Wuwenbin on 2019/3/13 at 13:16
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "chika_project")
public class ChiKaProject implements Serializable {

    private Long id;
    private String cover;
    private String description;
    private String name;
    private String url;
    private LocalDateTime modify;
    private LocalDateTime post;

}
