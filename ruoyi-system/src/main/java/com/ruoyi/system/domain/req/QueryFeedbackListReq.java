package com.ruoyi.system.domain.req;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.io.Serializable;

/**
 * 查询反馈对象
 * 
 * @author tds
 * @date 2025-03-12
 */
@Data
public class QueryFeedbackListReq implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 反馈状态：0-待处理 1-已解决 2-已关闭 */
    private Integer status;

    /** 创建人 */
    private String createBy;

}
