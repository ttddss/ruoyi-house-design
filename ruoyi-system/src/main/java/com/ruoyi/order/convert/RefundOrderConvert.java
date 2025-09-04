package com.ruoyi.order.convert;


import com.ruoyi.order.domain.RefundOrder;
import com.ruoyi.order.domain.req.QueryRefundOrderListReq;
import com.ruoyi.order.domain.vo.RefundOrderInfoVO;
import com.ruoyi.order.domain.vo.RefundOrderListVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 退款单bean转换器
 * 
 * @author tds
 * @date 2025-03-21
 */
@Mapper
public interface RefundOrderConvert
{
    RefundOrderConvert INSTANCE = Mappers.getMapper(RefundOrderConvert.class);

    RefundOrder convert(QueryRefundOrderListReq queryReq);

    List<RefundOrderListVO> convert(List<RefundOrder> list);

    RefundOrderInfoVO convert(RefundOrder refundOrder);


}
