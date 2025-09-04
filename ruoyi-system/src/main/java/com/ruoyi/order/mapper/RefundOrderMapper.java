package com.ruoyi.order.mapper;

import java.util.List;
import com.ruoyi.order.domain.RefundOrder;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.order.domain.req.QueryRefundOrderListReq;
import com.ruoyi.order.domain.vo.RefundOrderListVO;
import org.apache.ibatis.annotations.Param;

/**
 * 退款单Mapper接口
 * 
 * @author tds
 * @date 2025-03-21
 */
public interface RefundOrderMapper extends BaseMapper<RefundOrder>
{


    /**
     * 查询退款单列表
     * 
     * @param queryReq 退款单
     * @return 退款单集合
     */
    List<RefundOrderListVO> selectRefundOrderList(QueryRefundOrderListReq queryReq);

    /**
     * 更新退款单失败
     *
     * @param refundNo 退款单号
     * @param code 返回码
     * @param msg 返回信息
     * @return
     */
    int updateFail(@Param("refundNo") String refundNo, @Param("code") String code, @Param("msg") String msg);

    /**
     * 更新外部交易流水号
     *
     * @param outTradeNo 外部交易流水号
     * @param refundNo 退款单号
     * @return
     */
    int updateOuterTradeNo(@Param("outTradeNo") String outTradeNo, @Param("refundNo") String refundNo);
}
