package com.ruoyi.order.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruoyi.order.domain.PayOrder;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.order.domain.RefundOrder;
import com.ruoyi.order.domain.req.QueryPayOrderListReq;
import com.ruoyi.order.domain.vo.PayOrderListVO;
import org.apache.ibatis.annotations.Param;

/**
 * 支付订单Mapper接口
 * 
 * @author tds
 * @date 2025-03-21
 */
public interface PayOrderMapper extends BaseMapper<PayOrder>
{


    /**
     * 查询支付订单列表
     * 
     * @param queryReq 支付订单
     * @return 支付订单集合
     */
    List<PayOrderListVO> selectPayOrderList(QueryPayOrderListReq queryReq);

    /**
     * 关闭订单
     *
     * @param orderNo 订单号
     */
    int closeOrder(String orderNo);

    /**
     * 更新外部流水号
     *
     * @param outTradeNo 外部流水号
     * @param orderNo 订单号
     */
    int updateOuterTradeNo(@Param("outTradeNo") String outTradeNo, @Param("orderNo") String orderNo);

    /**
     * 更新支付失败
     *
     * @param orderNo 订单号
     * @param code 错误码
     * @param msg 错误信息
     * @return
     */
    int updateFail(@Param("orderNo") String orderNo, @Param("code") String code, @Param("msg") String msg);

    /**
     * 更新错误码和错误信息
     *
     * @param orderNo 订单号
     * @param code 错误码
     * @param msg 错误信息
     */
    int updateCodeAndMsg(@Param("orderNo") String orderNo, @Param("code") String code, @Param("msg") String msg);


}
