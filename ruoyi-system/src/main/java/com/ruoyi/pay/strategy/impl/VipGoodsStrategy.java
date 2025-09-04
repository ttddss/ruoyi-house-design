package com.ruoyi.pay.strategy.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruoyi.common.utils.AssertUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.member.domain.Vip;
import com.ruoyi.member.domain.VipUser;
import com.ruoyi.member.mapper.VipMapper;
import com.ruoyi.member.mapper.VipUserMapper;
import com.ruoyi.order.domain.PayOrder;
import com.ruoyi.order.domain.RefundOrder;
import com.ruoyi.order.enums.RefundStatusEnum;
import com.ruoyi.order.mapper.PayOrderMapper;
import com.ruoyi.order.mapper.RefundOrderMapper;
import com.ruoyi.pay.domain.dto.CalculateGoodsDTO;
import com.ruoyi.pay.domain.dto.CalculateGoodsResDTO;
import com.ruoyi.pay.strategy.IGoodsStrategy;
import com.ruoyi.system.enums.GoodsTypeEnum;
import com.ruoyi.order.enums.PayStatusEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 *
 * @author tongdashuai
 * @version 1.0
 * @description
 * @module
 * @date 2025/3/25 18:11
 */
@Component
@Slf4j
public class VipGoodsStrategy implements IGoodsStrategy {

    @Autowired
    private VipMapper vipMapper;

    @Autowired
    private VipUserMapper vipUserMapper;

    @Autowired
    private PayOrderMapper payOrderMapper;

    @Autowired
    private RefundOrderMapper refundOrderMapper;

    /**
     * 计算订单金额等信息
     *
     * @param calculateGoods
     */
    @Override
    public CalculateGoodsResDTO calculate(CalculateGoodsDTO calculateGoods) {
        Vip vipInfo = vipMapper.selectById(calculateGoods.getVipId());
        AssertUtils.notNull(vipInfo, "vip信息为空");

        // 总金额 = 单价 * 数量
        BigDecimal amount = vipInfo.getPrice().multiply(new BigDecimal(calculateGoods.getGoodsNum())).setScale(2, BigDecimal.ROUND_HALF_DOWN);

        return CalculateGoodsResDTO.builder()
                .amount(amount)
                .discountAmount(BigDecimal.ZERO)
                .discount(BigDecimal.TEN)
                .actualAmount(amount)
                .subject(vipInfo.getName())
                .build();
    }

    /**
     * 支付成功处理
     *
     * @param orderNo
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void paySuccess(String orderNo) {
        // 查询订单信息
        PayOrder payOrder = payOrderMapper.selectOne(new LambdaQueryWrapper<PayOrder>().eq(PayOrder::getOrderNo, orderNo));
        AssertUtils.notNull(payOrder, "订单信息为空");
        if (PayStatusEnum.SUCCESS.getCode() == payOrder.getStatus()) {
            log.info("订单已支付成功，无需更新");
            return;
        }

        AssertUtils.isTrue(PayStatusEnum.PAYING.getCode() == payOrder.getStatus()
                        || PayStatusEnum.CLOSE.getCode() == payOrder.getStatus()
                        || PayStatusEnum.ERROR.getCode() == payOrder.getStatus(),
                "订单状态异常");

        // 查询用户是否已经开通了该vip了
        VipUser vipUserDb = vipUserMapper.selectOne(new LambdaQueryWrapper<VipUser>()
                .eq(VipUser::getUserId, payOrder.getUserId())
                .eq(VipUser::getVipId, payOrder.getVipId())
                .gt(VipUser::getExpireTime, new Date()));
        // 未开通vip，给用户开通
        if (vipUserDb == null) {
            VipUser vipUser = new VipUser();
            vipUser.setUserId(payOrder.getUserId());
            vipUser.setVipId(payOrder.getVipId());
            vipUser.setExpireTime(DateUtil.offsetMonth(new Date(), payOrder.getGoodsNum()));
            vipUserMapper.insert(vipUser);
        } else {
            // 已开通vip，更新vip的过期时间
            VipUser updateDo = new VipUser();
            updateDo.setId(vipUserDb.getId());
            updateDo.setExpireTime(DateUtil.offsetMonth(vipUserDb.getExpireTime(), payOrder.getGoodsNum()));
            vipUserMapper.updateById(updateDo);
        }

        // 更新订单状态
        PayOrder updateDo = new PayOrder();
        updateDo.setId(payOrder.getId());
        updateDo.setStatus(PayStatusEnum.SUCCESS.getCode());
        updateDo.setUpdateBy(SecurityUtils.getUsername(false));
        payOrderMapper.updateById(updateDo);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void refundSuccess(String refundNo) {
        // 查询退款单信息
        RefundOrder refundOrder = refundOrderMapper.selectOne(new LambdaQueryWrapper<RefundOrder>().eq(RefundOrder::getRefundNo, refundNo));
        AssertUtils.notNull(refundOrder, "退款单信息为空");
        AssertUtils.isTrue(RefundStatusEnum.REFUNDING.getCode() == refundOrder.getStatus(),"退款单状态异常");

        // 查询订单信息
        PayOrder payOrder = payOrderMapper.selectOne(new LambdaQueryWrapper<PayOrder>().eq(PayOrder::getOrderNo, refundOrder.getOrderNo()));
        AssertUtils.notNull(payOrder, "订单信息为空");
        AssertUtils.isTrue(PayStatusEnum.SUCCESS.getCode() == payOrder.getStatus(),"订单状态异常");


        // 查询用户开通的vip信息
        VipUser vipUserDb = vipUserMapper.selectOne(new LambdaQueryWrapper<VipUser>()
                .eq(VipUser::getUserId, payOrder.getUserId())
                .eq(VipUser::getVipId, payOrder.getVipId())
                .gt(VipUser::getExpireTime, new Date()));
        // 已开通vip，更新vip的过期时间
        if (vipUserDb != null) {
            VipUser updateDo = new VipUser();
            updateDo.setId(vipUserDb.getId());
            updateDo.setExpireTime(DateUtil.offsetMonth(vipUserDb.getExpireTime(), -payOrder.getGoodsNum()));
            vipUserMapper.updateById(updateDo);
        }

        // 更新订单状态
        PayOrder updateDo = new PayOrder();
        updateDo.setId(payOrder.getId());
        updateDo.setStatus(PayStatusEnum.REFUNDED.getCode());
        updateDo.setUpdateBy(SecurityUtils.getUsername(false));
        payOrderMapper.updateById(updateDo);

        // 更新退款单状态
        RefundOrder updateRefundDo = new RefundOrder();
        updateRefundDo.setId(refundOrder.getId());
        updateRefundDo.setStatus(RefundStatusEnum.SUCCESS.getCode());
        updateRefundDo.setUpdateBy(SecurityUtils.getUsername(false));
        refundOrderMapper.updateById(updateRefundDo);
    }

    @Override
    public GoodsTypeEnum getGoodsType() {
        return GoodsTypeEnum.VIP;
    }
}
