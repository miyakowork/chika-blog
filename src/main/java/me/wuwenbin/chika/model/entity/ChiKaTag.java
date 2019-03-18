package me.wuwenbin.chika.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.beetl.sql.core.annotatoin.Table;

import java.io.Serializable;

/**
 * created by Wuwenbin on 2019/3/13 at 13:00
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "chika_tag")
public class ChiKaTag implements Serializable {

    private Long id ;
    private String name ;

}
