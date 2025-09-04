package com.ruoyi.order.domain.vo;

import java.math.BigDecimal;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import lombok.Data;
import java.io.Serializable;
import java.util.Date;

/**
 * 退款单vo对象
 * 
 * @author tds
 * @date 2025-03-21
 */
@Data
public class RefundOrderInfoVO implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 退款单号 */
    private String refundNo;

    /** 支付单号 */
    private String orderNo;

    /**
     * 外部交易流水号
     */
    private String outerTradeNo;

    /** 用户ID */
    private Long userId;

    /** 退款金额 */
    private BigDecimal amount;

    /** 退款描述信息 */
    private String description;

    /** 退款状态 */
    private Integer status;

    /** 响应编码 */
    private String code;

    /** 响应报文 */
    private String message;

    /** 删除标志: 0-代表存在 其他代表删除 */
    private Long delFlag;

    /** 备注 */
    private String remark;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;


    /** 手机号 */
    @Excel(name = "手机号")
    private String phonenumber;

    /** 用户昵称 */
    @Excel(name = "用户昵称")
    private String nickName;

}
