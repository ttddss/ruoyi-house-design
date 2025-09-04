package com.ruoyi.blueprints.domain.req;

import java.math.BigDecimal;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.util.Date;

/**
 * 新增图纸列表对象
 * 
 * @author tds
 * @date 2025-03-03
 */
@Data
public class AddBlueprintsReq implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 图纸编码 */
    private String code;

    /** 图纸名称 */
    private String name;

    /** 简介 */
    private String introduction;

    /** 图纸图片 */
    private String imageUrl;

    /** 单价 */
    private BigDecimal price;

    /** 最小造价(单位万元) */
    private BigDecimal minConstructionCost;

    /** 最大造价(单位万元) */
    private BigDecimal maxConstructionCost;

    /** 最小可建尺寸(单位米) */
    private BigDecimal minBuildableSize;

    /** 最大可建尺寸(单位米) */
    private BigDecimal maxBuildableSize;

    /** 开间尺寸(单位米) */
    private BigDecimal baySize;

    /** 进深尺寸(单位米) */
    private BigDecimal depthSize;

    /** 房型 */
    private Integer houseType;

    /** 室型 */
    private Integer bayType;

    /** 风格 */
    private Integer style;

    /** 卧室数 */
    private Integer bedroomSize;

    /** 客厅数 */
    private Integer parlourSize;

    /** 卫生间数 */
    private Integer toiletSize;

    /** 图纸访问地址 */
    private String fileUrl;

    /** 图纸概览图片 */
    private String previewImageUrl;

    /** 图纸说明图片 */
    private String introductionImageUrl;

}
