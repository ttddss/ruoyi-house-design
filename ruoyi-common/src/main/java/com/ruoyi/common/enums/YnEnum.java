package com.ruoyi.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 是否枚举：0-否 1-是
 *
 * @author tongdashuai
 * @version 1.0
 * @description
 * @module
 * @date 2024/8/12 13:39
 */
@AllArgsConstructor
@Getter
public enum YnEnum {

    NO(0, "0","否"),
    YES(1, "1", "是");



    private Integer code;

    private String scode;

    private String name;
}
