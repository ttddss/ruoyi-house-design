package com.ruoyi.pay.service;

import com.ruoyi.pay.domain.req.AlipayRefreshOrderReq;
import com.ruoyi.pay.domain.req.AlipayRefundReq;
import com.ruoyi.pay.domain.req.AlipayTradePreCreateReq;
import com.ruoyi.pay.domain.res.AlipayTradePreCreateRes;
import com.ruoyi.pay.domain.vo.AlipayOrderVO;
import com.ruoyi.pay.domain.vo.AlipayRefundVO;
import com.ruoyi.system.enums.PayChannelEnum;

/**
 *
 *
 * @author tongdashuai
 * @version 1.0
 * @description
 * @module
 * @date 2025/3/24 14:12
 */
public interface IAlipayService extends IPayService {

    /**
     * 扫码支付-统一收单线下交易预创建
     *
     * @param request
     * @return
     */
    AlipayTradePreCreateRes tradePreCreate(AlipayTradePreCreateReq request);


    /**
     * 查询订单信息vo
     * @param orderNo
     * @return
     */
    AlipayOrderVO queryOrder(String orderNo);



    @Override
    default PayChannelEnum getPayChannel() {
        return PayChannelEnum.ALIPAY;
    }
}
