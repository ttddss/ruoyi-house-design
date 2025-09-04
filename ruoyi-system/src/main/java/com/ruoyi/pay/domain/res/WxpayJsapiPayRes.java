package com.ruoyi.pay.domain.res;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * 微信jsapi支付下单响应结果.
 *
 * @author tongdashuai
 * @version 1.0
 * @description
 * @module
 * @date 2025/3/24 14:35
 */
@Data
@Builder
public class WxpayJsapiPayRes implements Serializable {

    private static final long serialVersionUID = -1912069606248541016L;

    /**
     * 公众号ID
     */
    private String appId;

    /**
     * 时间戳
     */
    private String timestamp;

    /**
     * 随机串
     */
    private String nonceStr;

    /**
     * JSAPI下单接口返回的prepay_id参数值，提交格式如：prepay_id=***
     */
    private String packageVal;

    /**
     * 签名类型，该接口V3版本仅支持RSA
     */
    private String signType;

    /**
     * 签名
     */
    private String paySign;
}
