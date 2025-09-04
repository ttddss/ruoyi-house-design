package com.ruoyi.member.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * vipvo对象
 * 
 * @author tds
 * @date 2025-03-13
 */
@Data
public class VipInfoVO implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
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
    private Integer validDays;

    /** 折扣 */
    private BigDecimal discount;

    /** 排序 */
    private Integer orderNum;

    /** 创建人 */
    private String createBy;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createTime;

    /** 价格 */
    private BigDecimal price;

}
