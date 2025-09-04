package com.ruoyi.system.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 子支付渠道：0-支付宝扫码付 1-支付宝手机网站支付 20-微信native支付 21-微信jsapi网页支付  22-微信小程序支付 99-其他
 *
 * @author tongdashuai
 * @version 1.0
 * @description
 * @module
 * @date 2024/8/5 13:39
 */
@AllArgsConstructor
@Getter
public enum PaySubChannelEnum {

    ALIPAY_SCAN_QR(0,"支付宝扫码付"),

    ALIPAY_WEB(1,"支付宝手机网站支付"),

    WX_NATIVE(2,"微信native支付"),

    WX_JSAPI(3,"微信jsapi网页支付"),

    WX_APPLET(4,"微信小程序支付"),

    OTHER(99,"其他") ;

    private int code;

    private String name;



}
