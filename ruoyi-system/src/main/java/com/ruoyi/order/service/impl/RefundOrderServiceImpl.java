package com.ruoyi.order.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.order.convert.RefundOrderConvert;
import com.ruoyi.order.domain.RefundOrder;
import com.ruoyi.order.domain.req.QueryRefundOrderListReq;
import com.ruoyi.order.domain.vo.RefundOrderInfoVO;
import com.ruoyi.order.domain.vo.RefundOrderListVO;
import com.ruoyi.order.mapper.RefundOrderMapper;
import com.ruoyi.order.service.IRefundOrderService;
import com.ruoyi.system.mapper.SysUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 退款单Service业务层处理
 * 
 * @author tds
 * @date 2025-03-21
 */
@Service
public class RefundOrderServiceImpl extends ServiceImpl<RefundOrderMapper, RefundOrder> implements IRefundOrderService
{

    @Autowired
    private SysUserMapper sysUserMapper;


    /**
     * 查询退款单
     * 
     * @param id 退款单主键
     * @return 退款单
     */
    @Override
    public RefundOrderInfoVO selectRefundOrderById(Long id)
    {
        RefundOrderInfoVO infoVO = RefundOrderConvert.INSTANCE.convert(this.getById(id));
        SysUser sysUser = sysUserMapper.selectUserById(infoVO.getUserId());
        if (sysUser != null) {
            infoVO.setNickName(sysUser.getNickName());
            infoVO.setPhonenumber(sysUser.getPhonenumber());
        }
        return infoVO;
    }

    /**
     * 查询退款单列表
     * 
     * @param queryReq 退款单
     * @return 退款单
     */
    @Override
    public List<RefundOrderListVO> selectRefundOrderList(QueryRefundOrderListReq queryReq)
    {
        return getBaseMapper().selectRefundOrderList(queryReq);
    }




}
