package com.ruoyi.pay.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.domain.AlipayTradeRefundModel;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ijpay.core.IJPayHttpResponse;
import com.ijpay.wxpay.WxPayApi;
import com.ijpay.wxpay.model.v3.RefundAmount;
import com.ijpay.wxpay.model.v3.RefundModel;
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
import com.ruoyi.pay.domain.dto.CalculateGoodsDTO;
import com.ruoyi.pay.domain.dto.CalculateGoodsResDTO;
import com.ruoyi.pay.domain.req.RefundReq;
import com.ruoyi.pay.domain.req.WxpayJsapiPayReq;
import com.ruoyi.pay.domain.req.WxpayQueryAuthReq;
import com.ruoyi.pay.domain.req.WxpayRefundReq;
import com.ruoyi.pay.domain.res.WxpayJsapiPayRes;
import com.ruoyi.pay.domain.res.WxpayQueryOrderRes;
import com.ruoyi.pay.domain.res.WxpayQueryRefundRes;
import com.ruoyi.pay.domain.res.WxpayRefundRes;
import com.ruoyi.pay.domain.vo.*;
import com.ruoyi.pay.enums.WxpayRefundStatusEnum;
import com.ruoyi.pay.enums.WxpayTradeStateEnum;
import com.ruoyi.pay.service.IWxpayService;
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

import javax.servlet.http.HttpServletRequest;

/**
 *
 *
 * @author tongdashuai
 * @version 1.0
 * @description
 * @module
 * @date 2025/3/26 15:36
 */
@Slf4j
@Service
public class WxpayServiceImpl implements IWxpayService {

    @Value("${payConfig.autoCloseOrderTime:300}")
    private Integer autoCloseOrderTime;

    @Autowired
    private IPayConfigService payConfigService;

    @Autowired
    private GoodsStrategyManager goodsStrategyManager;

    @Autowired
    private PayOrderMapper payOrderMapper;

    @Autowired
    private RefundOrderMapper refundOrderMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public WxpayJsapiPayRes jsapiPay(WxpayJsapiPayReq request) {
        // 查询支付配置信息
        PayConfig payConfig = payConfigService.selectPayConfigInfo(PayChannelEnum.ALIPAY.getCode(), PaySubChannelEnum.WX_JSAPI.getCode());
        AssertUtils.notNull(payConfig, "支付宝支付配置为空");

        // 生成订单
        PayOrder payOrder = generateOrder(request, payConfig);

        // 订单放入mq中。用于后续主动查询并刷新微信订单状态
        MqBizUtils.sendDelayMqMsg(RabbitMqConfig.QUEUE_QUERY_ORDER_NAME, payOrder.getOrderNo(), MsgBizTypeEnum.QUERY_ORDER_STATUS, 60);

        // 订单放入mq中。用于后续自动关闭微信订单
        MqBizUtils.sendDelayMqMsg(RabbitMqConfig.QUEUE_AUTO_CLOSE_ORDER_NAME, payOrder.getOrderNo(), MsgBizTypeEnum.AUTO_CLOSE, autoCloseOrderTime);

        // 调用jsapi支付下单接口
        return WxpayUtils.jsapiPay(request.getOpenid(), payConfig, payOrder);
    }

    @Override
    public void payNotify(HttpServletRequest request, String orderNo) {
        PayOrder payOrder = payOrderMapper.selectOne(new LambdaQueryWrapper<PayOrder>().eq(PayOrder::getOrderNo, orderNo));
        AssertUtils.notNull(payOrder, "订单不能为空");
        AssertUtils.isTrue(PayStatusEnum.PAYING.getCode() == payOrder.getStatus(), "订单状态异常");

        // 查询支付配置信息
        PayConfig payConfig = payConfigService.selectPayConfigInfo(payOrder.getChannel(), payOrder.getSubChannel());
        AssertUtils.notNull(payConfig, "支付配置信息为空");

        // 解析通知结果信息（包括验签）
        WxpayUtils.parseNotify(request, payConfig);

        // 支付成功处理
        goodsStrategyManager.paySuccess(GoodsTypeEnum.getEnumByCode(payOrder.getGoodsType()), payOrder.getOrderNo());
    }

    @Override
    public WxpayOrderVO queryOrder(String orderNo) {
        PayOrder payOrder = payOrderMapper.selectOne(new LambdaQueryWrapper<PayOrder>().eq(PayOrder::getOrderNo, orderNo));
        WxpayOrderVO orderVO = new WxpayOrderVO();
        orderVO.setStatus(payOrder.getStatus());
        return orderVO;
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

        // 查询支付配置信息
        PayConfig payConfig = payConfigService.selectPayConfigInfo(payOrder.getChannel(), payOrder.getSubChannel());
        AssertUtils.notNull(payConfig, "支付配置信息为空");

        // 去微信查询订单信息
        WxpayQueryOrderRes queryOrderRes = WxpayUtils.queryOrder(payConfig, payOrder);

        // 支付成功
        if (WxpayTradeStateEnum.SUCCESS.getCode().equals(queryOrderRes.getTrade_state())) {
            // 更新微信订单号
            payOrderMapper.updateOuterTradeNo(queryOrderRes.getTransaction_id(), payOrder.getOrderNo());
            // 支付成功处理
            goodsStrategyManager.paySuccess(GoodsTypeEnum.getEnumByCode(payOrder.getGoodsType()), payOrder.getOrderNo());
        }
    }

    @Override
    public RefundVO refund(RefundReq request) {
        RefundVO refundVO = new RefundVO();
        refundVO.setStatus(RefundStatusEnum.REFUNDING.getCode());

        PayOrder payOrder = payOrderMapper.selectOne(new LambdaQueryWrapper<PayOrder>()
                .eq(PayOrder::getOrderNo, request.getPayOrderNo()));
        AssertUtils.notNull(payOrder, "订单不能为空");

        // 查询支付配置信息
        PayConfig payConfig = payConfigService.selectPayConfigInfo(payOrder.getChannel(), payOrder.getSubChannel());
        AssertUtils.notNull(payConfig, "支付配置信息为空");

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

        // 退款单放入mq中。用于后续主动查询并刷新微信退款单状态
        MqBizUtils.sendDelayMqMsg(RabbitMqConfig.QUEUE_QUERY_REFUND_NAME, refundNo, MsgBizTypeEnum.QUERY_REFUND_STATUS, 60);

        // 调用退款接口
        WxpayRefundRes refundRes = WxpayUtils.refund(payConfig, refundOrder);

        // 退款成功
        if (WxpayRefundStatusEnum.SUCCESS.getCode().equals(refundRes.getStatus())) {
            // 更新微信退款流水号
            refundOrderMapper.updateOuterTradeNo(refundRes.getRefund_id(), refundNo);
            // 退款成功处理
            goodsStrategyManager.refundSuccess(GoodsTypeEnum.getEnumByCode(payOrder.getGoodsType()), refundNo);
            refundVO.setStatus(RefundStatusEnum.SUCCESS.getCode());
        }

        // 更新退款单失败状态
        if(WxpayRefundStatusEnum.ABNORMAL.getCode().equals(refundRes.getStatus())
                || WxpayRefundStatusEnum.CLOSED.getCode().equals(refundRes.getStatus())){
            refundOrderMapper.updateFail(refundNo, refundRes.getStatus(), "退款失败");
            throw new ServiceException("退款失败");
        }

        return refundVO;
    }

    @Override
    public void refundNotify(HttpServletRequest request, String refundNo) {
        // 查询退款单信息
        RefundOrder refundOrder = refundOrderMapper.selectOne(new LambdaQueryWrapper<RefundOrder>().eq(RefundOrder::getRefundNo, refundNo));
        AssertUtils.notNull(refundOrder, "退款单信息为空");
        AssertUtils.isTrue(RefundStatusEnum.REFUNDING.getCode() == refundOrder.getStatus(), "订单状态异常");

        // 查询订单信息
        PayOrder payOrder = payOrderMapper.selectOne(new LambdaQueryWrapper<PayOrder>().eq(PayOrder::getOrderNo, refundOrder.getOrderNo()));
        AssertUtils.notNull(payOrder, "订单信息为空");
        AssertUtils.isTrue(PayStatusEnum.SUCCESS.getCode() == refundOrder.getStatus(), "订单状态异常");

        // 查询支付配置信息
        PayConfig payConfig = payConfigService.selectPayConfigInfo(payOrder.getChannel(), payOrder.getSubChannel());
        AssertUtils.notNull(payConfig, "支付配置信息为空");

        // 解析通知结果信息（包括验签）
        WxpayUtils.parseRefundNotify(request, payConfig);

        // 支付成功处理
        goodsStrategyManager.refundSuccess(GoodsTypeEnum.getEnumByCode(payOrder.getGoodsType()), refundNo);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void refreshRefund(String refundNo) {
        RefundOrder refundOrder = refundOrderMapper.selectOne(new LambdaQueryWrapper<RefundOrder>().eq(RefundOrder::getRefundNo, refundNo));
        AssertUtils.notNull(refundOrder, "退款单信息不存在");
        if (RefundStatusEnum.REFUNDING.getCode() == refundOrder.getStatus()) {
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

        // 去微信查询退款单信息
        WxpayQueryRefundRes queryOrderRes = WxpayUtils.queryRefundOrder(payConfig, refundOrder);

        // 退款成功
        if (WxpayRefundStatusEnum.SUCCESS.getCode().equals(queryOrderRes.getStatus())) {
            // 更新微信订单号
            refundOrderMapper.updateOuterTradeNo(queryOrderRes.getRefund_id(), payOrder.getOrderNo());
            // 退款成功处理
            goodsStrategyManager.refundSuccess(GoodsTypeEnum.getEnumByCode(payOrder.getGoodsType()), refundNo);
        }
    }

    @Override
    public WxpayQueryAuthVO getAuthInfo(WxpayQueryAuthReq request) {
        WxpayQueryAuthVO response = new WxpayQueryAuthVO();
        // 查询支付配置信息
        PayConfig payConfig = payConfigService.selectPayConfigInfo(PayChannelEnum.WX.getCode(), PaySubChannelEnum.WX_JSAPI.getCode());
        AssertUtils.notNull(payConfig, "支付配置信息为空");

        log.info("微信通过code换取网页授权信息");
        String url = StrUtil.format("https://api.weixin.qq.com/sns/oauth2/access_token?appid={}&secret={}&code={}&grant_type=authorization_code",
                payConfig.getAppid(), payConfig.getAppSecret(), request.getCode());
        // 获取网页授权信息
        String res = HttpUtil.get(url);
        JSONObject jsonObject = JSON.parseObject(res);
        response.setAccessToken(jsonObject.getString("access_token"));
        response.setExpiresIn(jsonObject.getString("expires_in"));
        response.setIsSnapshotuser(jsonObject.getString("is_snapshotuser"));
        response.setOpenid(jsonObject.getString("openid"));
        response.setRefreshToken(jsonObject.getString("refresh_token"));
        response.setScope(jsonObject.getString("scope"));
        response.setUnionid(jsonObject.getString("unionid"));
        return response;
    }


    /**
     * 生成订单信息
     *
     * @param request 下单请求信息
     * @param payConfig 支付配置信息
     * @return
     */
    private PayOrder generateOrder(WxpayJsapiPayReq request, PayConfig payConfig) {
        // 雪花算法生成订单号
        String orderNo = IdUtil.getSnowflakeNextIdStr();

        // 根据商品类型计算对应的金额
        GoodsTypeEnum goodsTypeEnum = GoodsTypeEnum.getEnumByCode(request.getGoodsType());
        AssertUtils.notNull(goodsTypeEnum, "不支持的商品类型" + request.getGoodsType());

        CalculateGoodsDTO calculateGoods = CalculateGoodsDTO.builder()
                .goodsNum(request.getGoodsNum())
                .vipId(request.getVipId())
                .blueprintsId(request.getBlueprintsId())
                .userId(SecurityUtils.getUserId())
                .build();
        CalculateGoodsResDTO goodsRes = goodsStrategyManager.calculate(goodsTypeEnum, calculateGoods);

        // 生成订单信息
        PayOrder payOrder = new PayOrder();
        payOrder.setCreateBy(SecurityUtils.getUsername());
        payOrder.setUserId(request.getUserId());
        payOrder.setOrderNo(orderNo);
        payOrder.setAmount(goodsRes.getAmount());
        payOrder.setSubject(goodsRes.getSubject());
        payOrder.setGoodsNum(request.getGoodsNum());
        payOrder.setGoodsType(request.getGoodsType());
        payOrder.setBlueprintsId(request.getBlueprintsId());
        payOrder.setVipId(request.getVipId());
        payOrder.setChannel(payConfig.getSubChannel());
        payOrder.setSubChannel(payConfig.getSubChannel());
        payOrder.setAppid(payConfig.getAppid());
        payOrder.setMchid(payConfig.getMchid());
        payOrder.setStatus(PayStatusEnum.PAYING.getCode());
        payOrder.setDiscount(goodsRes.getDiscount());
        payOrder.setActualAmount(goodsRes.getActualAmount());
        payOrder.setDiscountAmount(goodsRes.getDiscountAmount());
        payOrderMapper.insert(payOrder);
        return payOrder;
    }
}
