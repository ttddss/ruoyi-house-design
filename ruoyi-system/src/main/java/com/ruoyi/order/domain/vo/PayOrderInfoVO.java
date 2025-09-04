package com.ruoyi.order.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 支付订单vo对象
 * 
 * @author tds
 * @date 2025-03-21
 */
@Data
public class PayOrderInfoVO implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 订单号 */
    private String orderNo;

    /** 支付渠道 */
    private Integer channel;

    /** 子支付渠道 */
    private Integer subChannel;

    /** 手机号 */
    private String phonenumber;

    /** 用户昵称 */
    private String nickName;

    /** 订单状态 */
    private Integer status;

    /** 用户ID */
    private Long userId;

    /** vipID */
    private Long vipId;

    /** 图纸ID */
    private Long blueprintsId;

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
    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /** 商品类型：0-vip 1-图纸 */
    private Integer goodsType;

    /** 商品数量 */
    private Integer goodsNum;

}
