package com.ruoyi.system.convert;


import com.ruoyi.system.domain.PayConfig;
import com.ruoyi.system.domain.req.AddPayConfigReq;
import com.ruoyi.system.domain.req.EditPayConfigReq;
import com.ruoyi.system.domain.req.QueryPayConfigListReq;
import com.ruoyi.system.domain.vo.PayConfigInfoVO;
import com.ruoyi.system.domain.vo.PayConfigListVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 支付配置bean转换器
 * 
 * @author tds
 * @date 2025-03-21
 */
@Mapper
public interface PayConfigConvert
{
    PayConfigConvert INSTANCE = Mappers.getMapper(PayConfigConvert.class);

    PayConfig convert(QueryPayConfigListReq queryReq);

    List<PayConfigListVO> convert(List<PayConfig> list);

    PayConfigInfoVO convert(PayConfig payConfig);

    PayConfig convert(AddPayConfigReq payConfig);

    PayConfig convert(EditPayConfigReq payConfigReq);

}
