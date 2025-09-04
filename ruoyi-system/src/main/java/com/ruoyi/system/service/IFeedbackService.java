package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.Feedback;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.system.domain.req.AddFeedbackReq;
import com.ruoyi.system.domain.req.ReplyFeedbackReq;
import com.ruoyi.system.domain.req.QueryFeedbackListReq;
import com.ruoyi.system.domain.vo.FeedbackInfoVO;
import com.ruoyi.system.domain.vo.FeedbackListVO;

/**
 * 反馈Service接口
 * 
 * @author tds
 * @date 2025-03-12
 */
public interface IFeedbackService extends IService<Feedback>
{
    /**
     * 查询反馈
     * 
     * @param id 反馈主键
     * @return 反馈
     */
    FeedbackInfoVO selectFeedbackById(Long id);

    /**
     * 查询反馈列表
     * 
     * @param queryReq 反馈
     * @return 反馈集合
     */
    List<FeedbackListVO> selectFeedbackList(QueryFeedbackListReq queryReq);

    /**
     * 新增反馈
     * 
     * @param addReq 反馈
     * @return 结果
     */
    boolean insertFeedback(AddFeedbackReq addReq);

    /**
     * 修改反馈
     * 
     * @param editReq 反馈
     * @return 结果
     */
    boolean replyFeedback(ReplyFeedbackReq editReq);


    /**
     * 关闭反馈信息
     * @param ids 反馈信息id
     * @return
     */
    void closeFeedback(Long[] ids);
}
