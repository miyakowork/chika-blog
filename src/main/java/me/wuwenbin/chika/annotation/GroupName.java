package me.wuwenbin.chika.annotation;

import java.lang.annotation.*;

/**
 * created by Wuwenbin on 2019/3/15 at 13:18
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
public @interface GroupName {

    /**
     * 组名，
     * 在后台菜单中表示为菜单分组的名字，即一级菜单
     *
     * @return
     */
    String value();
}
