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
@Table(name = "chika_param")
public class ChiKaParam implements Serializable {

    private Long id ;
    /*
    越高表示参数级别权限越高可能会影响系统运行
    */
    private Integer level ;
    private Integer orderIndex ;
    private String name ;
    private String remark ;
    private String value ;
}
