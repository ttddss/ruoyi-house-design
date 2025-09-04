package com.ruoyi.blueprints.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 图纸购买前返回vo对象
 * 
 * @author tds
 * @date 2025-03-03
 */
@Data
@Builder
public class MallBuyPreVO implements Serializable
{
    private static final long serialVersionUID = 1L;

    /**
     * 实付金额
     */
    private BigDecimal actualAmount;

    /**
     * 优惠金额
     */
    private BigDecimal discountAmount;

}
