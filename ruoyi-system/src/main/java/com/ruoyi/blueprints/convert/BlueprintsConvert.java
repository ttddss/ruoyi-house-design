package com.ruoyi.blueprints.convert;


import com.ruoyi.blueprints.domain.Blueprints;
import com.ruoyi.blueprints.domain.BlueprintsDetail;
import com.ruoyi.blueprints.domain.req.AddBlueprintsReq;
import com.ruoyi.blueprints.domain.req.EditBlueprintsReq;
import com.ruoyi.blueprints.domain.req.QueryBlueprintsListReq;
import com.ruoyi.blueprints.domain.vo.BlueprintsInfoVO;
import com.ruoyi.blueprints.domain.vo.BlueprintsListVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 图纸列表bean转换器
 * 
 * @author tds
 * @date 2025-03-03
 */
@Mapper
public interface BlueprintsConvert
{
    BlueprintsConvert INSTANCE = Mappers.getMapper(BlueprintsConvert.class);

    Blueprints convert(QueryBlueprintsListReq queryReq);

    List<BlueprintsListVO> convert(List<Blueprints> list);

    BlueprintsInfoVO convert(Blueprints blueprints);

    Blueprints convert(AddBlueprintsReq blueprints);

    Blueprints convert(EditBlueprintsReq blueprintsReq);

    BlueprintsDetail convertDetail(AddBlueprintsReq addReq);

    BlueprintsDetail convertDetail(EditBlueprintsReq editReq);
}
