package me.wuwenbin.chika.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * created by Wuwenbin on 2019/3/13 at 13:15
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CKFile implements Serializable {

    private Long id;
    private String description;
    private String name;
    private String url;
    private Date modify;
    private Date post;
}
