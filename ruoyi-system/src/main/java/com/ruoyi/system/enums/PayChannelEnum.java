package com.ruoyi.system.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 支付渠道枚举：0-支付宝 1-微信 99-其他
 *
 * @author tongdashuai
 * @version 1.0
 * @description
 * @module
 * @date 2024/8/5 13:39
 */
@AllArgsConstructor
@Getter
public enum PayChannelEnum {

    ALIPAY(0,"支付宝"),

    WX(1,"微信"),

    OTHER(99,"其他");

    private int code;

    private String name;




}
