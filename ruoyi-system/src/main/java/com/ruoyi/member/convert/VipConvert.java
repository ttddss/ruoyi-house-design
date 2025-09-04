package com.ruoyi.member.convert;


import com.ruoyi.member.domain.Vip;
import com.ruoyi.member.domain.req.AddVipReq;
import com.ruoyi.member.domain.req.EditVipReq;
import com.ruoyi.member.domain.req.QueryVipListReq;
import com.ruoyi.member.domain.vo.MallVipListVO;
import com.ruoyi.member.domain.vo.VipInfoVO;
import com.ruoyi.member.domain.vo.VipListVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * vipbean转换器
 * 
 * @author tds
 * @date 2025-03-13
 */
@Mapper
public interface VipConvert
{
    VipConvert INSTANCE = Mappers.getMapper(VipConvert.class);

    Vip convert(QueryVipListReq queryReq);

    List<VipListVO> convert(List<Vip> list);

    VipInfoVO convert(Vip vip);

    Vip convert(AddVipReq vip);

    Vip convert(EditVipReq vipReq);

    MallVipListVO convertMallVo(Vip vip);
}
