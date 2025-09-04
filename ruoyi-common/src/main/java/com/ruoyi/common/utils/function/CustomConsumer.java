package com.ruoyi.common.utils.function;

/**
 * 由于java的java.util.function.Consumer函数接口不能抛异常，所以写了个抛异常的Consumer
 *
 * @author tongdashuai
 * @version 1.0
 * @description
 * @module
 * @date 2024/4/1 17:07
 */
public interface CustomConsumer<T> {

    void accept(T t) throws Exception;
}
