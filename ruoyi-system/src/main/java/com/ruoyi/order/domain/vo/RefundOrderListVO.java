package com.ruoyi.order.domain.vo;

import java.math.BigDecimal;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import com.ruoyi.common.annotation.Excel;
import java.io.Serializable;
import java.util.Date;

/**
 * 退款单集合vo对象
 * 
 * @author tds
 * @date 2025-03-21
 */
@Data
public class RefundOrderListVO implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    @Excel(name = "主键")
    private Long id;

    /** 退款单号 */
    @Excel(name = "退款单号")
    private String refundNo;

    /** 支付单号 */
    @Excel(name = "支付单号")
    private String orderNo;

    /**
     * 外部交易流水号
     */
    private String outerTradeNo;

    /** 手机号 */
    @Excel(name = "手机号")
    private String phonenumber;

    /** 用户昵称 */
    @Excel(name = "用户昵称")
    private String nickName;

    /** 退款金额 */
    @Excel(name = "退款金额")
    private BigDecimal amount;

    /** 退款描述信息 */
    @Excel(name = "退款描述信息")
    private String description;

    /** 退款状态 */
    @Excel(name = "退款状态")
    private Integer status;

    /** 响应编码 */
    @Excel(name = "响应编码")
    private String code;

    /** 响应报文 */
    @Excel(name = "响应报文")
    private String message;

    /** 备注 */
    @Excel(name = "备注")
    private String remark;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

}
