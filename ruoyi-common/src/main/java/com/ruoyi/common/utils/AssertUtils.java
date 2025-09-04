package com.ruoyi.common.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.ruoyi.common.enums.ResultEnum;
import com.ruoyi.common.exception.ServiceException;

import java.util.List;
import java.util.Objects;

/**
 * @author xzh
 * @since 2024/2/29 13:50
 */
public class AssertUtils {
    public static void notNull( Object object, String message) {
        if (Objects.isNull(object)) {
            throw new ServiceException(message);
        }
    }
    public static void isNull( Object object, String message) {
        if (Objects.nonNull(object)) {
            throw new ServiceException(message);
        }
    }
    public static void isTrue(boolean expression, String message) {
        if (!expression) {
            throw new ServiceException(message);
        }
    }
    public static void isFalse(boolean expression, String message) {
        if (expression) {
            throw new ServiceException(message);
        }
    }

    public static void isFalse(boolean expression, ResultEnum resultEnum) {
        if (expression) {
            throw new ServiceException(resultEnum.getName(), resultEnum.getCode(), resultEnum.getScode());
        }
    }

    /**
     * 断言给定集合是否包含元素，集合必须不为 null 且至少包含一个元素
     *
     * @param list 被检查的集合
     * @param message 错误提示信息
     */
    public static void notEmpty(List list, String message) {
        if (CollUtil.isEmpty(list)) {
            throw new ServiceException(message);
        }
    }

    /**
     * 断言给定字符串是否不为空。如果str为空字符串，则抛出异常
     *
     * @param str 被检查的字符串
     * @param message 错误提示信息
     */
    public static void notBlank(String str, String message) {
        if (StrUtil.isBlank(str)) {
            throw new ServiceException(message);
        }
    }
}
