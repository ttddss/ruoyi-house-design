package com.ruoyi.system.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * 反馈状态：0-待处理 1-已解决 2-已关闭
 *
 * @author tongdashuai
 * @version 1.0
 * @description
 * @module
 * @date 2024/8/5 13:39
 */
@AllArgsConstructor
@Getter
public enum FeedbackStatusEnum {

    PENDING(0,"待处理"),

    RESOLVED(1,"已解决") ,

    CLOSED(2,"已关闭");

    private int code;

    private String name;



    private static final Map<Integer, FeedbackStatusEnum> CACHE = new HashMap<>();

    static {
        for (FeedbackStatusEnum bizTypeEnum : values()) {
            CACHE.put(bizTypeEnum.getCode(), bizTypeEnum);
        }
    }

    public static FeedbackStatusEnum getByCode(int code) {
        return CACHE.get(code);
    }
}
