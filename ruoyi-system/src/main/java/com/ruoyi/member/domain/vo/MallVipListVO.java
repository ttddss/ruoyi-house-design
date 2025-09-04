package com.ruoyi.member.domain.vo;

import lombok.Data;

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
public class MallVipListVO implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 名称 */
    private String name;


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

    /** 价格 */
    private BigDecimal price;

    /** 用户id */
    private Long userId;

    /**
     * 到期时间
     */
    private Date expireTime;

}
