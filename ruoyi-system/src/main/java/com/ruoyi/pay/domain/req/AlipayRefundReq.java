package com.ruoyi.pay.domain.req;

import lombok.Data;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 支付宝退款请求.
 *
 * @author tongdashuai
 * @version 1.0
 * @description
 * @module
 * @date 2025/3/24 14:35
 */
@Data
public class AlipayRefundReq implements Serializable {


    private static final long serialVersionUID = 718975053997804786L;


    /**
     * 支付单号
     */
    @NotBlank(message = "支付单号不能为空")
    private String payOrderNo;


    /**
     * 退款描述信息
     */
    private String description;


}
