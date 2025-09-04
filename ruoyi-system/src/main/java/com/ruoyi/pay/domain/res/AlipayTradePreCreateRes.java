package com.ruoyi.pay.domain.res;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * 支付宝扫码支付-统一收单线下交易预创建响应结果.
 *
 * @author tongdashuai
 * @version 1.0
 * @description
 * @module
 * @date 2025/3/24 14:35
 */
@Data
@Builder
public class AlipayTradePreCreateRes implements Serializable {

    private static final long serialVersionUID = -1912069606248541016L;

    /**
     * 支付宝扫码付二维码链接
     */
    private String qrCode;

    /**
     * 订单号
     */
    private String orderNo;
}
