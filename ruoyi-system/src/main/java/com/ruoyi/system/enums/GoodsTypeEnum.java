package com.ruoyi.system.enums;

import cn.hutool.core.map.MapUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * 商品类型：0-vip 1-图纸
 *
 * @author tongdashuai
 * @version 1.0
 * @description
 * @module
 * @date 2024/8/5 13:39
 */
@AllArgsConstructor
@Getter
public enum GoodsTypeEnum {

    VIP(0,"vip"),

    BLUEPRINTS(1,"图纸") ;

    private int code;

    private String name;

    private static final Map<Integer, GoodsTypeEnum> CACHE_MAP = new HashMap<>();

    static {
        for (GoodsTypeEnum goodsTypeEnum : values()) {
            CACHE_MAP.put(goodsTypeEnum.getCode(), goodsTypeEnum);
        }
    }

    public static GoodsTypeEnum getEnumByCode(Integer goodsType) {
       return CACHE_MAP.get(goodsType);
    }
}
