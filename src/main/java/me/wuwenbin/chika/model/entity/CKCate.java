package me.wuwenbin.chika.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.wuwenbin.chika.annotation.SqlTable;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * created by Wuwenbin on 2019/3/13 at 12:53
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@SqlTable("chika_cate")
public class CKCate implements Serializable {

    private Long id;
    @NotNull(message = "分类中文名称不能为空")
    private String cnName;
    private String fontIcon;
    @NotEmpty(message = "分类标识名称不能为空")
    private String name;
    /*
    分3种：article、file和project
    */
    @NotEmpty(message = "必须制定分类为一个模块下")
    private String type;
    private Integer orderIndex;
}
