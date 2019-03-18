package me.wuwenbin.chika.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.beetl.sql.core.annotatoin.Table;

import java.io.Serializable;

/**
 * created by Wuwenbin on 2019/3/13 at 12:51
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "chika_dict")
public class ChiKaDict implements Serializable {

    private Long id;
    private Integer enable;
    /*
    字典的类型（关键字、节假日等）
    */
    private String type;
    private String value;
    private Integer orderIndex;

}
