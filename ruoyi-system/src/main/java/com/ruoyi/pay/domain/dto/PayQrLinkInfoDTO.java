package com.ruoyi.pay.domain.dto;

import com.ruoyi.pay.domain.req.PayQrLinkReq;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 聚合支付二维码缓存信息.
 *
 * @author tongdashuai
 * @version 1.0
 * @description
 * @module
 * @date 2025/3/24 14:35
 */
@Data
@NoArgsConstructor
public class PayQrLinkInfoDTO implements Serializable {


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
     * 订单号
     */
    private String orderNo;

    public PayQrLinkInfoDTO(PayQrLinkReq request) {
        this.blueprintsId = request.getBlueprintsId();
        this.goodsNum = request.getGoodsNum();
        this.goodsType = request.getGoodsType();
        this.userId = request.getUserId();
        this.vipId = request.getVipId();
    }
}
