package com.ruoyi.system.util;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.ijpay.core.IJPayHttpResponse;
import com.ijpay.core.enums.RequestMethod;
import com.ijpay.core.enums.SignType;
import com.ijpay.core.kit.HttpKit;
import com.ijpay.core.kit.WxPayKit;
import com.ijpay.core.utils.DateTimeZoneUtil;
import com.ijpay.wxpay.WxPayApi;
import com.ijpay.wxpay.enums.WxDomainEnum;
import com.ijpay.wxpay.enums.v3.BasePayApiEnum;
import com.ijpay.wxpay.model.v3.*;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.AssertUtils;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.order.domain.PayOrder;
import com.ruoyi.order.domain.RefundOrder;
import com.ruoyi.pay.domain.req.WxpayJsapiPayReq;
import com.ruoyi.pay.domain.res.WxpayJsapiPayRes;
import com.ruoyi.pay.domain.res.WxpayQueryOrderRes;
import com.ruoyi.pay.domain.res.WxpayQueryRefundRes;
import com.ruoyi.pay.domain.res.WxpayRefundRes;
import com.ruoyi.system.domain.PayConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

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
public class WxpayUtils {


    /**
     * 支付结果通知接口
     */
    private static final String NOTIFY_URL = SpringUtils.getRequiredProperty("payConfig.wxpay.notifyUrl");

    /**
     * 退款结果通知接口
     */
    private static final String REFUND_NOTIFY_URL = SpringUtils.getRequiredProperty("payConfig.wxpay.refundNotifyUrl");

    private final static int OK = 200;

    public static final String WECHAT_PAY_SERIAL = "Wechatpay-Serial";
    public static final String WECHAT_PAY_SIGNATURE = "Wechatpay-Signature";
    public static final String WECHAT_PAY_TIMESTAMP = "Wechatpay-Timestamp";
    public static final String WECHAT_PAY_NONCE = "Wechatpay-Nonce";

    /**
     * 退款币种-人民币币种
     */
    private static final String CNY = "CNY";


    /**
     * 从字符串中加载RSA私钥。
     *
     * @param keyString 私钥字符串
     * @return RSA私钥
     */
    public static PrivateKey loadPrivateKeyFromString(String keyString) {
        try {
            keyString =
                    keyString
                            .replace("-----BEGIN PRIVATE KEY-----", "")
                            .replace("-----END PRIVATE KEY-----", "")
                            .replaceAll("\\s+", "");
            return KeyFactory.getInstance("RSA")
                    .generatePrivate(new PKCS8EncodedKeySpec(Base64.getDecoder().decode(keyString)));
        } catch (NoSuchAlgorithmException e) {
            throw new UnsupportedOperationException(e);
        } catch (InvalidKeySpecException e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * jsapi统一下单请求
     *
     * @param openId 用户openId
     * @param payConfig 支付配置信息
     * @param payOrder 订单信息
     * @return
     */
    public static WxpayJsapiPayRes jsapiPay(String openId, PayConfig payConfig, PayOrder payOrder) {
        try {
            // 5分钟过期
            String timeExpire = DateTimeZoneUtil.dateToTimeZone(System.currentTimeMillis() + 1000 * 60 * 5);
            PrivateKey privateKey = loadPrivateKeyFromString(payConfig.getPrivateKey());

            // 实际金额，单位分
            BigDecimal actualAmount = payOrder.getActualAmount().multiply(new BigDecimal(100));
            UnifiedOrderModel unifiedOrderModel = new UnifiedOrderModel()
                    .setAppid(payConfig.getAppid())
                    .setMchid(payConfig.getMchid())
                    .setDescription(payOrder.getSubject())
                    .setOut_trade_no(payOrder.getOrderNo())
                    .setTime_expire(timeExpire)
                    .setNotify_url(NOTIFY_URL + "/" + payOrder.getOrderNo())
                    .setAmount(new Amount().setTotal(actualAmount.intValue()))
                    .setPayer(new Payer().setOpenid(openId));

            log.info("统一下单参数 {}", JSONUtil.toJsonStr(unifiedOrderModel));
            IJPayHttpResponse response = WxPayApi.v3(
                    RequestMethod.POST,
                    WxDomainEnum.CHINA.toString(),
                    BasePayApiEnum.JS_API_PAY.toString(),
                    payConfig.getMchid(),
                    payConfig.getSerialNum(),
                    null,
                    privateKey,
                    JSONUtil.toJsonStr(unifiedOrderModel)
            );
            log.info("统一下单响应 {}", response);
            AssertUtils.isTrue(response.getStatus() == OK, "微信统一下单接口请求失败");


            // 返回结果验签
            boolean verifySignature = WxPayKit.verifySignature(response, payConfig.getPlatCertPath());
            log.info("验签结果: {}", verifySignature);
            AssertUtils.isTrue(verifySignature, "微信统一下单接口验签失败");

            // jsapi支付参数签名
            String body = response.getBody();
            String prepayId = JSONUtil.parseObj(body).getStr("prepay_id");
            Map<String, String> map = WxPayKit.jsApiCreateSign(payConfig.getAppid(), prepayId, privateKey);
            return BeanUtil.mapToBean(map, WxpayJsapiPayRes.class, true, CopyOptions.create());
        } catch (Exception e) {
            log.error("jsapi统一下单接口调用失败", e);
            throw new ServiceException("jsapi统一下单接口调用失败");
        }
    }


    /**
     * 解析微信支付通知信息
     *
     * @param request
     * @param payConfig 支付配置信息
     */
    public static void parseNotify(HttpServletRequest request, PayConfig payConfig) {
        try {
            // 获取部分请求头
            String body = HttpKit.readData(request);

            String timestamp = request.getHeader(WECHAT_PAY_TIMESTAMP);
            String nonce = request.getHeader(WECHAT_PAY_NONCE);
            String serialNo = request.getHeader(WECHAT_PAY_SERIAL);
            String signature = request.getHeader(WECHAT_PAY_SIGNATURE);
            log.info("支付通知请求头timestamp:{} nonce:{} serialNo:{} signature:{}", timestamp, nonce, serialNo, signature);
            log.info("支付通知密文 {}", body);
            // 验签并获取通知明文
            String plainText = WxPayKit.verifyNotify(serialNo, body, signature, nonce, timestamp, payConfig.getApiKey(), payConfig.getPlatCertPath());
            log.info("微信支付通知明文: {}", plainText);
            AssertUtils.notBlank(plainText, "签名错误");
        } catch (Exception e) {
            log.error("微信支付通知结果解析失败,异常堆栈信息：", e);
            throw new ServiceException("微信支付通知结果解析失败");
        }

    }

    /**
     * 查询订单信息
     *
     * @param payConfig
     * @param payOrder
     */
    public static WxpayQueryOrderRes queryOrder(PayConfig payConfig, PayOrder payOrder) {
        try {
            Map<String, String> params = new HashMap<>();
            params.put("mchid", payConfig.getMchid());
            log.info("微信查询订单信息请求参数.mchId:{}, orderNo:{}", payConfig.getMchid(), payOrder.getOrderNo());
            IJPayHttpResponse response = WxPayApi.v3(
                    RequestMethod.GET,
                    WxDomainEnum.CHINA.toString(),
                    String.format(BasePayApiEnum.ORDER_QUERY_BY_OUT_TRADE_NO.toString(), payOrder.getOrderNo()),
                    payConfig.getMchid(),
                    payConfig.getSerialNum(),
                    null,
                    loadPrivateKeyFromString(payConfig.getPrivateKey()),
                    params
            );
            log.info("微信查询订单信息响应 {}", response);
            AssertUtils.isTrue(response.getStatus() == OK, "微信查询订单信息请求失败");

            // 微信公钥验证签名
            boolean verifySignature = WxPayKit.verifySignature(response, payConfig.getPlatCertPath());
            log.info("验签结果: {}", verifySignature);
            AssertUtils.isTrue(verifySignature, "微信查询订单信息接口验签失败");

            return JSONUtil.toBean(response.getBody(), WxpayQueryOrderRes.class);
        } catch (Exception e) {
            log.error("微信查询订单信息接口调用失败,错误堆栈信息：", e);
            throw new ServiceException("微信查询订单信息接口调用失败");
        }
    }

    /**
     * 微信退款请求
     *
     * @param payConfig 支付配置
     * @param refundOrder 退款单信息
     * @return
     */
    public static WxpayRefundRes refund(PayConfig payConfig, RefundOrder refundOrder) {
        try {
            int refundAmount = refundOrder.getAmount().multiply(new BigDecimal(100)).intValue();
            RefundModel refundModel = new RefundModel()
                    .setOut_refund_no(refundOrder.getRefundNo())
                    .setReason(refundOrder.getDescription())
                    .setOut_trade_no(refundOrder.getOrderNo())
                    .setNotify_url(REFUND_NOTIFY_URL  + "/" + refundOrder.getRefundNo())
                    .setAmount(new RefundAmount().setRefund(refundAmount)
                            .setTotal(refundAmount)
                            .setCurrency(CNY));
            log.info("微信退款参数 {}", JSONUtil.toJsonStr(refundModel));
            IJPayHttpResponse response = WxPayApi.v3(
                    RequestMethod.POST,
                    WxDomainEnum.CHINA.toString(),
                    BasePayApiEnum.REFUND.toString(),
                    payConfig.getMchid(),
                    payConfig.getSerialNum(),
                    null,
                    loadPrivateKeyFromString(payConfig.getPrivateKey()),
                    JSONUtil.toJsonStr(refundModel)
            );
            log.info("微信退款接口响应: {}", response);

            // 微信支付公钥验证签名
            boolean verifySignature = WxPayKit.verifySignature(response, payConfig.getPlatCertPath());
            log.info("验签结果: {}", verifySignature);
            AssertUtils.isTrue(verifySignature, "微信退款接口验签失败");

            return JSONUtil.toBean(response.getBody(), WxpayRefundRes.class);
        } catch (Exception e) {
            log.error("微信退款接口调用失败,错误堆栈信息：", e);
            throw new ServiceException("微信退款接口调用失败");
        }

    }

    /**
     * 微信退款通知解析
     *
     * @param request
     * @param payConfig 支付配置
     */
    public static void parseRefundNotify(HttpServletRequest request, PayConfig payConfig) {
        try {
            // 获取部分请求头
            String body = HttpKit.readData(request);

            String timestamp = request.getHeader(WECHAT_PAY_TIMESTAMP);
            String nonce = request.getHeader(WECHAT_PAY_NONCE);
            String serialNo = request.getHeader(WECHAT_PAY_SERIAL);
            String signature = request.getHeader(WECHAT_PAY_SIGNATURE);
            log.info("退款通知请求头timestamp:{} nonce:{} serialNo:{} signature:{}", timestamp, nonce, serialNo, signature);
            log.info("退款通知密文 {}", body);
            // 验签并获取通知明文
            String plainText = WxPayKit.verifyNotify(serialNo, body, signature, nonce, timestamp, payConfig.getApiKey(), payConfig.getPlatCertPath());
            log.info("微信退款通知明文: {}", plainText);
            AssertUtils.notBlank(plainText, "签名错误");
        } catch (Exception e) {
            log.error("微信退款通知结果解析失败,异常堆栈信息：", e);
            throw new ServiceException("微信退款通知结果解析失败");
        }
    }

    /**
     * 查询微信退款单信息
     *
     * @param payConfig 支付配置信息
     * @param refundOrder 退款单信息
     * @return
     */
    public static WxpayQueryRefundRes queryRefundOrder(PayConfig payConfig, RefundOrder refundOrder) {
        try {
            log.info("微信查询退款单信息请求参数.mchId:{}, orderNo:{}", payConfig.getMchid(), refundOrder.getOrderNo());
            IJPayHttpResponse response = WxPayApi.v3(
                    RequestMethod.GET,
                    WxDomainEnum.CHINA.toString(),
                    String.format(BasePayApiEnum.REFUND_QUERY_BY_OUT_REFUND_NO.toString(), refundOrder.getRefundNo()),
                    payConfig.getMchid(),
                    payConfig.getSerialNum(),
                    null,
                    loadPrivateKeyFromString(payConfig.getPrivateKey()),
                    new HashMap<>()
            );
            log.info("微信查询退款单信息响应 {}", response);
            AssertUtils.isTrue(response.getStatus() == OK, "微信查询退款单信息请求失败");

            // 微信公钥验证签名
            boolean verifySignature = WxPayKit.verifySignature(response, payConfig.getPlatCertPath());
            log.info("验签结果: {}", verifySignature);
            AssertUtils.isTrue(verifySignature, "微信查询退款单信息接口验签失败");

            return JSONUtil.toBean(response.getBody(), WxpayQueryRefundRes.class);
        } catch (Exception e) {
            log.error("微信查询退款单信息接口调用失败,错误堆栈信息：", e);
            throw new ServiceException("微信查询退款单信息接口调用失败");
        }
    }
}
