package me.wuwenbin.chika.annotation;

import java.lang.annotation.*;

/**
 * created by Wuwenbin on 2018/7/20 at 15:12
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface SqlTable {

    String value() default "";
}
