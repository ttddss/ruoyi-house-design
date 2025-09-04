package com.ruoyi.system.domain.vo;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import lombok.Data;
import java.io.Serializable;
import java.util.Date;

/**
 * 反馈vo对象
 * 
 * @author tds
 * @date 2025-03-12
 */
@Data
public class FeedbackInfoVO implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 管理员用户账号 */
    private String responseUsername;

    /** 联系方式 */
    private String contactInfo;

    /** 反馈状态：0-待处理 1-已解决 2-已关闭 */
    private Integer status;

    /** 反馈图片 */
    private String imageUrl;

    /** 反馈内容 */
    private String content;

    /** 管理员回复内容 */
    private String response;

    /** 管理员回复时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date responseTime;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd hh:mm:ss")
    private Date createTime;

    /** 创建人 */
    private String createBy;

}
