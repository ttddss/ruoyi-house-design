package com.ruoyi.system.domain.req;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * 新增支付配置对象
 * 
 * @author tds
 * @date 2025-03-21
 */
@Data
public class AddPayConfigReq implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 配置名称 */
    @NotBlank(message = "配置名称不能为空")
    private String name;

    /** 支付渠道 */
    @NotNull(message = "支付渠道不能为空")
    private Integer channel;

    /**
     * 子支付渠道
     */
    @NotNull(message = "子支付渠道不能为空")
    private Integer subChannel;

    /** 应用id */
    private String appid;

    /** 应用名称 */
    private String appname;

    /** 商户号 */
    private String mchid;

    /** 支付宝应用类型 */
    private Integer aliType;

    /** 微信支付应用类型 */
    private Integer wxType;

    /** 公钥 */
    private String publicKey;

    /** 私钥 */
    private String privateKey;

    /** 开发者密钥 */
    private String appSecret;

    /** 备注 */
    private String remark;

    /**
     * 网关地址
     */
    private String gatewayUrl;

    /**
     * 序列号
     */
    private String serialNum;

    /**
     * 失效时间
     */
    private Date expireTime;

    /**
     * aliv3密钥
     */
    private String apiKey;

}
