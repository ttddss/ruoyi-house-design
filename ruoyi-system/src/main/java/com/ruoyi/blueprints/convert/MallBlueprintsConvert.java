package com.ruoyi.blueprints.convert;


import com.ruoyi.blueprints.domain.vo.*;
import com.ruoyi.order.domain.vo.PayOrderListVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 图纸列表bean转换器
 * 
 * @author tds
 * @date 2025-03-03
 */
@Mapper
public interface MallBlueprintsConvert
{
    MallBlueprintsConvert INSTANCE = Mappers.getMapper(MallBlueprintsConvert.class);


    List<MallBlueprintsListVO> convert(List<BlueprintsListVO> list);

    MallBlueprintsInfoVO convert(BlueprintsInfoVO blueprintsInfoVO);


    List<MallPayOrderListVO> convertOrderList(List<PayOrderListVO> list);
}
