package com.ruoyi.blueprints.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 上架状态：0-已下架 1-已上架
 *
 * @author tongdashuai
 * @version 1.0
 * @description
 * @module
 * @date 2024/8/5 13:39
 */
@AllArgsConstructor
@Getter
public enum ListingStatusEnum {

    REMOVED(0,"已下架"),

    LISTED(1,"已上架");

    private Integer code;

    private String name;
}
