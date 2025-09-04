package com.ruoyi.mall.controller.feedback;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.enums.OperatorType;
import com.ruoyi.system.domain.req.AddFeedbackReq;
import com.ruoyi.system.service.IFeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.PermitAll;

/**
 * 商城-图纸相关接口
 * 
 * @author tds
 * @date 2025-03-03
 */
@RestController
@RequestMapping("/mall/feedback")
public class MallFeedbackController extends BaseController
{
    @Autowired
    private IFeedbackService feedbackService;

    /**
     * 提交反馈
     */
    @Log(title = "提交反馈", businessType = BusinessType.INSERT, operatorType = OperatorType.WEB)
    @PermitAll
    @PostMapping
    public AjaxResult submitFeedBack(@RequestBody @Validated AddFeedbackReq addReq) {
        return AjaxResult.success(feedbackService.insertFeedback(addReq));
    }

}
