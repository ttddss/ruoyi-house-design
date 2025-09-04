package com.ruoyi.pay.strategy;

import com.ruoyi.pay.domain.dto.CalculateGoodsDTO;
import com.ruoyi.pay.domain.dto.CalculateGoodsResDTO;
import com.ruoyi.system.enums.GoodsTypeEnum;

/**
 *
 *
 * @author tongdashuai
 * @version 1.0
 * @description
 * @module
 * @date 2025/3/25 18:04
 */
public interface IGoodsStrategy {

    /**
     * 计算订单金额等信息
     */
    CalculateGoodsResDTO calculate(CalculateGoodsDTO calculateGoods);

    /**
     * 支付成功处理
     *
     * @param orderNo
     */
    void paySuccess(String orderNo);

    /**
     * 退款成功处理
     * @param refundNo
     */
    void refundSuccess(String refundNo);

    /**
     * 返回商品类型
     */
    GoodsTypeEnum getGoodsType();


}
