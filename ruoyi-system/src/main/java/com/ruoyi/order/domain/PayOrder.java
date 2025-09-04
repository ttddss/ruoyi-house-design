package com.ruoyi.order.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 支付订单对象 pay_order
 * 
 * @author tds
 * @date 2025-03-21
 */
@TableName("pay_order")
@Data
@NoArgsConstructor
public class PayOrder implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 订单号 */
    private String orderNo;

    /** 支付渠道 */
    private Integer channel;

    /** 子支付渠道 */
    private Integer subChannel;

    /** 用户ID */
    private Long userId;

    /** vipID */
    private Long vipId;

    /** 图纸ID */
    private Long blueprintsId;

    /** 订单状态 */
    private Integer status;

    /** 商品类型：0-vip 1-图纸 */
    private Integer goodsType;

    /** 商品数量 */
    private Integer goodsNum;

    /** 订单金额 */
    private BigDecimal amount;

    /** 优惠金额 */
    private BigDecimal discountAmount;

    /** 折扣 */
    private BigDecimal discount;

    /** 实付金额 */
    private BigDecimal actualAmount;

    /** 订单标题 */
    private String subject;

    /** 外部交易流水号 */
    private String outerTradeNo;

    /** 应用id */
    private String appid;

    /** 商户号 */
    private String mchid;

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
