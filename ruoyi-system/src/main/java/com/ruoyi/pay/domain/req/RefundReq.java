package com.ruoyi.pay.domain.req;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 退款请求.
 *
 * @author tongdashuai
 * @version 1.0
 * @description
 * @module
 * @date 2025/3/24 14:35
 */
@Data
public class RefundReq implements Serializable {


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
