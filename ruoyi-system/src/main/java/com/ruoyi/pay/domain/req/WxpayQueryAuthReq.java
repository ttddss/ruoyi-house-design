package com.ruoyi.pay.domain.req;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 微信获取授权信息请求.
 *
 * @author tongdashuai
 * @version 1.0
 * @description
 * @module
 * @date 2025/3/24 14:35
 */
@Data
public class WxpayQueryAuthReq implements Serializable {


    private static final long serialVersionUID = 718975053997804786L;


    /**
     * 授权code码
     */
    @NotBlank(message = "授权码不能为空")
    private String code;

    /**
     * 应用id
     */
    @NotBlank(message = "微信appid不能为空")
    private String wxAppid;


}
