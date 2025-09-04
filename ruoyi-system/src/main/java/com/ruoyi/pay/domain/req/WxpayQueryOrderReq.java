package com.ruoyi.pay.domain.req;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 微信查询订单请求.
 *
 * @author tongdashuai
 * @version 1.0
 * @description
 * @module
 * @date 2025/3/24 14:35
 */
@Data
public class WxpayQueryOrderReq implements Serializable {


    private static final long serialVersionUID = 718975053997804786L;



    /**
     * 订单号
     */
    @NotBlank(message = "订单号不能为空")
    private String orderNo;

}
