package com.ruoyi.pay.service.impl;

import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.domain.AlipayTradeRefundModel;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruoyi.common.config.RabbitMqConfig;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.AssertUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.order.domain.PayOrder;
import com.ruoyi.order.domain.RefundOrder;
import com.ruoyi.order.enums.RefundStatusEnum;
import com.ruoyi.order.mapper.PayOrderMapper;
import com.ruoyi.order.mapper.RefundOrderMapper;
import com.ruoyi.pay.domain.req.RefundReq;
import com.ruoyi.pay.domain.vo.RefundVO;
import com.ruoyi.pay.service.IPayService;
import com.ruoyi.pay.strategy.GoodsStrategyManager;
import com.ruoyi.system.enums.GoodsTypeEnum;
import com.ruoyi.system.enums.MsgBizTypeEnum;
import com.ruoyi.system.enums.PayChannelEnum;
import com.ruoyi.system.util.AlipayUtils;
import com.ruoyi.system.util.MqBizUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author tds
 * @version 1.0.0
 * @Company
 * @ClassName FreePayServiceImpl
 * @Description
 * @Date 2025-03-29 18:37
 */
@Service
@Slf4j
public class FreePayServiceImpl implements IPayService {

    @Autowired
    private PayOrderMapper payOrderMapper;

    @Autowired
    private RefundOrderMapper refundOrderMapper;

    @Autowired
    private GoodsStrategyManager goodsStrategyManager;

    @Override
    public void refreshOrder(String orderNo) {

    }

    @Override
    public void refreshRefund(String refundNo) {

    }

    @Override
    public RefundVO refund(RefundReq request) {
        RefundVO refundVO = new RefundVO();
        refundVO.setStatus(RefundStatusEnum.REFUNDING.getCode());

        PayOrder payOrder = payOrderMapper.selectOne(new LambdaQueryWrapper<PayOrder>()
                .eq(PayOrder::getOrderNo, request.getPayOrderNo()));
        AssertUtils.notNull(payOrder, "订单不能为空");

        // 退款单号
        String refundNo = IdUtil.getSnowflakeNextIdStr();
        // 生成退款单信息
        RefundOrder refundOrder = new RefundOrder();
        refundOrder.setRefundNo(refundNo);
        refundOrder.setAmount(payOrder.getActualAmount());
        refundOrder.setDescription(request.getDescription());
        refundOrder.setUserId(payOrder.getUserId());
        refundOrder.setOrderNo(payOrder.getOrderNo());
        refundOrder.setStatus(RefundStatusEnum.REFUNDING.getCode());
        refundOrder.setCreateBy(SecurityUtils.getUsername(false));
        refundOrderMapper.insert(refundOrder);

        // 退款成功处理
        goodsStrategyManager.refundSuccess(GoodsTypeEnum.getEnumByCode(payOrder.getGoodsType()), refundNo);
        refundVO.setStatus(RefundStatusEnum.SUCCESS.getCode());

        return refundVO;
    }

    @Override
    public PayChannelEnum getPayChannel() {
        return PayChannelEnum.OTHER;
    }
}
