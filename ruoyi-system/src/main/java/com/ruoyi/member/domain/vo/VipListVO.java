package com.ruoyi.member.domain.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import com.ruoyi.common.annotation.Excel;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * vip集合vo对象
 * 
 * @author tds
 * @date 2025-03-13
 */
@Data
public class VipListVO implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    @Excel(name = "id")
    private Long id;

    /** 名称 */
    @Excel(name = "名称")
    private String name;

    /** 状态 */
    @Excel(name = "状态")
    private Integer status;

    /** 图片 */
    @Excel(name = "图片")
    private String imageUrl;

    /** 颜色 */
    @Excel(name = "颜色")
    private String color;

    /** 备注 */
    @Excel(name = "备注")
    private String remark;

    /** 有效天数 */
    @Excel(name = "有效天数")
    private Integer validDays;

    /** 折扣 */
    @Excel(name = "折扣")
    private BigDecimal discount;

    /** 排序 */
    @Excel(name = "排序")
    private Integer orderNum;

    /** 创建人 */
    @Excel(name = "创建人")
    private String createBy;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date createTime;

    /** 价格 */
    private BigDecimal price;

    /**
     * 图标图片
     */
    private String iconImageUrl;


}
