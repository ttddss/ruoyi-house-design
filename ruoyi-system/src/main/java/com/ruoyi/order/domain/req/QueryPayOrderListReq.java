package com.ruoyi.order.domain.req;

import java.math.BigDecimal;
import lombok.Data;
import java.io.Serializable;
import java.util.Date;

/**
 * 查询支付订单对象
 * 
 * @author tds
 * @date 2025-03-21
 */
@Data
public class QueryPayOrderListReq implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 订单号 */
    private String orderNo;

    /** 支付渠道 */
    private Integer channel;

    /** 子支付渠道 */
    private Integer subChannel;

    /** 用户手机号 */
    private Long phonenumber;

    /**
     * 用户id
     */
    private Long userId;


    /** 订单状态 */
    private Integer status;

    /** 外部交易流水号 */
    private String outerTradeNo;

    /** 开始创建时间 */
    private Date beginCreateTime;

    /** 结束创建时间 */
    private Date endCreateTime;

}
