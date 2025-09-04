package com.ruoyi.pay.domain.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 *
 *
 * @author tongdashuai
 * @version 1.0
 * @description
 * @module
 * @date 2025/3/25 18:08
 */
@Data
@Builder
public class CalculateGoodsDTO {

    /** vipID */
    private Long vipId;

    /** 图纸ID */
    private Long blueprintsId;

    /** 商品数量 */
    private Integer goodsNum;

    private Long userId;
}
