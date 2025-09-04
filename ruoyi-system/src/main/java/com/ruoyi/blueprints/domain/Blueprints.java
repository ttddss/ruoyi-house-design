package com.ruoyi.blueprints.domain;

import java.math.BigDecimal;
import lombok.Data;
import com.baomidou.mybatisplus.annotation.*;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.util.Date;

/**
 * 图纸列表对象 blueprints
 * 
 * @author tds
 * @date 2025-03-03
 */
@TableName("blueprints")
@Data
@NoArgsConstructor
public class Blueprints implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户id
     */
    private Long userId;

    /** 图纸编码 */
    private String code;

    /** 图纸名称 */
    private String name;

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


    /** 删除标志: 0-代表存在 其他代表删除 */
    private Long delFlag;

    /** 备注 */
    private String remark;

    /** 上架状态：0-已下架 1-已上架 */
    private Integer listingStatus;

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
