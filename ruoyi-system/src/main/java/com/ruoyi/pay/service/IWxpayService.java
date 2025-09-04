package com.ruoyi.pay.service;

import com.ruoyi.pay.domain.req.WxpayJsapiPayReq;
import com.ruoyi.pay.domain.req.WxpayQueryAuthReq;
import com.ruoyi.pay.domain.req.WxpayRefreshOrderReq;
import com.ruoyi.pay.domain.req.WxpayRefundReq;
import com.ruoyi.pay.domain.res.WxpayJsapiPayRes;
import com.ruoyi.pay.domain.vo.WxpayOrderVO;
import com.ruoyi.pay.domain.vo.WxpayQueryAuthVO;
import com.ruoyi.pay.domain.vo.WxpayRefundVO;
import com.ruoyi.system.enums.PayChannelEnum;

import javax.servlet.http.HttpServletRequest;

/**
 *
 *
 * @author tongdashuai
 * @version 1.0
 * @description
 * @module
 * @date 2025/3/24 14:12
 */
public interface IWxpayService extends IPayService {



    /**
     * 微信jsapi支付下单
     *
     * @param request
     * @return
     */
    WxpayJsapiPayRes jsapiPay(WxpayJsapiPayReq request);

    /**
     * 微信支付结果通知
     *
     * @param request
     * @param orderNo 订单号
     */
    void payNotify(HttpServletRequest request, String orderNo);

    /**
     * 查询订单信息
     *
     * @param orderNo 订单号
     * @return
     */
    WxpayOrderVO queryOrder(String orderNo);




    /**
     * 微信退款通知处理
     * @param request
     * @param refundNo
     */
    void refundNotify(HttpServletRequest request, String refundNo);



    /**
     * 获取授权信息
     * @param request
     * @return
     */
    WxpayQueryAuthVO getAuthInfo(WxpayQueryAuthReq request);

    @Override
    default PayChannelEnum getPayChannel() {
        return PayChannelEnum.WX;
    }
}
