package com.ruoyi.blueprints.domain.req;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.io.Serializable;
import java.util.Date;

/**
 * 查询图纸列表对象
 * 
 * @author tds
 * @date 2025-03-03
 */
@Data
public class QueryBlueprintsListReq implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 图纸编码 */
    private String code;

    /** 图纸名称 */
    private String name;

    /** 最小单价 */
    private BigDecimal beginPrice;

    /** 最大单价 */
    private BigDecimal endPrice;

    /** 房型 */
    private Integer houseType;

    /** 室型 */
    private Integer bayType;

    /** 风格 */
    private Integer blueprintsStyle;


    /** 开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date beginCreateTime;

    /** 结束时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date endCreateTime;

    /** 用户ID */
    private Long userId;

    /** 收藏用户ID */
    private Long collectUserId;

    /** 上架状态：0-已下架 1-已上架 */
    private Integer listingStatus;

    /**
     * 排序方式: 0-综合 1-价格升序 2-价格降序 3-销量升序 4-销量降序
     */
    private Integer orderWay;

}
