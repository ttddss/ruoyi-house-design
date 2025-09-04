package com.ruoyi.blueprints.convert;


import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 用户图纸bean转换器
 * 
 * @author tds
 * @date 2025-03-26
 */
@Mapper
public interface BlueprintsUserConvert
{
    BlueprintsUserConvert INSTANCE = Mappers.getMapper(BlueprintsUserConvert.class);



}
