package com.ruoyi.blueprints.domain;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.*;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.util.Date;

/**
 * 用户设计图纸收藏对象 blueprints_collect
 * 
 * @author tds
 * @date 2025-03-03
 */
@TableName("blueprints_collect")
@Data
@NoArgsConstructor
public class BlueprintsCollect implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 用户ID */
    private Long userId;

    /** 图纸ID */
    private Long blueprintsId;

    /** 删除标志: 0-代表存在 其他代表删除 */
    private Long delFlag;

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
