package com.ruoyi.pay.domain.res;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 微信支付通知参数.
 * 具体见：https://pay.weixin.qq.com/doc/v3/merchant/4012791861
 *
 * @author tongdashuai
 * @version 1.0
 * @description
 * @module
 * @date 2025/3/24 14:35
 */
@Data
@NoArgsConstructor
public class WxpayPayNotifyRes implements Serializable {

    private static final long serialVersionUID = -1912069606248541016L;

    /**
     * 【通知ID】回调通知的唯一编号。
     */
    private String id;

    /**
     * 【通知创建时间】
     * 1、定义：本次回调通知创建的时间。
     * 2、格式：遵循rfc3339标准格式：yyyy-MM-DDTHH:mm:ss+TIMEZONE。yyyy-MM-DD 表示年月日；T 字符用于分隔日期和时间部分；HH:mm:ss 表示具体的时分秒；TIMEZONE 表示时区（例如，+08:00 对应东八区时间，即北京时间）。
     * 示例：2015-05-20T13:29:35+08:00 表示北京时间2015年5月20日13点29分35秒。
     */
    private String create_time;

    /**
     * 【通知数据类型】通知的资源数据类型，固定为encrypt-resource。
     */
    private String resource_type;

    /**
     * 【通知的类型】微信支付回调通知的类型。
     * 支付成功通知的类型为TRANSACTION.SUCCESS。
     */
    private String event_type;

    /**
     * 【回调摘要】微信支付对回调内容的摘要备注。
     */
    private String summary;

    /**
     * 【通知数据】通知资源数据。
     */
    private ResourceBean resource;

    @NoArgsConstructor
    @Data
    public static class ResourceBean {
        /**
         * original_type : transaction
         * algorithm : AEAD_AES_256_GCM
         * ciphertext :
         * associated_data :
         * nonce :
         */

        private String original_type;
        private String algorithm;
        private String ciphertext;
        private String associated_data;
        private String nonce;
    }
}
