package com.ruoyi.blueprints.domain.vo;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import com.ruoyi.common.annotation.Excel;
import java.io.Serializable;
import java.util.Date;

/**
 * 图纸列表集合vo对象
 * 
 * @author tds
 * @date 2025-03-03
 */
@Data
public class BlueprintsListVO implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 图纸编码 */
    @Excel(name = "图纸编码")
    private String code;

    /** 图纸名称 */
    @Excel(name = "图纸名称")
    private String name;

    /** 简介 */
    @Excel(name = "简介")
    private String introduction;

    /** 图纸图片 */
    @Excel(name = "图纸图片")
    private String imageUrl;

    /** 单价 */
    @Excel(name = "单价")
    private BigDecimal price;

    /** 最小造价(单位万元) */
    @Excel(name = "最小造价(单位万元)")
    private BigDecimal minConstructionCost;

    /** 最大造价(单位万元) */
    @Excel(name = "最大造价(单位万元)")
    private BigDecimal maxConstructionCost;

    /** 最小可建尺寸(单位米) */
    @Excel(name = "最小可建尺寸(单位米)")
    private BigDecimal minBuildableSize;

    /** 最大可建尺寸(单位米) */
    @Excel(name = "最大可建尺寸(单位米)")
    private BigDecimal maxBuildableSize;

    /** 开间尺寸(单位米) */
    @Excel(name = "开间尺寸(单位米)")
    private BigDecimal baySize;

    /** 进深尺寸(单位米) */
    @Excel(name = "进深尺寸(单位米)")
    private BigDecimal depthSize;

    /** 房型 */
    @Excel(name = "房型")
    private Integer houseType;

    /** 室型 */
    @Excel(name = "室型")
    private Integer bayType;

    /** 风格 */
    @Excel(name = "风格")
    private Integer style;

    /** 卧室数 */
    @Excel(name = "卧室数")
    private Integer bedroomSize;

    /** 客厅数 */
    @Excel(name = "客厅数")
    private Integer parlourSize;

    /** 卫生间数 */
    @Excel(name = "卫生间数")
    private Integer toiletSize;

    /** 图纸访问地址 */
    @Excel(name = "图纸访问地址")
    private String fileUrl;

    /** 创建人 */
    @Excel(name = "创建人")
    private String createBy;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /** 用户ID */
    private Long userId;

    /** 上架状态：0-已下架 1-已上架 */
    private Integer listingStatus;

    /**
     * 浏览量
     */
    private Integer viewNum;

    /**
     * 收藏人数
     */
    private Integer collectNum;

    /**
     * 销售量
     */
    private Integer saleNum;

}
