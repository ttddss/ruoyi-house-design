package com.ruoyi.pay.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 *
 *
 * @author tongdashuai
 * @version 1.0
 * @description 交易类型： 0-消费 1-退款 1-其他
 * @module
 * @date 2023/3/14 15:56
 */
@Getter
@AllArgsConstructor
public enum TransactionTypeEnum {

    /**
     * 消费
     */
    CONSUME(0, "消费", "交易", "SUCCESS"),

    /**
     * 退款
     */
    REFUND(1, "退款", "退款", "REFUND"),

    /**
     * 其他
     */
    OTHER(2, "其他", "其他", "其他");


    private int code;

    private String name;

    /**
     * 支付宝账单业务类型名称
     */
    private String aliBillName;

    /**
     * 微信账单业务类型名称
     */
    private String wxBillName;



    private static Map<Integer, TransactionTypeEnum> cacheMap = new HashMap<>();
    private static Map<String, TransactionTypeEnum> cacheAliNameMap = new HashMap<>();
    private static Map<String, TransactionTypeEnum> cacheWxNameMap = new HashMap<>();

    static {
        for (TransactionTypeEnum item : values()) {
            cacheMap.put(item.getCode(), item);
            cacheAliNameMap.put(item.getAliBillName(), item);
            cacheWxNameMap.put(item.getWxBillName(), item);
        }
    }

    public static TransactionTypeEnum valueCodeOf(Integer code) {
        return cacheMap.get(code);
    }

    public static TransactionTypeEnum valueAliNameOf(String name) {
        return cacheAliNameMap.get(name) == null ? OTHER : cacheAliNameMap.get(name);
    }

    public static TransactionTypeEnum valueWxNameOf(String name) {
        return cacheWxNameMap.get(name) == null ? OTHER : cacheWxNameMap.get(name);
    }
}
