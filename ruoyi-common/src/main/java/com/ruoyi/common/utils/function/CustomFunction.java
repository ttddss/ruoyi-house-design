package com.ruoyi.common.utils.function;

/**
 * 由于java的java.util.function.Function函数接口不能抛异常，所以写了个抛异常的Function
 *
 * @author tongdashuai
 * @version 1.0
 * @description
 * @module
 * @date 2024/4/1 17:07
 */
public interface CustomFunction<T, R> {

    R apply(T t) throws Exception;
}
