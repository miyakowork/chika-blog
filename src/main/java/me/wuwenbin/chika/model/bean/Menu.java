package me.wuwenbin.chika.model.bean;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * created by Wuwenbin on 2019/3/15 at 11:20
 */
@Data
@Builder
public class Menu implements Serializable {

    private String name;
    private String url;
    private String icon;
    private int order;
    private String groupName;
}
