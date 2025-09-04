package com.ruoyi.system.domain.req;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 新增反馈对象
 * 
 * @author tds
 * @date 2025-03-12
 */
@Data
public class AddFeedbackReq implements Serializable
{
    private static final long serialVersionUID = 1L;


    /** 联系方式 */
    @NotBlank(message = "联系方式不能为空")
    private String contactInfo;

    /**
     * 反馈图片
     */
    private String imageUrl;

    /** 反馈内容 */
    @NotBlank(message = "反馈内容不能为空")
    @Length(max = 200, message = "反馈内容不能超过200个字符")
    private String content;

}
