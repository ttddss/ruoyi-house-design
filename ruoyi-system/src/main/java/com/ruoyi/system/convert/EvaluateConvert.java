package com.ruoyi.system.convert;


import com.ruoyi.system.domain.Evaluate;
import com.ruoyi.system.domain.req.AddEvaluateReq;
import com.ruoyi.system.domain.req.EditEvaluateReq;
import com.ruoyi.system.domain.req.QueryEvaluateListReq;
import com.ruoyi.system.domain.vo.EvaluateInfoVO;
import com.ruoyi.system.domain.vo.EvaluateListVO;
import com.ruoyi.system.domain.vo.MallEvaluateListVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 评价bean转换器
 * 
 * @author tds
 * @date 2025-04-02
 */
@Mapper
public interface EvaluateConvert
{
    EvaluateConvert INSTANCE = Mappers.getMapper(EvaluateConvert.class);

    Evaluate convert(QueryEvaluateListReq queryReq);

    List<EvaluateListVO> convert(List<Evaluate> list);

    EvaluateInfoVO convert(Evaluate evaluate);

    Evaluate convert(AddEvaluateReq evaluate);

    Evaluate convert(EditEvaluateReq evaluateReq);

    List<MallEvaluateListVO> convertMallList(List<EvaluateListVO> list);
}
