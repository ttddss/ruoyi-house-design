package com.ruoyi.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 登录类型：0-管理端用户 1-移动端学员
 *
 * @author tongdashuai
 * @version 1.0
 * @description
 * @module
 * @date 2024/8/12 13:39
 */
@AllArgsConstructor
@Getter
public enum LoginTypeEnum {

    MANAGEMENT_USER(0, "管理端用户"),

    MOBILE_STUDNET(1, "移动端学员");

    private Integer code;

    private String name;
}
