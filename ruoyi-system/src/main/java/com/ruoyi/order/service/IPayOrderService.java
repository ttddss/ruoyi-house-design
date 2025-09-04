package com.ruoyi.order.service;

import java.util.List;
import com.ruoyi.order.domain.PayOrder;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.order.domain.req.QueryPayOrderListReq;
import com.ruoyi.order.domain.vo.PayOrderInfoVO;
import com.ruoyi.order.domain.vo.PayOrderListVO;
import org.apache.ibatis.annotations.Param;

/**
 * 支付订单Service接口
 * 
 * @author tds
 * @date 2025-03-21
 */
public interface IPayOrderService extends IService<PayOrder>
{
    /**
     * 查询支付订单
     * 
     * @param id 支付订单主键
     * @return 支付订单
     */
    PayOrderInfoVO selectPayOrderById(Long id);

    /**
     * 查询支付订单列表
     * 
     * @param queryReq 支付订单
     * @return 支付订单集合
     */
    List<PayOrderListVO> selectPayOrderList(QueryPayOrderListReq queryReq);


    /**
     * 关闭订单
     * @param orderNo
     */
    int closeOrder(String orderNo);



}
