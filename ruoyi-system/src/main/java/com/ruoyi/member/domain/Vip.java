package com.ruoyi.member.domain;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.*;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * vip对象 vip
 * 
 * @author tds
 * @date 2025-03-13
 */
@TableName("vip")
@Data
@NoArgsConstructor
public class Vip implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 名称 */
    private String name;

    /** 状态 */
    private Integer status;

    /** 图片 */
    private String imageUrl;

    /**
     * 图标图片
     */
    private String iconImageUrl;

    /** 颜色 */
    private String color;

    /** 备注 */
    private String remark;

    /** 有效天数 */
    private Long validDays;

    /** 折扣 */
    private BigDecimal discount;

    /** 排序 */
    private Long orderNum;

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


    /** 价格 */
    private BigDecimal price;

}
