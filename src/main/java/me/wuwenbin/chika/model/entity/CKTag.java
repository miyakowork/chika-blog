package me.wuwenbin.chika.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * created by Wuwenbin on 2019/3/13 at 13:00
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CKTag implements Serializable {

    private Long id;
    private String name;

}
