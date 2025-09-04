package com.ruoyi.pay.domain.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 支付宝刷新订单请求.
 *
 * @author tongdashuai
 * @version 1.0
 * @description
 * @module
 * @date 2025/3/24 14:35
 */
@Data
public class AlipayOrderVO implements Serializable {


    private static final long serialVersionUID = 7568848579322484192L;

    /**
     * 订单状态：0-支付中 1-支付成功 2-取消支付 3-关闭 4-支付异常 5-已退款
     */
    private Integer status;

}
