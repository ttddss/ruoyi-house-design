package com.ruoyi.web.controller.pay;

import cn.hutool.json.JSONUtil;
import com.alipay.api.internal.util.AlipaySignature;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ijpay.alipay.AliPayApi;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.annotation.RepeatSubmit;
import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.AssertUtils;
import com.ruoyi.common.utils.LockUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.order.domain.PayOrder;
import com.ruoyi.order.mapper.PayOrderMapper;
import com.ruoyi.order.service.IPayOrderService;
import com.ruoyi.pay.domain.req.AlipayRefreshOrderReq;
import com.ruoyi.pay.domain.req.AlipayTradePreCreateReq;
import com.ruoyi.pay.domain.req.RefundReq;
import com.ruoyi.pay.domain.req.WxpayRefreshRefundReq;
import com.ruoyi.pay.domain.res.AlipayTradePreCreateRes;
import com.ruoyi.pay.domain.vo.AlipayOrderVO;
import com.ruoyi.pay.domain.vo.AlipayRefundVO;
import com.ruoyi.pay.domain.vo.RefundVO;
import com.ruoyi.pay.service.IAlipayService;
import com.ruoyi.pay.service.PayManager;
import com.ruoyi.pay.strategy.GoodsStrategyManager;
import com.ruoyi.system.domain.PayConfig;
import com.ruoyi.system.enums.GoodsTypeEnum;
import com.ruoyi.system.service.IPayConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 支付宝支付相关接口
 *
 * @author tongdashuai
 * @version 1.0
 * @description
 * @module
 * @date 2025/3/24 17:44
 */
@RestController
@RequestMapping("/alipay")
@Slf4j
public class AlipayController {

    /**
     * 商户订单号
     */
    private static final String OUT_TRADE_NO = "out_trade_no";

    /**
     * 支付宝订单号
     */
    private static final String TRADE_NO = "trade_no";

    /**
     * 支付结果通知返回成功
     */
    private static final String SUCCESS = "success";

    /**
     * 支付结果通知返回失败
     */
    private static final String FAILURE = "failure";

    @Autowired
    private IAlipayService alipayService;

    @Autowired
    private IPayOrderService payOrderService;

    @Autowired
    private PayOrderMapper payOrderMapper;

    @Autowired
    private IPayConfigService payConfigService;

    @Autowired
    private GoodsStrategyManager goodsStrategyManager;

    @Autowired
    private PayManager payManager;

    /**
     * 扫码支付
     * <p>返回二维码url
     * <p>收银员通过收银台或商户后台调用支付宝接口，生成二维码后，展示给用户，由用户扫描二维码完成订单支付。
     * <p>支付宝文档详见：https://opendocs.alipay.com/open/194/106078?ref=api
     */
    @Log(title = "支付宝扫码支付下单", businessType = BusinessType.OTHER)
    @RepeatSubmit
    @PostMapping("/tradePreCreate")
    public AjaxResult<AlipayTradePreCreateRes> tradePreCreate(@Validated @RequestBody AlipayTradePreCreateReq request) {
        request.setUserId(SecurityUtils.getUserId());
        // 生成支付宝扫码付订单
        AlipayTradePreCreateRes res = alipayService.tradePreCreate(request);
        return AjaxResult.success(res);
    }

    /**
     * 刷新订单状态
     * <p>先去查询数据库订单状态，如果不是成功或失败状态，则去支付宝端查询订单状态并将最新的支付宝订单状态更新。
     */
    @PostMapping("/refreshOrder")
    @Log(title = "支付宝刷新订单状态", businessType = BusinessType.OTHER)
    public AjaxResult refreshOrder(@Validated @RequestBody AlipayRefreshOrderReq request) throws Exception {
        payManager.refreshOrder(request.getOrderNo());
        return AjaxResult.success();
    }

    /**
     * 查询订单信息
     * <p>仅查询数据库订单状态，非支付宝最新状态
     */
    @GetMapping("/queryOrder")
    public AjaxResult<AlipayOrderVO> queryOrder(String orderNo) {
        return AjaxResult.success(alipayService.queryOrder(orderNo));
    }

    /**
     * 退款
     * <p>退款可能立即到账，也可能延时到账，如果未立即到账，可以调用接口去查询（退款到银行卡时间由银行结算为准）。
     */
    @PostMapping("/refund")
    @RepeatSubmit
    @Log(title = "支付宝退款", businessType = BusinessType.OTHER)
    public AjaxResult<RefundVO> refund(@Validated @RequestBody RefundReq request) throws Exception {
        // 订单处理加锁
        RefundVO alipayRefundVO = LockUtils.tryLock(CacheConstants.ORDER_LOCK_KEY + request.getPayOrderNo(), "订单处理中，请稍后重试",
                request, (t) -> {
                    return alipayService.refund(request);
                });
        return AjaxResult.success(alipayRefundVO);
    }

    /**
     * 刷新退款信息
     * <p>如果退款单状态是退款中，去支付宝查询最新状态并更新状态
     */
    @PostMapping("/refreshRefund")
    @Log(title = "支付宝刷新退款单状态", businessType = BusinessType.OTHER)
    public AjaxResult refreshRefund(@Validated @RequestBody WxpayRefreshRefundReq request) throws Exception {
        payManager.refreshRefund(request.getRefundNo());
        return AjaxResult.success();
    }

    /**
     * 支付宝支付结果通知.该接口是支付宝支付成功后，支付宝异步推送的支付结果通知
     */
    @PostMapping("/payNotify")
    public String payNotify(HttpServletRequest request) {
        try {
            // 获取支付宝POST过来反馈信息
            Map<String, String> params = AliPayApi.toMap(request);
            log.info("支付宝支付结果通知参数：{}", JSONUtil.toJsonStr(params));

            // 商户订单号
            String orderNo = params.get(OUT_TRADE_NO);
            // 支付宝订单号
            String tradeNo = params.get(TRADE_NO);
            // 查询订单信息
            PayOrder payOrder = payOrderService.getOne(new LambdaQueryWrapper<PayOrder>().eq(PayOrder::getOrderNo, orderNo));
            AssertUtils.notNull(payOrder, "订单信息不存在");

            // 查询支付宝配置
            PayConfig payConfig = payConfigService.selectPayConfigInfo(payOrder.getChannel(), payOrder.getSubChannel());
            AssertUtils.notNull(payConfig, "支付宝支付配置信息不存在");

            // 验签
            boolean verifyResult = AlipaySignature.rsaCheckV1(params, payConfig.getPublicKey(), Constants.UTF8, Constants.RSA2);
            if (verifyResult) {
                // 订单处理加锁
                LockUtils.tryLock(CacheConstants.ORDER_LOCK_KEY + orderNo, "订单处理中，请稍后重试",
                        orderNo, (t) -> {
                    // 更新支付宝订单号
                    payOrderMapper.updateOuterTradeNo(tradeNo, orderNo);
                    // 支付成功处理
                    goodsStrategyManager.paySuccess(GoodsTypeEnum.getEnumByCode(payOrder.getGoodsType()), t);
                });

                return SUCCESS;
            } else {
                log.error("支付宝支付通知验签失败");
                return FAILURE;
            }
        } catch (Exception e) {
            log.error("支付宝支付通知处理异常，异常堆栈信息：", e);
            return FAILURE;
        }
    }

}
