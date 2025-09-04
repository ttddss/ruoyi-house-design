package com.ruoyi.system.domain.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import com.ruoyi.common.annotation.Excel;
import java.io.Serializable;
import java.util.Date;

/**
 * 支付配置集合vo对象
 * 
 * @author tds
 * @date 2025-03-21
 */
@Data
public class PayConfigListVO implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    @Excel(name = "主键")
    private Long id;

    /** 配置名称 */
    @Excel(name = "配置名称")
    private String name;

    /** 支付渠道 */
    @Excel(name = "支付渠道")
    private Integer channel;

    /**
     * 子支付渠道
     */
    private Integer subChannel;

    /** 应用id */
    @Excel(name = "应用id")
    private String appid;

    /** 应用名称 */
    @Excel(name = "应用名称")
    private String appname;

    /** 商户号 */
    @Excel(name = "商户号")
    private String mchid;

    /** 支付宝应用类型 */
    @Excel(name = "支付宝应用类型")
    private Integer aliType;

    /** 微信支付应用类型 */
    @Excel(name = "微信支付应用类型")
    private Integer wxType;

    /** 公钥 */
    @Excel(name = "公钥")
    private String publicKey;

    /** 私钥 */
    @Excel(name = "私钥")
    private String privateKey;

    /** 开发者密钥 */
    @Excel(name = "开发者密钥")
    private String appSecret;

    /** 创建人 */
    private String createBy;

    /** 创建时间 */
    private Date createTime;

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
