package com.ruoyi.blueprints.domain;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.*;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.util.Date;

/**
 * 设计图纸详情对象 blueprints_detail
 * 
 * @author tds
 * @date 2025-03-08
 */
@TableName("blueprints_detail")
@Data
@NoArgsConstructor
public class BlueprintsDetail implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 图纸id */
    private Long blueprintsId;

    /** 简介 */
    private String introduction;

    /** 图纸访问地址 */
    private String fileUrl;

    /** 图纸概览图片 */
    private String previewImageUrl;

    /** 图纸说明图片 */
    private String introductionImageUrl;

    /** 删除标志: 0-代表存在 其他代表删除 */
    private Long delFlag;

    /** 备注 */
    private String remark;

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




}
