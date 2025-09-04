package com.ruoyi.member.domain.req;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 修改vip对象
 * 
 * @author tds
 * @date 2025-03-13
 */
@Data
public class EditVipReq implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    @NotNull(message = "主键不能为空")
    private Long id;


    /** 名称 */
    @NotBlank(message = "名称不能为空")
    private String name;

    /** 背景图片 */
    private String imageUrl;

    /** 图标图片 */
    private String iconImageUrl;

    /** 颜色 */
    @NotBlank(message = "颜色不能为空")
    private String color;

    /** 备注 */
    @NotBlank(message = "备注不能为空")
    private String remark;

    /** 有效天数 */
    @NotNull(message = "有效天数不能为空")
    private Integer validDays;

    /** 折扣 */
    @NotNull(message = "折扣不能为空")
    private BigDecimal discount;

    /** 价格 */
    @NotNull(message = "价格不能为空")
    @Min(value = 0, message = "价格最小为0")
    private BigDecimal price;

    /** 排序 */
    @NotNull(message = "排序不能为空")
    private Integer orderNum;

}
