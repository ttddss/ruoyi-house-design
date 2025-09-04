package com.ruoyi.web.controller.system;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.system.domain.req.QueryFeedbackListReq;
import com.ruoyi.system.domain.req.ReplyFeedbackReq;
import com.ruoyi.system.domain.vo.FeedbackInfoVO;
import com.ruoyi.system.domain.vo.FeedbackListVO;
import com.ruoyi.system.service.IFeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 反馈Controller
 * 
 * @author tds
 * @date 2025-03-12
 */
@RestController
@RequestMapping("/system/feedback")
public class FeedbackController extends BaseController
{
    @Autowired
    private IFeedbackService feedbackService;

    /**
     * 查询反馈列表
     */
    @PreAuthorize("@ss.hasPermi('system:feedback:list')")
    @GetMapping("/list")
    public TableDataInfo<List<FeedbackListVO>> list(QueryFeedbackListReq queryReq)
    {
        startPage();
        List<FeedbackListVO> list = feedbackService.selectFeedbackList(queryReq);
        return getDataTable(list);
    }



    /**
     * 获取反馈详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:feedback:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult<FeedbackInfoVO> getInfo(@PathVariable("id") Long id)
    {
        return success(feedbackService.selectFeedbackById(id));
    }

    /**
     * 反馈回复
     */
    @PreAuthorize("@ss.hasPermi('system:feedback:reply')")
    @PostMapping(value = "/reply")
    public AjaxResult replyFeedback(@RequestBody @Validated ReplyFeedbackReq replyReq)
    {
        return success(feedbackService.replyFeedback(replyReq));
    }

    /**
     * 关闭反馈信息
     */
    @PreAuthorize("@ss.hasPermi('system:feedback:close')")
    @PostMapping(value = "/close/{ids}")
    public AjaxResult closeFeedback(@PathVariable("ids") Long[] ids)
    {
        feedbackService.closeFeedback(ids);
        return success();
    }

}
