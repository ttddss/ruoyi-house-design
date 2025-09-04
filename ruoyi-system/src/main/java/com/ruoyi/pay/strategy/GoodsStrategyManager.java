package com.ruoyi.pay.strategy;

import com.ruoyi.common.utils.AssertUtils;
import com.ruoyi.pay.domain.dto.CalculateGoodsDTO;
import com.ruoyi.pay.domain.dto.CalculateGoodsResDTO;
import com.ruoyi.system.enums.GoodsTypeEnum;
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
 * @date 2025/3/25 18:17
 */
@Component
public class GoodsStrategyManager {

    private static final Map<Integer, IGoodsStrategy> STRATEGY_MAP = new HashMap<>();

    public GoodsStrategyManager(List<IGoodsStrategy> goodsStrategyList) {
        for (IGoodsStrategy goodsStrategy : goodsStrategyList) {
            STRATEGY_MAP.put(goodsStrategy.getGoodsType().getCode(), goodsStrategy);
        }
    }

    /**
     * 计算订单金额等信息
     */
    public CalculateGoodsResDTO calculate(GoodsTypeEnum goodsTypeEnum, CalculateGoodsDTO calculateGoods) {
        IGoodsStrategy goodsStrategy = STRATEGY_MAP.get(goodsTypeEnum.getCode());
        AssertUtils.notNull(goodsStrategy, "不支持的商品类型");
        return goodsStrategy.calculate(calculateGoods);
    }

    /**
     * 支付成功处理
     *
     * @param orderNo 订单号
     */
    public void paySuccess(GoodsTypeEnum goodsTypeEnum, String orderNo) {
        IGoodsStrategy goodsStrategy = STRATEGY_MAP.get(goodsTypeEnum.getCode());
        AssertUtils.notNull(goodsStrategy, "商品策略不存在");
        goodsStrategy.paySuccess(orderNo);
    }

    /**
     * 退款成功处理
     * @param goodsTypeEnum
     * @param refundNo 退款单号
     */
    public void refundSuccess(GoodsTypeEnum goodsTypeEnum, String refundNo) {
        IGoodsStrategy goodsStrategy = STRATEGY_MAP.get(goodsTypeEnum.getCode());
        AssertUtils.notNull(goodsStrategy, "商品策略不存在");
        goodsStrategy.refundSuccess(refundNo);
    }
}
