package com.ruoyi.order.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * 退款状态：0-退款中 1-退款成功 2-退款失败 3-系统异常
 *
 * @author tongdashuai
 * @version 1.0
 * @description
 * @module
 * @date 2024/8/5 13:39
 */
@AllArgsConstructor
@Getter
public enum RefundStatusEnum {

    REFUNDING(0,"退款中"),

    SUCCESS(1,"退款成功"),

    FAIL(2,"退款失败"),

    ERROR(3,"系统异常") ;

    private int code;

    private String name;

    private static final Map<Integer, RefundStatusEnum> CACHE_MAP = new HashMap<>();

    static {
        for (RefundStatusEnum payStatusEnum : values()) {
            CACHE_MAP.put(payStatusEnum.getCode(), payStatusEnum);
        }
    }

    public static String getNameByCode(Integer code) {
        return CACHE_MAP.get(code) == null ? "" : CACHE_MAP.get(code).getName();
    }
}
