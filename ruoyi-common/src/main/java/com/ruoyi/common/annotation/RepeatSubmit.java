package com.ruoyi.common.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义注解防止表单重复提交
 * 
 * @author ruoyi
 *
 */
@Inherited
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RepeatSubmit
{
    /**
     * 间隔时间(ms)，小于此时间视为重复提交
     */
    int interval() default 5000;

    /**
     * 提示消息
     */
    String message() default "不允许重复提交，请稍候再试";

    /**
     * 执行完是否释放锁。比如默认5s钟防重复提交，如果方法执行完，那么设置的true，则下一个请求可以继续执行，否则必须得5s后下一个方法才能执行
     */
    boolean releaseAfter() default false;

    /**
     * 重复提交的key表达式。如果为空，则使用默认的
     * 表达式，可以获取Bean对象中的属性（字段）值或者Map中key对应的值
     * 表达式栗子：
     * persion
     * persion.name
     * persons[3]
     * person.friends[5].name
     */
    String keyExpression() default "";

}
