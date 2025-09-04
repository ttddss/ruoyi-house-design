package com.ruoyi.system.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * 图片上传的文件对应的业务类型：0-默认 1-图纸图片 2-图纸文件
 *
 * @author tongdashuai
 * @version 1.0
 * @description
 * @module
 * @date 2024/8/5 13:39
 */
@AllArgsConstructor
@Getter
public enum DictTypeEnum {

    HOUSE_TYPE("houseType","房型", "house_type"),

    BAY_TYPE("bayType","室型", "bay_type") ,

    BLUEPRINTS_STYLE("blueprintsStyle","图纸风格", "blueprints_style") ;

    private String code;

    private String name;

    /**
     * 字典类型
     */
    private String type;


    private static final Map<String, DictTypeEnum> CACHE = new HashMap<>();

    static {
        for (DictTypeEnum bizTypeEnum : values()) {
            CACHE.put(bizTypeEnum.getCode(), bizTypeEnum);
        }
    }

    public static DictTypeEnum getByCode(String code) {
        return CACHE.get(code);
    }
}
