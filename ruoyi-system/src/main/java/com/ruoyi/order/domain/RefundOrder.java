package com.ruoyi.order.domain;

import java.math.BigDecimal;
import lombok.Data;
import com.baomidou.mybatisplus.annotation.*;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.util.Date;

/**
 * 退款单对象 refund_order
 * 
 * @author tds
 * @date 2025-03-21
 */
@TableName("refund_order")
@Data
@NoArgsConstructor
public class RefundOrder implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    @TableId(type = IdType.AUTO)
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

    /** 创建人 */
    private String createBy;

    /** 创建时间 */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /** 更新人 */
    private String updateBy;

    /** 更新时间 */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;




}
