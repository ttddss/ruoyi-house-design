package com.ruoyi.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import com.baomidou.mybatisplus.annotation.*;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.util.Date;

/**
 * 反馈对象 feedback
 * 
 * @author tds
 * @date 2025-03-12
 */
@TableName("feedback")
@Data
@NoArgsConstructor
public class Feedback implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 用户id */
    private Long userId;

    /** 联系方式 */
    private String contactInfo;

    /** 反馈状态：0-待处理 1-已解决 2-已关闭 */
    private Integer status;

    /** 反馈图片 */
    private String imageUrl;

    /** 反馈内容 */
    private String content;

    /** 管理员用户id */
    private Long responseUserId;

    /** 管理员回复内容 */
    private String response;

    /** 管理员回复时间 */
    private Date responseTime;

    /** 创建人 */
    private String createBy;

    /** 创建时间 */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /** 更新人 */
    private String updateBy;

    /** 更新时间 */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /** 删除标志: 0-代表存在 其他代表删除 */
    private Long delFlag;




}
