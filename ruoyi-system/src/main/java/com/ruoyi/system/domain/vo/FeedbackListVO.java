package com.ruoyi.system.domain.vo;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import com.ruoyi.common.annotation.Excel;
import java.io.Serializable;
import java.util.Date;

/**
 * 反馈集合vo对象
 * 
 * @author tds
 * @date 2025-03-12
 */
@Data
public class FeedbackListVO implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 创建者 */
    @Excel(name = "创建者")
    private String createBy;

    /** 联系方式 */
    @Excel(name = "联系方式")
    private String contactInfo;

    /** 反馈图片 */
    private String imageUrl;

    /** 反馈状态：0-待处理 1-已解决 2-已关闭 */
    @Excel(name = "反馈状态")
    private Integer status;

    /** 管理员用户账号 */
    @Excel(name = "回复人")
    private String responseUsername;

    /** 管理员回复时间 */
    @Excel(name = "回复时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date responseTime;


    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd hh:mm:ss")
    private Date createTime;

}
