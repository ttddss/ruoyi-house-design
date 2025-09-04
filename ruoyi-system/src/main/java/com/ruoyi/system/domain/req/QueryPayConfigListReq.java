package com.ruoyi.system.domain.req;

import lombok.Data;
import java.io.Serializable;

/**
 * 查询支付配置对象
 * 
 * @author tds
 * @date 2025-03-21
 */
@Data
public class QueryPayConfigListReq implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 配置名称 */
    private String name;

    /** 支付渠道 */
    private Integer channel;

    /** 子支付渠道 */
    private Integer subChannel;

}
