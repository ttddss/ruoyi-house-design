package com.ruoyi.order.domain.vo;

import java.math.BigDecimal;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import com.ruoyi.common.annotation.Excel;
import java.io.Serializable;
import java.util.Date;

/**
 * 支付订单集合vo对象
 * 
 * @author tds
 * @date 2025-03-21
 */
@Data
public class PayOrderListVO implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    @Excel(name = "主键")
    private Long id;

    /** 订单号 */
    @Excel(name = "订单号")
    private String orderNo;

    /** 支付渠道 */
    @Excel(name = "支付渠道")
    private Integer channel;

    /** 子支付渠道 */
    @Excel(name = "子支付渠道")
    private Integer subChannel;

    /** 手机号 */
    @Excel(name = "手机号")
    private String phonenumber;

    /** 用户昵称 */
    @Excel(name = "用户昵称")
    private String nickName;

    /** 用户ID */
    private Long userId;

    /** vipID */
    private Long vipId;

    /** 图纸ID */
    private Long blueprintsId;

    /** 订单状态 */
    @Excel(name = "订单状态")
    private Integer status;

    /** 商品类型：0-vip 1-图纸 */
    private Integer goodsType;

    /** 商品数量 */
    private Integer goodsNum;

    /** 订单金额 */
    @Excel(name = "订单金额")
    private BigDecimal amount;

    /** 优惠金额 */
    @Excel(name = "优惠金额")
    private BigDecimal discountAmount;



    /** 折扣 */
    @Excel(name = "折扣")
    private BigDecimal discount;

    /** 实付金额 */
    @Excel(name = "实付金额")
    private BigDecimal actualAmount;

    /** 订单标题 */
    @Excel(name = "订单标题")
    private String subject;

    /** 外部交易流水号 */
    @Excel(name = "外部交易流水号")
    private String outerTradeNo;

    /** 响应编码 */
    @Excel(name = "响应编码")
    private String code;

    /** 响应报文 */
    @Excel(name = "响应报文")
    private String message;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;


    /**
     * 评价数
     */
    private Integer evaluateNum;
}
