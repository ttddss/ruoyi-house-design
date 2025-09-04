package com.ruoyi.blueprints.domain.req;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 修改图纸列表对象
 * 
 * @author tds
 * @date 2025-03-03
 */
@Data
public class ChangeBlueprintsListingStatusReq implements Serializable
{

    private static final long serialVersionUID = 3885421232276499503L;

    /** 主键 */
    @NotNull(message = "id不能为空")
    private Long id;


    /** 上架状态：0-已下架 1-已上架 */
    @NotNull(message = "上架状态不能为空")
    private Integer listingStatus;



}
