package com.ruoyi.pay.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.utils.AssertUtils;
import com.ruoyi.common.utils.LockUtils;
import com.ruoyi.order.domain.PayOrder;
import com.ruoyi.order.domain.RefundOrder;
import com.ruoyi.order.mapper.PayOrderMapper;
import com.ruoyi.order.mapper.RefundOrderMapper;
import com.ruoyi.pay.domain.req.RefundReq;
import com.ruoyi.pay.domain.vo.RefundVO;
import com.ruoyi.system.enums.PayChannelEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 *
 * @author tongdashuai
 * @version 1.0
 * @description
 * @module
 * @date 2025/3/27 16:49
 */
@Component
@Slf4j
public class PayManager {

    private static final Map<Integer, IPayService> PAY_SERVICE_MAP = new HashMap<>();

    @Autowired
    private RefundOrderMapper refundOrderMapper;

    @Autowired
    private PayOrderMapper payOrderMapper;

    public PayManager(List<IPayService> payServices) {
        for (IPayService payService : payServices) {
            PAY_SERVICE_MAP.put(payService.getPayChannel().getCode(), payService);
        }
    }

    /**
     * 刷新订单信息
     *
     * @param orderNo 订单号
     */
    public void refreshOrder(String orderNo) throws Exception {
        PayOrder payOrder = payOrderMapper.selectOne(new LambdaQueryWrapper<PayOrder>().eq(PayOrder::getOrderNo, orderNo));
        AssertUtils.notNull(payOrder, "订单不存在");

        IPayService payService = PAY_SERVICE_MAP.get(payOrder.getChannel());
        AssertUtils.notNull(payService, "不支持的支付渠道");
        // 订单处理加锁
        LockUtils.tryLock(CacheConstants.ORDER_LOCK_KEY + orderNo, "订单处理中，请稍后重试",
                orderNo, (t) -> {
                    payService.refreshOrder(t);
                });
    }



    /**
     * 刷新退款单状态
     *
     * @param refundNo 退款单号
     */
    public void refreshRefund(String refundNo) throws Exception {
        RefundOrder refundOrder = refundOrderMapper.selectOne(new LambdaQueryWrapper<RefundOrder>().eq(RefundOrder::getRefundNo, refundNo));
        AssertUtils.notNull(refundOrder, "退款单不存在");

        PayOrder payOrder = payOrderMapper.selectOne(new LambdaQueryWrapper<PayOrder>().eq(PayOrder::getOrderNo, refundOrder.getOrderNo()));
        AssertUtils.notNull(payOrder, "订单不存在");

        IPayService payService = PAY_SERVICE_MAP.get(payOrder.getChannel());
        AssertUtils.notNull(payService, "不支持的支付渠道");

        // 订单处理加锁
        LockUtils.tryLock(CacheConstants.ORDER_LOCK_KEY + refundOrder.getOrderNo(), "订单处理中，请稍后重试",
                refundNo, (t) -> {
                    payService.refreshOrder(t);
                });
    }

    /**
     * 退款
     *
     * @param request
     * @return
     */
    public RefundVO refund(RefundReq request) throws Exception {
        PayOrder payOrder = payOrderMapper.selectOne(new LambdaQueryWrapper<PayOrder>().eq(PayOrder::getOrderNo, request.getPayOrderNo()));
        AssertUtils.notNull(payOrder, "订单不存在");

        IPayService payService = PAY_SERVICE_MAP.get(payOrder.getChannel());
        AssertUtils.notNull(payService, "不支持的支付渠道");
        // 订单处理加锁
        return LockUtils.tryLock(CacheConstants.ORDER_LOCK_KEY + request.getPayOrderNo(), "订单处理中，请稍后重试",
                request, (t) -> {
                    return payService.refund(t);
                });
    }


}
