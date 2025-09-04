package com.ruoyi.system.convert;


import com.ruoyi.system.domain.Feedback;
import com.ruoyi.system.domain.req.AddFeedbackReq;
import com.ruoyi.system.domain.req.ReplyFeedbackReq;
import com.ruoyi.system.domain.req.QueryFeedbackListReq;
import com.ruoyi.system.domain.vo.FeedbackInfoVO;
import com.ruoyi.system.domain.vo.FeedbackListVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 反馈bean转换器
 * 
 * @author tds
 * @date 2025-03-12
 */
@Mapper
public interface FeedbackConvert
{
    FeedbackConvert INSTANCE = Mappers.getMapper(FeedbackConvert.class);

    Feedback convert(QueryFeedbackListReq queryReq);

    List<FeedbackListVO> convert(List<Feedback> list);

    FeedbackInfoVO convert(Feedback feedback);

    Feedback convert(AddFeedbackReq feedback);

    Feedback convert(ReplyFeedbackReq feedbackReq);

}
