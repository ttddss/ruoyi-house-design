package com.ruoyi.pay.domain.res;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 微信jsapi查询订单信息响应结果.
 *
 * @author tongdashuai
 * @version 1.0
 * @description
 * @module
 * @date 2025/3/24 14:35
 */
@NoArgsConstructor
@Data
public class WxpayQueryOrderRes implements Serializable {

    private static final long serialVersionUID = -1912069606248541016L;




    /**
     * 【公众账号ID】商户下单时传入的公众账号ID。
     */
    private String appid;

    /**
     * 【商户号】商户下单时传入的商户号。
     */
    private String mchid;

    /**
     * 【商户订单号】 商户下单时传入的商户系统内部订单号。
     */
    private String out_trade_no;

    /**
     * 【微信支付订单号】 微信支付侧订单的唯一标识，订单支付成功后返回。
     */
    private String transaction_id;

    /**
     * 【交易类型】 返回当前订单的交易类型，枚举值：
     *      JSAPI：公众号支付、小程序支付
     *      NATIVE：Native支付
     *      APP：APP支付
     *      MICROPAY：付款码支付
     *      MWEB：H5支付
     *      FACEPAY：刷脸支付
     */
    private String trade_type;

    /**
     * 【交易状态】 交易状态，详细业务流转状态处理请参考开发指引-订单状态流转图。枚举值：
     *  SUCCESS：支付成功
     *  REFUND：转入退款
     *  NOTPAY：未支付
     *  CLOSED：已关闭
     *  REVOKED：已撤销（仅付款码支付会返回）
     *  USERPAYING：用户支付中（仅付款码支付会返回）
     *  PAYERROR：支付失败（仅付款码支付会返回）
     */
    private String trade_state;

    /**
     * 【交易状态描述】 对交易状态的详细说明。
     */
    private String trade_state_desc;

    /**
     * 【银行类型】 用户支付方式说明，订单支付成功后返回，格式为银行简码_具体类型(DEBIT借记卡/CREDIT信用卡/ECNY数字人民币)，
     * 例如ICBC_DEBIT代表工商银行借记卡，非银行卡支付类型(例如余额/零钱通等)统一为OTHERS
     */
    private String bank_type;

    /**
     * 【商户数据包】商户下单时传入的自定义数据包，用户不可见，长度不超过128字符，若下单传入该参数，则
     * 订单支付成功后此接口和支付成功回调通知以及交易账单中会原样返回；若下单未传该参数，则不会返回。
     */
    private String attach;

    /**
     * 【支付完成时间】
     * 1、定义：用户完成订单支付的时间。该参数在订单支付成功后返回。
     * 2、格式：遵循rfc3339标准格式：yyyy-MM-DDTHH:mm:ss+TIMEZONE。yyyy-MM-DD 表示年月日；T 字符用于分隔日期和时间部分；HH:mm:ss 表示具体的时分秒；TIMEZONE 表示时区（例如，+08:00 对应东八区时间，即北京时间）。
     * 示例：2015-05-20T13:29:35+08:00 表示北京时间2015年5月20日13点29分35秒。
     */
    private String success_time;

    /**
     * 【支付者信息】 订单的支付者信息，订单支付成功后返回。
     */
    private PayerBean payer;

    /**
     * 【订单金额】 订单金额信息。
     */
    private AmountBean amount;

    /**
     * 【场景信息】 下单时传入的支付场景描述，若下单传入该参数，则原样返回；若下单未传该参数，则不会返回。
     */
    private SceneInfoBean scene_info;

    /**
     * 【优惠功能】 代金券信息，当订单支付时，有使用代金券时，该字段将返回所使用的代金券信息。
     */
    private List<PromotionDetailBean> promotion_detail;

    @NoArgsConstructor
    @Data
    public static class PayerBean {

        /**
         * 【用户标识】用户在商户下单的appid下唯一标识。
         */
        private String openid;
    }

    @NoArgsConstructor
    @Data
    public static class AmountBean {

        /**
         * 【总金额】 订单总金额，单位为分，整型。
         */
        private int total;

        /**
         * 【用户支付金额】用户实际支付金额，整型，单位为分，用户支付金额=总金额-代金券金额。
         */
        private int payer_total;

        /**
         * 【货币类型】固定返回：CNY，代表人民币。
         */
        private String currency;

        /**
         * 【用户支付币种】 订单支付成功后固定返回：CNY，代表人民币。
         */
        private String payer_currency;
    }

    @NoArgsConstructor
    @Data
    public static class SceneInfoBean {

        /**
         * 【商户端设备号】 商户下单时传入的商户端设备号（门店号或收银设备ID）。
         */
        private String device_id;
    }

    @NoArgsConstructor
    @Data
    public static class PromotionDetailBean {


        private String coupon_id;
        private String name;
        private String scope;
        private String type;
        private int amount;
        private String stock_id;
        private int wechatpay_contribute;
        private int merchant_contribute;
        private int other_contribute;
        private String currency;
        private List<GoodsDetailBean> goods_detail;

        @NoArgsConstructor
        @Data
        public static class GoodsDetailBean {


            private String goods_id;
            private int quantity;
            private int unit_price;
            private int discount_amount;
            private String goods_remark;
        }
    }
}
