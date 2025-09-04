package com.ruoyi.blueprints.domain;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.*;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.util.Date;

/**
 * 图纸浏览记录对象 blueprints_view
 * 
 * @author tds
 * @date 2025-03-09
 */
@TableName("blueprints_view")
@Data
@NoArgsConstructor
public class BlueprintsView implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 图纸id */
    private Long blueprintsId;

    /** 用户id */
    private Long userId;

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
