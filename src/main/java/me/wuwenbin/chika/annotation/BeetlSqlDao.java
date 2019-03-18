package me.wuwenbin.chika.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * 修饰注解，防止IDEA警告红线
 * created by Wuwenbin on 2019/3/13 at 14:52
 */
@Component
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
public @interface BeetlSqlDao {
}
