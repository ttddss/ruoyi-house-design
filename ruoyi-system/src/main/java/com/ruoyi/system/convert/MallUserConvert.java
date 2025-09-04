package com.ruoyi.system.convert;


import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.system.domain.vo.MallUserVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 图纸列表bean转换器
 * 
 * @author tds
 * @date 2025-03-03
 */
@Mapper
public interface MallUserConvert
{
    MallUserConvert INSTANCE = Mappers.getMapper(MallUserConvert.class);


    MallUserVO convert(SysUser user);
}
