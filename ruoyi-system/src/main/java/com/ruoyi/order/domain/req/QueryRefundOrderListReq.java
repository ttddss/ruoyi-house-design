package com.ruoyi.order.domain.req;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.io.Serializable;
import java.util.Date;

/**
 * 查询退款单对象
 * 
 * @author tds
 * @date 2025-03-21
 */
@Data
public class QueryRefundOrderListReq implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 退款单号 */
    private String refundNo;

    /** 支付单号 */
    private String orderNo;

    /** 用户手机号 */
    private Long phonenumber;

    /** 退款状态 */
    private Integer status;

    /** 创建时间 */
    private Date beginCreateTime;

    /** 创建时间 */
    private Date endCreateTime;


}
