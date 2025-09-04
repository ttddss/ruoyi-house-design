package com.ruoyi.pay.domain.vo;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 获取聚合支付二维码请求.
 *
 * @author tongdashuai
 * @version 1.0
 * @description
 * @module
 * @date 2025/3/24 14:35
 */
@Data
@Builder
public class PayQrLinkVO implements Serializable {


    private static final long serialVersionUID = 718975053997804786L;



    /** 二维码标识 */
    private String qrLinkId;

    /** 二维码链接 */
    private String qrLink;

}
