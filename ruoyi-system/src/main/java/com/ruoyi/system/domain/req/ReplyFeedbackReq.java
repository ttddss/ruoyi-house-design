package com.ruoyi.system.domain.req;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 反馈回复对象
 * 
 * @author tds
 * @date 2025-03-12
 */
@Data
public class ReplyFeedbackReq implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    @NotNull(message = "id不能为空")
    private Long id;


    /** 管理员回复内容 */
    @NotBlank(message = "回复内容不能为空")
    private String response;


}
