package com.ruoyi.blueprints.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 图纸列表集合vo对象
 * 
 * @author tds
 * @date 2025-03-03
 */
@Data
public class MallBlueprintsListVO implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 图纸编码 */
    private String code;

    /** 图纸名称 */
    private String name;

    /** 简介 */
    private String introduction;

    /** 图纸图片 */
    private String imageUrl;

    /** 创建人 */
    private String createBy;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 浏览量
     */
    private Integer viewNum;

    /** 单价 */
    private BigDecimal price;

    /**
     * 销售量
     */
    private Integer saleNum;

}
