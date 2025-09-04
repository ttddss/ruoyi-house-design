package com.ruoyi.web.controller.pay;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.annotation.RepeatSubmit;
import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.AssertUtils;
import com.ruoyi.common.utils.LockUtils;
import com.ruoyi.order.domain.RefundOrder;
import com.ruoyi.order.mapper.RefundOrderMapper;
import com.ruoyi.pay.domain.WxResult;
import com.ruoyi.pay.domain.req.*;
import com.ruoyi.pay.domain.res.WxpayJsapiPayRes;
import com.ruoyi.pay.domain.vo.RefundVO;
import com.ruoyi.pay.domain.vo.WxpayOrderVO;
import com.ruoyi.pay.domain.vo.WxpayQueryAuthVO;
import com.ruoyi.pay.domain.vo.WxpayRefundVO;
import com.ruoyi.pay.service.IWxpayService;
import com.ruoyi.pay.service.PayManager;
import com.ruoyi.system.enums.PayChannelEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 微信支付相关接口
 *
 * @author tongdashuai
 * @version 1.0
 * @description
 * @module
 * @date 2025/3/24 17:44
 */
@RestController
@RequestMapping("/wxpay")
@Slf4j
public class WxpayController {



    @Autowired
    private IWxpayService wxpayService;

    @Autowired
    private RefundOrderMapper refundOrderMapper;

    @Autowired
    private PayManager payManager;



    /**
     * jsapi下单
     * <p>jsapi支付或小程序支付会用到这个接口
     * <p>服务商文档详见：https://pay.weixin.qq.com/wiki/doc/apiv3_partner/open/pay/chapter1_1_1.shtml
     * <p>普通商户文档详见：https://pay.weixin.qq.com/wiki/doc/apiv3/apis/chapter3_1_1.shtml
     */
    @PostMapping("/jsapiPay")
    @Log(title = "微信jsapi下单", businessType = BusinessType.OTHER)
    @RepeatSubmit
    public AjaxResult<WxpayJsapiPayRes> jsapiPay(@Validated @RequestBody WxpayJsapiPayReq request) {
        WxpayJsapiPayRes res = wxpayService.jsapiPay(request);
        return AjaxResult.success(res);
    }

    /**
     * 查询订单信息
     * <p>仅查询数据库订单状态，非微信订单最新状态
     */
    @GetMapping("/queryOrder")
    public AjaxResult<WxpayOrderVO> queryOrder(String orderNo) {
        return AjaxResult.success(wxpayService.queryOrder(orderNo));
    }

    /**
     * 刷新订单状态
     * <p>先去查询数据库订单状态，如果不是成功或失败状态，则去微信端查询订单状态并将最新的微信订单状态更新。
     */
    @PostMapping("/refreshOrder")
    public AjaxResult refreshOrder(@Validated @RequestBody WxpayRefreshOrderReq request) throws Exception {
        payManager.refreshOrder(request.getOrderNo());
        return AjaxResult.success();
    }

    /**
     * 退款
     * <p>服务商退款文档详见：https://pay.weixin.qq.com/wiki/doc/apiv3_partner/apis/chapter4_1_9.shtml
     * <p>普通商户退款文档详见：https://pay.weixin.qq.com/wiki/doc/apiv3/apis/chapter3_1_9.shtml
     */
    @PostMapping("/refund")
    @Log(title = "微信退款", businessType = BusinessType.OTHER)
    public AjaxResult<RefundVO> refund(@Validated @RequestBody RefundReq request) throws Exception {
        // 订单处理加锁
        RefundVO refundVO = LockUtils.tryLock(CacheConstants.ORDER_LOCK_KEY + request.getPayOrderNo(), "订单处理中，请稍后重试",
                request, (t) -> {
                    return wxpayService.refund(request);
                });
        return AjaxResult.success(refundVO);
    }

    /**
     * 刷新退款信息
     * <p>如果退款单状态不是成功或失败，去微信查询最新状态并更新状态
     */
    @PostMapping("/refreshRefund")
    @Log(title = "微信刷新退款单状态", businessType = BusinessType.OTHER)
    public AjaxResult refreshRefund(@Validated @RequestBody WxpayRefreshRefundReq request) throws Exception {
        payManager.refreshRefund(request.getRefundNo());
        return AjaxResult.success();
    }

    /**
     * 微信支付结果通知.该接口是微信支付成功后，异步推送的支付结果
     */
    @PostMapping("/payNotify/{orderNo}")
    public WxResult payNotify(HttpServletRequest request, HttpServletResponse response, @PathVariable("orderNo") String orderNo) {
        try {
            // 订单处理加锁
            LockUtils.tryLock(CacheConstants.ORDER_LOCK_KEY + orderNo, "订单处理中，请稍后重试",
                    orderNo, (t) -> {
                        wxpayService.payNotify(request, t);
                    });
        } catch (Exception e) {
            log.error("微信普通商户支付结果通知处理失败：", e);
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return WxResult.fail(e.getMessage());
        }
        return WxResult.success();
    }

    /**
     * 微信退款结果通知.该接口是微信退款成功后，异步推送的退款结果
     */
    @PostMapping("/refundNotify/{refundNo}")
    public WxResult refundNotify(HttpServletRequest request, HttpServletResponse response, @PathVariable("refundNo") String refundNo) {
        try {
            // 查询退款单信息
            RefundOrder refundOrder = refundOrderMapper.selectOne(new LambdaQueryWrapper<RefundOrder>().eq(RefundOrder::getRefundNo, refundNo));
            AssertUtils.notNull(refundOrder, "退款单不存在");

            // 订单处理加锁
            LockUtils.tryLock(CacheConstants.ORDER_LOCK_KEY + refundOrder.getOrderNo(), "订单处理中，请稍后重试",
                    refundNo, (t) -> {
                        wxpayService.refundNotify(request, t);
                    });
        } catch (Exception e) {
            log.error("微信商户退款结果通知处理失败：", e);
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return WxResult.fail(e.getMessage());
        }
        return WxResult.success();
    }

    /**
     * 获取授权信息
     */
    @PostMapping("/getAuthInfo")
    public AjaxResult<WxpayQueryAuthVO> getAuthInfo(@Validated @RequestBody WxpayQueryAuthReq request) {
        WxpayQueryAuthVO res = wxpayService.getAuthInfo(request);
        return AjaxResult.success(res);
    }

}
