package com.ruoyi.blueprints.convert;


import com.ruoyi.blueprints.domain.BlueprintsCollect;
import com.ruoyi.blueprints.domain.vo.BlueprintsCollectInfoVO;
import com.ruoyi.blueprints.domain.vo.BlueprintsCollectListVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 用户设计图纸收藏bean转换器
 * 
 * @author tds
 * @date 2025-03-03
 */
@Mapper
public interface BlueprintsCollectConvert
{
    BlueprintsCollectConvert INSTANCE = Mappers.getMapper(BlueprintsCollectConvert.class);


    List<BlueprintsCollectListVO> convert(List<BlueprintsCollect> list);

    BlueprintsCollectInfoVO convert(BlueprintsCollect blueprintsCollect);



}
