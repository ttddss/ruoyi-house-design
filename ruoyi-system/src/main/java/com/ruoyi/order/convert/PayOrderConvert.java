package com.ruoyi.order.convert;


import com.ruoyi.order.domain.PayOrder;
import com.ruoyi.order.domain.req.QueryPayOrderListReq;
import com.ruoyi.order.domain.vo.PayOrderInfoVO;
import com.ruoyi.order.domain.vo.PayOrderListVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 支付订单bean转换器
 * 
 * @author tds
 * @date 2025-03-21
 */
@Mapper
public interface PayOrderConvert
{
    PayOrderConvert INSTANCE = Mappers.getMapper(PayOrderConvert.class);

    PayOrder convert(QueryPayOrderListReq queryReq);

    List<PayOrderListVO> convert(List<PayOrder> list);

    PayOrderInfoVO convert(PayOrder payOrder);


}
