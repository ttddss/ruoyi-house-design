package com.ruoyi.pay.domain.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 支付宝对账单表
 * </p>
 *
 * @author tds
 * @since 2023-03-31
 */
@Data
@Builder
public class AlipayStatementDTO  {


    /**
     * 应用id
     */
    private String appid;

    /**
     * 子应用id
     */
    private String subAppid;

    /**
     * 第三方创建时间
     */
    private Date thirdCreateTime;

    /**
     * 第三方完成时间
     */
    private Date thirdFinishTime;

    /**
     * 商户订单号
     */
    private String orderNo;

    /**
     * 第三方平台订单号
     */
    private String thirdOrderNo;

    /**
     * 商户退款单号
     */
    private String refundNo;


    /**
     * 交易类型： 0-消费 1-退款 1-其他
     */
    private Integer transactionType;

    /**
     * 商品名称
     */
    private String subject;

    /**
     * 门店编号
     */
    private String storeNo;

    /**
     * 门店名称
     */
    private String storeName;

    /**
     * 操作员
     */
    private String operator;

    /**
     * 终端号
     */
    private String terminalNo;

    /**
     * 收款账户
     */
    private String buyAccount;

    /**
     * 订单金额
     */
    private BigDecimal amount;

    /**
     * 商家实收金额
     */
    private BigDecimal realAmount;

    /**
     * 服务费
     */
    private BigDecimal serviceFee;

    /**
     * 分润
     */
    private BigDecimal commission;

    /**
     * 更新时间
     */
    private Date updateTime;


}
