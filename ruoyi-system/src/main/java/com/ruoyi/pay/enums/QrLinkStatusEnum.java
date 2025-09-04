package com.ruoyi.pay.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * 支付二维码链接状态：0-支付中 1-已失效 2-支付成功
 *
 * @author tongdashuai
 * @version 1.0
 * @description
 * @module
 * @date 2024/8/5 13:39
 */
@AllArgsConstructor
@Getter
public enum QrLinkStatusEnum {

    PAYING(0,"支付中"),

    EXPIRED(1,"已失效"),

    SUCCESS(2,"支付成功");

    private int code;

    private String name;

    private static final Map<Integer, QrLinkStatusEnum> CACHE_MAP = new HashMap<>();

    static {
        for (QrLinkStatusEnum payStatusEnum : values()) {
            CACHE_MAP.put(payStatusEnum.getCode(), payStatusEnum);
        }
    }

    public static String getNameByCode(Integer code) {
        return CACHE_MAP.get(code) == null ? "" : CACHE_MAP.get(code).getName();
    }
}
