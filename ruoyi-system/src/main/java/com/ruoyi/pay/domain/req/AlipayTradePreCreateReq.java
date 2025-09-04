package com.ruoyi.pay.domain.req;

import com.ruoyi.pay.domain.dto.PayQrLinkInfoDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 支付宝扫码支付-统一收单线下交易预创建请求.
 *
 * @author tongdashuai
 * @version 1.0
 * @description
 * @module
 * @date 2025/3/24 14:35
 */
@Data
@NoArgsConstructor
public class AlipayTradePreCreateReq implements Serializable {


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

    public AlipayTradePreCreateReq(PayQrLinkInfoDTO payInfo) {
        this.vipId = payInfo.getVipId();
        this.blueprintsId = payInfo.getBlueprintsId();
        this.goodsType = payInfo.getGoodsType();
        this.goodsNum = payInfo.getGoodsNum();
        this.userId = payInfo.getUserId();
    }
}
