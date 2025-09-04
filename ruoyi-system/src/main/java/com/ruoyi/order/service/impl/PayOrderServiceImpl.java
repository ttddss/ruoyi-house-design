package com.ruoyi.order.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.order.convert.PayOrderConvert;
import com.ruoyi.order.domain.PayOrder;
import com.ruoyi.order.domain.req.QueryPayOrderListReq;
import com.ruoyi.order.domain.vo.PayOrderInfoVO;
import com.ruoyi.order.domain.vo.PayOrderListVO;
import com.ruoyi.order.mapper.PayOrderMapper;
import com.ruoyi.order.service.IPayOrderService;
import com.ruoyi.system.mapper.SysUserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 支付订单Service业务层处理
 * 
 * @author tds
 * @date 2025-03-21
 */
@Service
@Slf4j
public class PayOrderServiceImpl extends ServiceImpl<PayOrderMapper, PayOrder> implements IPayOrderService
{

    @Autowired
    private SysUserMapper sysUserMapper;

    /**
     * 查询支付订单
     * 
     * @param id 支付订单主键
     * @return 支付订单
     */
    @Override
    public PayOrderInfoVO selectPayOrderById(Long id)
    {
        PayOrderInfoVO infoVO = PayOrderConvert.INSTANCE.convert(this.getById(id));
        SysUser sysUser = sysUserMapper.selectUserById(infoVO.getUserId());
        if (sysUser != null) {
            infoVO.setNickName(sysUser.getNickName());
            infoVO.setPhonenumber(sysUser.getPhonenumber());
        }
        return infoVO;
    }

    /**
     * 查询支付订单列表
     * 
     * @param queryReq 支付订单
     * @return 支付订单
     */
    @Override
    public List<PayOrderListVO> selectPayOrderList(QueryPayOrderListReq queryReq)
    {
        return getBaseMapper().selectPayOrderList(queryReq);
    }

    @Override
    public int closeOrder(String orderNo) {
        return baseMapper.closeOrder(orderNo);
    }



}
