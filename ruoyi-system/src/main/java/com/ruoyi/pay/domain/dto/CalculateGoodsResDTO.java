package com.ruoyi.pay.domain.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

/**
 *
 *
 * @author tongdashuai
 * @version 1.0
 * @description
 * @module
 * @date 2025/3/25 18:08
 */
@Data
@Builder
public class CalculateGoodsResDTO {

    /**
     * 折扣
     */
    private BigDecimal discount;

    /**
     * 商品信息
     */
    private String subject;

    /**
     * 商品金额
     */
    private BigDecimal amount;

    /**
     * 实付金额
     */
    private BigDecimal actualAmount;

    /**
     * 优惠金额
     */
    private BigDecimal discountAmount;
}
