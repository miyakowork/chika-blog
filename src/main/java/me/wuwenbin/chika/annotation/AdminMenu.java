package me.wuwenbin.chika.annotation;

import java.lang.annotation.*;

/**
 * 标注区分以生成后台管理的菜单
 * 省自动识别新的菜单，省去手动编写菜单json的麻烦
 * created by Wuwenbin on 2019/3/15 at 10:50
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AdminMenu {

    /**
     * 菜单名
     *
     * @return
     */
    String value();

    /**
     * 属于哪一组
     * class为任意自定义的类都可，但是必须在类上注解{@link GroupName}
     *
     * @return
     */
    Class<?>[] groups() default {};


    /**
     * 菜单的排序顺序
     *
     * @return
     */
    int order() default 0;

}
