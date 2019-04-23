package me.wuwenbin.chika.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * created by Wuwenbin on 2019/3/13 at 12:53
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CKCate implements Serializable {

    private Long id;
    private String cnName;
    private String fontIcon;
    private String name;
    /*
    分3种：article、file和project
    */
    private String type;
    private Integer orderIndex;
}
