package com.ruoyi.pay.service;

import com.ruoyi.pay.domain.req.RefundReq;
import com.ruoyi.pay.domain.vo.RefundVO;
import com.ruoyi.system.enums.PayChannelEnum;

/**
 *
 *
 * @author tongdashuai
 * @version 1.0
 * @description
 * @module
 * @date 2025/3/27 16:18
 */
public interface IPayService {

    /**
     * 刷新订单信息
     *
     * @param orderNo 订单号
     */
    void refreshOrder(String orderNo);

    /**
     * 刷新退款单状态
     *
     * @param refundNo 退款单号
     */
    void refreshRefund(String refundNo);

    /**
     * 退款
     *
     * @param request
     * @return
     */
    RefundVO refund(RefundReq request);

    PayChannelEnum getPayChannel();
}
