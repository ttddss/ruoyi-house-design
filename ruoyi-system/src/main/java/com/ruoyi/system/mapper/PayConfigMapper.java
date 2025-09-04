package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.PayConfig;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.system.domain.req.QueryPayConfigListReq;

/**
 * 支付配置Mapper接口
 * 
 * @author tds
 * @date 2025-03-21
 */
public interface PayConfigMapper extends BaseMapper<PayConfig>
{


    /**
     * 查询支付配置列表
     * 
     * @param queryReq 支付配置
     * @return 支付配置集合
     */
    List<PayConfig> selectPayConfigList(QueryPayConfigListReq queryReq);

}
