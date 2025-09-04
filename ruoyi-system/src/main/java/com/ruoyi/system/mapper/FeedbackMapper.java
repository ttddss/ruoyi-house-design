package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.Feedback;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.system.domain.req.QueryFeedbackListReq;
import com.ruoyi.system.domain.vo.FeedbackListVO;

/**
 * 反馈Mapper接口
 * 
 * @author tds
 * @date 2025-03-12
 */
public interface FeedbackMapper extends BaseMapper<Feedback>
{


    /**
     * 查询反馈列表
     * 
     * @param queryReq 反馈
     * @return 反馈集合
     */
    List<FeedbackListVO> selectFeedbackList(QueryFeedbackListReq queryReq);

}
