package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.PayConfig;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.system.domain.req.AddPayConfigReq;
import com.ruoyi.system.domain.req.EditPayConfigReq;
import com.ruoyi.system.domain.req.QueryPayConfigListReq;
import com.ruoyi.system.domain.vo.PayConfigInfoVO;
import com.ruoyi.system.domain.vo.PayConfigListVO;

/**
 * 支付配置Service接口
 * 
 * @author tds
 * @date 2025-03-21
 */
public interface IPayConfigService extends IService<PayConfig>
{
    /**
     * 查询支付配置
     * 
     * @param id 支付配置主键
     * @return 支付配置
     */
    PayConfigInfoVO selectPayConfigById(Long id);

    /**
     * 查询支付配置列表
     * 
     * @param queryReq 支付配置
     * @return 支付配置集合
     */
    List<PayConfigListVO> selectPayConfigList(QueryPayConfigListReq queryReq);

    /**
     * 新增支付配置
     * 
     * @param addReq 支付配置
     * @return 结果
     */
    boolean insertPayConfig(AddPayConfigReq addReq);

    /**
     * 修改支付配置
     * 
     * @param editReq 支付配置
     * @return 结果
     */
    boolean updatePayConfig(EditPayConfigReq editReq);

    /**
     * 批量删除支付配置
     * 
     * @param ids 需要删除的支付配置主键集合
     * @return 结果
     */
    boolean deletePayConfigByIds(Long[] ids);

    /**
     * 根据应用id查询支付配置信息
     *
     * @param channel 支付渠道
     * @param subChannel 子支付渠道
     * @return
     */
    PayConfig selectPayConfigInfo(int channel, int subChannel);
}
