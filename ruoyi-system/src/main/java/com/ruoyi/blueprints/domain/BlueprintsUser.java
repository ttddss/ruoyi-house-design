package com.ruoyi.blueprints.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户图纸对象 blueprints_user
 * 
 * @author tds
 * @date 2025-03-26
 */
@TableName("blueprints_user")
@Data
@NoArgsConstructor
public class BlueprintsUser implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 图纸id */
    private Long blueprintsId;

    /** 用户ID */
    private Long userId;

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
