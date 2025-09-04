package com.ruoyi.pay.service.impl;

import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.domain.AlipayTradePrecreateModel;
import com.alipay.api.domain.AlipayTradeRefundModel;
import com.alipay.api.response.AlipayTradeFastpayRefundQueryResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruoyi.common.config.RabbitMqConfig;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.AssertUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.order.domain.PayOrder;
import com.ruoyi.order.domain.RefundOrder;
import com.ruoyi.order.enums.PayStatusEnum;
import com.ruoyi.order.enums.RefundStatusEnum;
import com.ruoyi.order.mapper.PayOrderMapper;
import com.ruoyi.order.mapper.RefundOrderMapper;
import com.ruoyi.order.service.IPayOrderService;
import com.ruoyi.pay.domain.dto.CalculateGoodsDTO;
import com.ruoyi.pay.domain.dto.CalculateGoodsResDTO;
import com.ruoyi.pay.domain.req.AlipayRefundReq;
import com.ruoyi.pay.domain.req.AlipayTradePreCreateReq;
import com.ruoyi.pay.domain.req.RefundReq;
import com.ruoyi.pay.domain.res.AlipayTradePreCreateRes;
import com.ruoyi.pay.domain.res.WxpayQueryRefundRes;
import com.ruoyi.pay.domain.vo.AlipayOrderVO;
import com.ruoyi.pay.domain.vo.AlipayRefundVO;
import com.ruoyi.pay.domain.vo.RefundVO;
import com.ruoyi.pay.enums.AlipaySubCodeEnum;
import com.ruoyi.pay.enums.AlipayTradeStatusEnum;
import com.ruoyi.pay.enums.WxpayRefundStatusEnum;
import com.ruoyi.pay.service.IAlipayService;
import com.ruoyi.pay.strategy.GoodsStrategyManager;
import com.ruoyi.system.domain.PayConfig;
import com.ruoyi.system.enums.GoodsTypeEnum;
import com.ruoyi.system.enums.MsgBizTypeEnum;
import com.ruoyi.system.enums.PayChannelEnum;
import com.ruoyi.system.enums.PaySubChannelEnum;
import com.ruoyi.system.service.IPayConfigService;
import com.ruoyi.system.util.AlipayUtils;
import com.ruoyi.system.util.MqBizUtils;
import com.ruoyi.system.util.WxpayUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 *
 * @author tongdashuai
 * @version 1.0
 * @description
 * @module
 * @date 2025/3/24 14:13
 */
@Service
@Slf4j
public class AlipayServiceImpl implements IAlipayService {

    @Value("${payConfig.autoCloseOrderTime:300}")
    private Integer autoCloseOrderTime;

    private static final String REFUND_SUCCESS = "REFUND_SUCCESS";

    /**
     * 退款返回的fund_change字段值“Y”,表示退款成功，本次退款发生了资金变化，
     */
    private static final String IS_FUND_CHANGE = "Y";

    @Autowired
    private IPayConfigService payConfigService;

    @Autowired
    private IPayOrderService payOrderService;

    @Autowired
    private PayOrderMapper payOrderMapper;

    @Autowired
    private RefundOrderMapper refundOrderMapper;


    @Autowired
    private GoodsStrategyManager goodsStrategyManager;


    @Transactional(rollbackFor = Exception.class)
    @Override
    public AlipayTradePreCreateRes tradePreCreate(AlipayTradePreCreateReq request) {
        // 初始化支付配置
        PayConfig payConfig = initPayConfig(PayChannelEnum.ALIPAY.getCode(), PaySubChannelEnum.ALIPAY_SCAN_QR.getCode());

        // 生成订单
        PayOrder payOrder = generateOrder(request, payConfig);

        // 组装统一收单线下交易预创建请求参数
        AlipayTradePrecreateModel model = new AlipayTradePrecreateModel();
        model.setSubject(payOrder.getSubject());
        model.setTotalAmount(payOrder.getActualAmount().toString());
        // 订单相对超时时间。 从预下单请求时间开始计算。该笔订单允许的最晚付款时间，逾期将关闭交易。取值范围：1m～15d。m-分钟，h-小时，d-天，1c-当天
        model.setTimeoutExpress("5m");
        model.setOutTradeNo(payOrder.getOrderNo());

        // 调用扫码付统一收单线下交易预创建接口
        String qrCode = AlipayUtils.tradePreCreatePay(model);

        // 订单放入mq中。用于后续主动查询并刷新支付宝订单状态
        MqBizUtils.sendDelayMqMsg(RabbitMqConfig.QUEUE_QUERY_ORDER_NAME, payOrder.getOrderNo(), MsgBizTypeEnum.QUERY_ORDER_STATUS, 60);

        // 订单放入mq中。用于后续自动关闭支付宝订单
        MqBizUtils.sendDelayMqMsg(RabbitMqConfig.QUEUE_AUTO_CLOSE_ORDER_NAME, payOrder.getOrderNo(), MsgBizTypeEnum.AUTO_CLOSE, autoCloseOrderTime);

        return AlipayTradePreCreateRes.builder().qrCode(qrCode).orderNo(payOrder.getOrderNo()).build();
    }

    /**
     * 初始化支付配置
     *
     * @param channel 支付渠道
     * @param subChannel 子支付渠道
     * @return
     */
    private PayConfig initPayConfig(int channel, int subChannel) {
        // 查询支付配置信息
        PayConfig payConfig = payConfigService.selectPayConfigInfo(channel, subChannel);
        AssertUtils.notNull(payConfig, "支付宝支付配置为空");
        // 初始化支付宝支付配置
        AlipayUtils.initApiConfig(payConfig);
        return payConfig;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void refreshOrder(String orderNo) {
        // 查询订单信息
        PayOrder payOrder = payOrderMapper.selectOne(new LambdaQueryWrapper<PayOrder>().eq(PayOrder::getOrderNo, orderNo));
        AssertUtils.notNull(payOrder, "订单信息不存在");
        if (PayStatusEnum.PAYING.getCode() != payOrder.getStatus()) {
            log.info("订单状态不需要刷新");
            return;
        }

        // 初始化支付配置
        initPayConfig(payOrder.getChannel(), payOrder.getSubChannel());

        // 去支付宝查询订单状态
        AlipayTradeQueryResponse response = AlipayUtils.queryOrder(orderNo);

        // 支付成功
        if (parseQuerySuccess(response)) {
            // 更新支付宝订单号
            payOrderMapper.updateOuterTradeNo(response.getTradeNo(), payOrder.getOrderNo());
            // 支付成功处理
            goodsStrategyManager.paySuccess(GoodsTypeEnum.getEnumByCode(payOrder.getGoodsType()), payOrder.getOrderNo());
            return;
        }

        // 支付失败
        if (parseQueryFail(response, payOrder)) {
            // 更新订单状态为支付失败
            payOrderMapper.updateFail(payOrder.getOrderNo(), response.getSubCode(), response.getSubMsg());
            return;
        }

        // 处理中更新错误码和错误信息
        payOrderMapper.updateCodeAndMsg(payOrder.getOrderNo(), response.getSubCode(), response.getSubMsg());
    }

    /**
     * 刷新退款单状态
     *
     * @param refundNo 退款单号
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void refreshRefund(String refundNo) {
        RefundOrder refundOrder = refundOrderMapper.selectOne(new LambdaQueryWrapper<RefundOrder>().eq(RefundOrder::getRefundNo, refundNo));
        AssertUtils.notNull(refundOrder, "退款单信息不存在");
        if (RefundStatusEnum.REFUNDING.getCode() != refundOrder.getStatus()) {
            log.info("退款单状态不需要刷新");
            return;
        }

        // 查询订单信息
        PayOrder payOrder = payOrderMapper.selectOne(new LambdaQueryWrapper<PayOrder>().eq(PayOrder::getOrderNo, refundOrder.getOrderNo()));
        AssertUtils.notNull(payOrder, "订单信息不存在");
        AssertUtils.isTrue(PayStatusEnum.PAYING.getCode() == payOrder.getStatus(), "订单状态不需要刷新");

        // 查询支付配置信息
        PayConfig payConfig = payConfigService.selectPayConfigInfo(payOrder.getChannel(), payOrder.getSubChannel());
        AssertUtils.notNull(payConfig, "支付配置信息为空");

        // 去支付宝查询退款单信息
        AlipayTradeFastpayRefundQueryResponse response = AlipayUtils.queryRefundOrder(refundOrder);

        // 退款成功
        if (REFUND_SUCCESS.equals(response.getRefundStatus())) {
            // 退款成功处理
            goodsStrategyManager.refundSuccess(GoodsTypeEnum.getEnumByCode(payOrder.getGoodsType()), refundNo);
        }
    }

    @Override
    public AlipayOrderVO queryOrder(String orderNo) {
        PayOrder payOrder = payOrderMapper.selectOne(new LambdaQueryWrapper<PayOrder>().eq(PayOrder::getOrderNo, orderNo));
        AlipayOrderVO orderVO = new AlipayOrderVO();
        orderVO.setStatus(payOrder.getStatus());
        return orderVO;
    }

    @Override
    public RefundVO refund(RefundReq request) {
        RefundVO refundVO = new RefundVO();
        refundVO.setStatus(RefundStatusEnum.REFUNDING.getCode());

        PayOrder payOrder = payOrderMapper.selectOne(new LambdaQueryWrapper<PayOrder>()
                .eq(PayOrder::getOrderNo, request.getPayOrderNo()));
        AssertUtils.notNull(payOrder, "订单不能为空");

        // 初始化支付配置
        initPayConfig(payOrder.getChannel(), payOrder.getSubChannel());

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

        // 退款单放入mq中。用于后续主动查询并刷新支付宝退款单状态
        MqBizUtils.sendDelayMqMsg(RabbitMqConfig.QUEUE_QUERY_REFUND_NAME, refundNo, MsgBizTypeEnum.QUERY_REFUND_STATUS, 60);

        // 调用退款接口
        AlipayTradeRefundModel model = new AlipayTradeRefundModel();
        model.setOutTradeNo(request.getPayOrderNo());
        model.setOutRequestNo(refundNo);
        model.setRefundAmount(payOrder.getActualAmount().toString());
        model.setRefundReason(request.getDescription());
        AlipayTradeRefundResponse response = AlipayUtils.refund(model);

        // 更新退款单失败状态
        if(!response.isSuccess()){
            log.error("调用支付宝统一收单交易退款接口返回失败,支付宝返回信息：{}", JSONObject.toJSONString(response));
            refundOrderMapper.updateFail(refundNo, response.getSubCode(), response.getSubMsg());
            throw new ServiceException("退款失败" +  response.getSubCode() + response.getSubMsg());
        }

        // fundChange，资金是否发生变化。退款到银行卡实际退款成功由银行结算时间为准，如果时退还到余额，那么立即到账
        // 退款成功判断说明：接口返回fund_change=Y为退款成功，fund_change=N或无此字段值返回时需通过退款查询接口进一步确认退款状态。
        // 详见退款成功判断指导。注意，接口中code=10000，仅代表本次退款请求成功，不代表退款成功。
        if (IS_FUND_CHANGE.equals(response.getFundChange())) {
            // 更新支付宝退款流水号
            refundOrderMapper.updateOuterTradeNo(response.getTradeNo(), refundNo);
            // 退款成功处理
            goodsStrategyManager.refundSuccess(GoodsTypeEnum.getEnumByCode(payOrder.getGoodsType()), refundNo);
            refundVO.setStatus(RefundStatusEnum.SUCCESS.getCode());
        }

        return refundVO;
    }

    /**
     * 订单查询返回成功判断
     *
     * @param tradeQueryResponse
     * @return
     */
    private boolean parseQuerySuccess(AlipayTradeQueryResponse tradeQueryResponse) {
        if (!tradeQueryResponse.isSuccess()) {
            return false;
        }
        if (AlipayTradeStatusEnum.TRADE_FINISHED.getCode().equals(tradeQueryResponse.getTradeStatus())) {
            return true;
        }
        if (AlipayTradeStatusEnum.TRADE_SUCCESS.getCode().equals(tradeQueryResponse.getTradeStatus())) {
            return true;
        }

        return false;
    }

    /**
     * 订单查询返回失败判断
     * @param tradeQueryResponse 支付宝查询返回结果
     * @param payOrder 订单信息
     * @return
     */
    private boolean parseQueryFail(AlipayTradeQueryResponse tradeQueryResponse, PayOrder payOrder) {
        if (tradeQueryResponse.isSuccess()) {
            return false;
        }
        // 扫码付，预创建的订单是不存在的，返回支付中状态（业务错误码：ACQ.TRADE_NOT_EXIST，查询的交易不存在）.否则当做失败
        if (PaySubChannelEnum.ALIPAY_SCAN_QR.getCode() != payOrder.getSubChannel()
                && AlipaySubCodeEnum.ACQ_TRADE_NOT_EXIST.getCode().equals(tradeQueryResponse.getSubCode())) {
            return true;
        }
        return false;
    }

    /**
     * 生成订单信息
     *
     * @param request 下单请求信息
     * @param payConfig 支付配置信息
     * @return
     */
    private PayOrder generateOrder(AlipayTradePreCreateReq request, PayConfig payConfig) {
        // 雪花算法生成订单号
        String orderNo = IdUtil.getSnowflakeNextIdStr();

        // 根据商品类型计算对应的金额
        GoodsTypeEnum goodsTypeEnum = GoodsTypeEnum.getEnumByCode(request.getGoodsType());
        AssertUtils.notNull(goodsTypeEnum, "不支持的商品类型" + request.getGoodsType());

        CalculateGoodsDTO calculateGoods = CalculateGoodsDTO.builder()
                .goodsNum(request.getGoodsNum())
                .vipId(request.getVipId())
                .blueprintsId(request.getBlueprintsId())
                .userId(request.getUserId())
                .build();
        CalculateGoodsResDTO goodsRes = goodsStrategyManager.calculate(goodsTypeEnum, calculateGoods);

        // 生成订单信息
        PayOrder payOrder = new PayOrder();
        payOrder.setCreateBy(SecurityUtils.getUsername(false));
        payOrder.setUserId(request.getUserId());
        payOrder.setOrderNo(orderNo);
        payOrder.setAmount(goodsRes.getAmount());
        payOrder.setSubject(goodsRes.getSubject());
        payOrder.setGoodsNum(request.getGoodsNum());
        payOrder.setGoodsType(request.getGoodsType());
        payOrder.setBlueprintsId(request.getBlueprintsId());
        payOrder.setVipId(request.getVipId());
        payOrder.setChannel(PayChannelEnum.ALIPAY.getCode());
        payOrder.setSubChannel(PaySubChannelEnum.ALIPAY_SCAN_QR.getCode());
        payOrder.setAppid(payConfig.getAppid());
        payOrder.setMchid(payConfig.getMchid());
        payOrder.setStatus(PayStatusEnum.PAYING.getCode());
        payOrder.setDiscount(goodsRes.getDiscount());
        payOrder.setActualAmount(goodsRes.getActualAmount());
        payOrder.setDiscountAmount(goodsRes.getDiscountAmount());
        payOrderService.save(payOrder);
        return payOrder;
    }
}
