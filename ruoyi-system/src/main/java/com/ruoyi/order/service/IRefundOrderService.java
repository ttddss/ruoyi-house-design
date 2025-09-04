package com.ruoyi.order.service;

import java.util.List;
import com.ruoyi.order.domain.RefundOrder;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.order.domain.req.QueryRefundOrderListReq;
import com.ruoyi.order.domain.vo.RefundOrderInfoVO;
import com.ruoyi.order.domain.vo.RefundOrderListVO;

/**
 * 退款单Service接口
 * 
 * @author tds
 * @date 2025-03-21
 */
public interface IRefundOrderService extends IService<RefundOrder>
{
    /**
     * 查询退款单
     * 
     * @param id 退款单主键
     * @return 退款单
     */
    RefundOrderInfoVO selectRefundOrderById(Long id);

    /**
     * 查询退款单列表
     * 
     * @param queryReq 退款单
     * @return 退款单集合
     */
    List<RefundOrderListVO> selectRefundOrderList(QueryRefundOrderListReq queryReq);


}
