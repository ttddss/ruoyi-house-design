package com.ruoyi.pay.domain.req;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 微信支付jasapi支付下单请求.
 *
 * @author tongdashuai
 * @version 1.0
 * @description
 * @module
 * @date 2025/3/24 14:35
 */
@Data
public class WxpayJsapiPayReq implements Serializable {


    private static final long serialVersionUID = 718975053997804786L;

    /** vipID */
    private Long vipId;

    /** 图纸ID */
    private Long blueprintsId;

    /** 商品类型：0-vip 1-图纸 */
    @NotNull(message = "商品类型不能为空")
    private Integer goodsType;

    /** 商品数量 */
    @NotNull(message = "商品数量不能为空")
    private Integer goodsNum;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 用户在appid下的唯一标识。 下单前需获取到用户的Openid。
     * 如果传了子商户appid，那么openid对应子商户appid对应的openid；如果没传子商户appid，那么对应服务商的appid对应的openid
     */
    @NotBlank(message = "openid不能为空")
    private String openid;

}
