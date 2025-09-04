package com.ruoyi.order.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * 订单状态：0-支付中 1-支付成功 2-取消支付 3-关闭 4-支付异常 5-已退款
 *
 * @author tongdashuai
 * @version 1.0
 * @description
 * @module
 * @date 2024/8/5 13:39
 */
@AllArgsConstructor
@Getter
public enum PayStatusEnum {

    PAYING(0,"支付中"),

    SUCCESS(1,"支付成功"),

    CANCEL(2,"取消支付"),

    CLOSE(3,"关闭"),

    ERROR(4,"支付异常"),

    REFUNDED(5,"已退款") ;

    private int code;

    private String name;

    private static final Map<Integer, PayStatusEnum> CACHE_MAP = new HashMap<>();

    static {
        for (PayStatusEnum payStatusEnum : values()) {
            CACHE_MAP.put(payStatusEnum.getCode(), payStatusEnum);
        }
    }

    public static String getNameByCode(Integer code) {
        return CACHE_MAP.get(code) == null ? "" : CACHE_MAP.get(code).getName();
    }
}
