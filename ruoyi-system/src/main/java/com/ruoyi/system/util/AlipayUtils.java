package com.ruoyi.system.util;

import cn.hutool.json.JSONUtil;
import com.alipay.api.AlipayApiException;
import com.alipay.api.domain.AlipayTradeFastpayRefundQueryModel;
import com.alipay.api.domain.AlipayTradePrecreateModel;
import com.alipay.api.domain.AlipayTradeQueryModel;
import com.alipay.api.domain.AlipayTradeRefundModel;
import com.alipay.api.request.AlipayTradeFastpayRefundQueryRequest;
import com.alipay.api.response.AlipayTradeFastpayRefundQueryResponse;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.ijpay.alipay.AliPayApi;
import com.ijpay.alipay.AliPayApiConfig;
import com.ijpay.alipay.AliPayApiConfigKit;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.AssertUtils;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.order.domain.RefundOrder;
import com.ruoyi.system.domain.PayConfig;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 *
 * @author tongdashuai
 * @version 1.0
 * @description
 * @module
 * @date 2025/3/24 10:18
 */
@Slf4j
public class AlipayUtils {

    /**
     * 支付宝应用配置版本
     */
    private static final Map<String, Integer> APP_VERSION = new ConcurrentHashMap<>() ;


    /**
     * 支付结果通知接口
     */
    private static final String NOTIFY_URL = SpringUtils.getRequiredProperty("payConfig.alipay.notifyUrl");

    /**
     * 初始化支付宝配置
     * @param payConfig
     * @return
     */
    public static AliPayApiConfig initApiConfig(PayConfig payConfig) {
        Integer version = APP_VERSION.get(payConfig.getAppid());
        if (version == null || version < payConfig.getVersion()) {
            AliPayApiConfig aliPayApiConfig = AliPayApiConfig.builder()
                    .setAppId(payConfig.getAppid())
                    .setAliPayPublicKey(payConfig.getPublicKey())
                    .setCharset(Constants.UTF8)
                    .setPrivateKey(payConfig.getPrivateKey())
                    .setServiceUrl(payConfig.getGatewayUrl())
                    .setSignType(Constants.RSA2)
                    // 普通公钥方式
                    .build();
            AliPayApiConfigKit.setThreadLocalAliPayApiConfig(aliPayApiConfig);
            return aliPayApiConfig;
        } else {
            return AliPayApiConfigKit.getApiConfig(payConfig.getAppid());
        }
    }

    /**
     * 支付宝扫码支付-统一收单线下交易预创建
     * 详见：https://opendocs.alipay.com/open/02ekfg?scene=19&pathHash=d3c84596
     *
     * @param model 支付订单信息
     * @return
     */
    public static String tradePreCreatePay(AlipayTradePrecreateModel model) {
        try {
            log.info("调用支付宝扫码付的统一收单线下交易预创建接口请求信息：{}", JSONUtil.toJsonStr(model));
            AlipayTradePrecreateResponse response = AliPayApi.tradePrecreatePayToResponse(model, NOTIFY_URL);
            log.info("调用支付宝扫码付的统一收单线下交易预创建接口返回信息：{}", JSONUtil.toJsonStr(response));
            AssertUtils.isTrue(response.isSuccess(), "支付宝扫码付统一下单接口调用失败");
            return response.getQrCode();
        } catch (Exception e) {
            log.error("支付宝扫码付统一下单接口调用失败, 错误堆栈信息：", e);
            throw new ServiceException("支付宝扫码付统一下单接口调用失败");
        }
    }

    /**
     * 查询支付宝订单信息
     *
     * @param orderNo 订单号
     * @return
     */
    public static AlipayTradeQueryResponse queryOrder(String orderNo) {
        AlipayTradeQueryModel model = new AlipayTradeQueryModel();
        model.setOutTradeNo(orderNo);
        try {
            log.info("调用支付宝查询订单接口请求信息：{}", JSONUtil.toJsonStr(model));
            AlipayTradeQueryResponse response = AliPayApi.tradeQueryToResponse(model);
            log.info("调用支付宝查询订单接口返回信息：{}", JSONUtil.toJsonStr(response));
            AssertUtils.isTrue(response.isSuccess(), "支付宝查询订单接口调用失败");
            return response;
        } catch (Exception e) {
            log.error("支付宝查询订单接口调用失败, 错误堆栈信息：", e);
            throw new ServiceException("支付宝查询订单接口调用失败");
        }
    }

    /**
     * 查询支付宝退款单信息
     *
     * @param refundOrder 退款信息
     * @return
     */
    public static AlipayTradeFastpayRefundQueryResponse queryRefundOrder(RefundOrder refundOrder) {
        AlipayTradeFastpayRefundQueryModel model = new AlipayTradeFastpayRefundQueryModel();
        model.setOutRequestNo(refundOrder.getRefundNo());
        model.setOutTradeNo(refundOrder.getOrderNo());
        try {
            log.info("调用支付宝查询退款单接口请求信息：{}", JSONUtil.toJsonStr(model));
            AlipayTradeFastpayRefundQueryResponse response = AliPayApi.tradeRefundQueryToResponse(model);
            log.info("调用支付宝查询退款单接口返回信息：{}", JSONUtil.toJsonStr(response));
            AssertUtils.isTrue(response.isSuccess(), "支付宝查询退款单接口调用失败");
            return response;
        } catch (Exception e) {
            log.error("支付宝查询退款单接口调用失败, 错误堆栈信息：", e);
            throw new ServiceException("支付宝查询退款单接口调用失败");
        }
    }


    /**
     * 支付宝退款
     *
     * @param model
     * @return
     */
    public static AlipayTradeRefundResponse refund(AlipayTradeRefundModel model) {
        try {
            log.info("调用支付宝退款接口请求信息：{}", JSONUtil.toJsonStr(model));
            AlipayTradeRefundResponse response = AliPayApi.tradeRefundToResponse(model);
            log.info("调用支付宝退款接口返回信息：{}", JSONUtil.toJsonStr(response));
            AssertUtils.isTrue(response.isSuccess(), "支付宝退款接口调用失败");
            return response;
        } catch (Exception e) {
            log.error("支付宝退款接口调用失败, 错误堆栈信息：", e);
            throw new ServiceException("支付宝退款接口调用失败");
        }
    }
}
