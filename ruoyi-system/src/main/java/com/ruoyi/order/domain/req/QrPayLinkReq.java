package com.ruoyi.order.domain.req;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 获取二维码支付链接对象
 * 
 * @author tds
 * @date 2025-03-13
 */
@Data
public class QrPayLinkReq implements Serializable
{
    private static final long serialVersionUID = 1L;

    private Long vipId;

    /**
     * 开通月份
     */
    private Integer months;

    /**
     * 订单类型：0-vip订单 1-图纸订单
     */
    @NotNull(message = "订单类型不能为空")
    private Long type;

}
