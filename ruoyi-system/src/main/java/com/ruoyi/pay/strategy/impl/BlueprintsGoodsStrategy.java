package com.ruoyi.pay.strategy.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.blueprints.domain.BlueprintsUser;
import com.ruoyi.blueprints.domain.vo.BlueprintsInfoVO;
import com.ruoyi.blueprints.mapper.BlueprintsMapper;
import com.ruoyi.blueprints.mapper.BlueprintsUserMapper;
import com.ruoyi.common.utils.AssertUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.member.domain.Vip;
import com.ruoyi.member.domain.VipUser;
import com.ruoyi.member.mapper.VipMapper;
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
 * @date 2025/3/25 18:12
 */
@Component
@Slf4j
public class BlueprintsGoodsStrategy implements IGoodsStrategy {

    @Autowired
    private VipMapper vipMapper;

    @Autowired
    private BlueprintsMapper blueprintsMapper;

    @Autowired
    private PayOrderMapper payOrderMapper;

    @Autowired
    private RefundOrderMapper refundOrderMapper;

    @Autowired
    private BlueprintsUserMapper blueprintsUserMapper;

    /**
     * 计算订单金额等信息
     *
     * @param calculateGoods
     */
    @Override
    public CalculateGoodsResDTO calculate(CalculateGoodsDTO calculateGoods) {
        // 获取用户最合适的vip会员的折扣
        Vip vip = vipMapper.selectUserVip(calculateGoods.getUserId());
        BigDecimal discount = vip == null ? BigDecimal.TEN : vip.getDiscount();

        // 查询图纸信息
        BlueprintsInfoVO blueprints = blueprintsMapper.selectBlueprintsById(calculateGoods.getBlueprintsId());
        AssertUtils.notNull(blueprints, "图纸信息为空");

        // 总金额 = 单价 * 数量
        BigDecimal amount = blueprints.getPrice().multiply(new BigDecimal(calculateGoods.getGoodsNum())).setScale(2, BigDecimal.ROUND_HALF_DOWN);
        // 实付金额 = 订单金额 * 折扣
        BigDecimal actualAmount = amount.multiply(discount).divide(BigDecimal.TEN, 2, BigDecimal.ROUND_HALF_DOWN);
        // 优惠金额 = 订单金额 - 实付金额
        BigDecimal discountAmount = amount.subtract(actualAmount);

        return CalculateGoodsResDTO.builder()
                .amount(amount)
                .discountAmount(discountAmount)
                .discount(discount)
                .actualAmount(actualAmount)
                .subject(blueprints.getName())
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

        // 发放图纸商品
        BlueprintsUser blueprintsUser = new BlueprintsUser();
        blueprintsUser.setBlueprintsId(payOrder.getBlueprintsId());
        blueprintsUser.setUserId(payOrder.getUserId());
        blueprintsUserMapper.insert(blueprintsUser);

        // 更新销售量
        blueprintsMapper.updateSaleNum(payOrder.getBlueprintsId(), 1);

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

        // 删除用户购买的图纸商品
        blueprintsUserMapper.delete(new LambdaQueryWrapper<BlueprintsUser>()
                .eq(BlueprintsUser::getUserId, payOrder.getUserId())
                .eq(BlueprintsUser::getBlueprintsId, payOrder.getBlueprintsId()));

        // 更新订单状态
        PayOrder updateDo = new PayOrder();
        updateDo.setId(payOrder.getId());
        updateDo.setStatus(PayStatusEnum.REFUNDED.getCode());
        updateDo.setUpdateBy(SecurityUtils.getUsername(false));
        payOrderMapper.updateById(updateDo);

        // 更新销售量
        blueprintsMapper.updateSaleNum(payOrder.getBlueprintsId(), -1);

        // 更新退款单状态
        RefundOrder updateRefundDo = new RefundOrder();
        updateRefundDo.setId(refundOrder.getId());
        updateRefundDo.setStatus(RefundStatusEnum.SUCCESS.getCode());
        updateRefundDo.setUpdateBy(SecurityUtils.getUsername(false));
        refundOrderMapper.updateById(updateRefundDo);
    }

    @Override
    public GoodsTypeEnum getGoodsType() {
        return GoodsTypeEnum.BLUEPRINTS;
    }
}
