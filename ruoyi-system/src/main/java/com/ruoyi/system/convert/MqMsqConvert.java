package com.ruoyi.system.convert;


import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 队列消息bean转换器
 * 
 * @author ruoyi
 * @date 2024-08-18
 */
@Mapper
public interface MqMsqConvert
{
    MqMsqConvert INSTANCE = Mappers.getMapper(MqMsqConvert.class);


}
