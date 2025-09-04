package com.ruoyi.member.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * vip状态：0-停用 1-正常
 *
 * @author tongdashuai
 * @version 1.0
 * @description
 * @module
 * @date 2024/8/5 13:39
 */
@AllArgsConstructor
@Getter
public enum VipStatusEnum {

    STOP(0,"停用"),

    ENABLE(1,"启用");

    private Integer code;

    private String name;
}
