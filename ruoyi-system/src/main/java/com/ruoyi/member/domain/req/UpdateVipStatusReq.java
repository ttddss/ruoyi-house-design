package com.ruoyi.member.domain.req;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 修改vip对象
 * 
 * @author tds
 * @date 2025-03-13
 */
@Data
public class UpdateVipStatusReq implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    @NotNull(message = "主键不能为空")
    private Long id;

    /** 状态 */
    @NotNull(message = "状态不能为空")
    private Integer status;

}
