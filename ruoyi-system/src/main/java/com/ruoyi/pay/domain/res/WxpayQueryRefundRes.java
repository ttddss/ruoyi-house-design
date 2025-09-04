package com.ruoyi.pay.domain.res;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 微信查询退款单响应结果.
 *
 * @author tongdashuai
 * @version 1.0
 * @description
 * @module
 * @date 2025/3/24 14:35
 */
@Data
@NoArgsConstructor
public class WxpayQueryRefundRes implements Serializable {

    private static final long serialVersionUID = -1912069606248541016L;


    /**
     * 【微信支付退款单号】申请退款受理成功时，该笔退款单在微信支付侧生成的唯一标识。
     */
    private String refund_id;

    /**
     * 【商户退款单号】商户申请退款时传入的商户系统内部退款单号。
     */
    private String out_refund_no;

    /**
     * 【微信支付订单号】微信支付侧订单的唯一标识。
     */
    private String transaction_id;

    /**
     * 【商户订单号】 商户下单时传入的商户系统内部订单号。
     */
    private String out_trade_no;

    /**
     * 【退款渠道】 订单退款渠道
     * 以下枚举：
     * ORIGINAL: 原路退款
     * BALANCE: 退回到余额
     * OTHER_BALANCE: 原账户异常退到其他余额账户
     * OTHER_BANKCARD: 原银行卡异常退到其他银行卡(发起异常退款成功后返回)
     */
    private String channel;

    /**
     * 【退款入账账户】 当前退款单的退款入账方，取值有以下几种情况：
     * 1）退回银行卡：{银行名称}{卡类型}{卡尾号}
     * 2）退回支付用户零钱:支付用户零钱
     * 3）退还商户:商户基本账户商户结算银行账户
     * 4）退回支付用户零钱通:支付用户零钱通
     * 5）退回支付用户银行电子账户:支付用户银行电子账户
     * 6）退回支付用户零花钱:支付用户零花钱
     * 7）退回用户经营账户:用户经营账户
     * 8）退回支付用户来华零钱包:支付用户来华零钱包
     * 9）退回企业支付商户:企业支付商户
     */
    private String user_received_account;

    /**
     * 【退款成功时间】
     * 1、定义：退款成功的时间，该字段在退款状态status为SUCCESS（退款成功）时返回。
     * 2、格式：遵循rfc3339标准格式：yyyy-MM-DDTHH:mm:ss+TIMEZONE。yyyy-MM-DD 表示年月日；T 字符用于分隔日期和时间部分；HH:mm:ss 表示具体的时分秒；TIMEZONE 表示时区（例如，+08:00 对应东八区时间，即北京时间）。
     * 示例：2015-05-20T13:29:35+08:00 表示北京时间2015年5月20日13点29分35秒。
     */
    private String success_time;

    /**
     * 【退款创建时间】
     * 1、定义：提交退款申请成功，微信受理退款申请单的时间。
     * 2、格式：遵循rfc3339标准格式：yyyy-MM-DDTHH:mm:ss+TIMEZONE。yyyy-MM-DD 表示年月日；T 字符用于分隔日期和时间部分；HH:mm:ss 表示具体的时分秒；TIMEZONE 表示时区（例如，+08:00 对应东八区时间，即北京时间）。
     * 示例：2015-05-20T13:29:35+08:00 表示北京时间2015年5月20日13点29分35秒。
     */
    private String create_time;

    /**
     * 【退款状态】退款单的退款处理状态。
     * SUCCESS: 退款成功
     * CLOSED: 退款关闭
     * PROCESSING: 退款处理中
     * ABNORMAL: 退款异常，退款到银行发现用户的卡作废或者冻结了，导致原路退款银行卡失败，可前往商户平台-交易中心，手动处理此笔退款，可参考： 退款异常的处理，或者通过发起异常退款接口进行处理。
     * 注：状态流转说明请参考状态流转图
     */
    private String status;

    /**
     * 【资金账户】 退款所使用资金对应的资金账户类型
     * UNSETTLED: 未结算资金
     * AVAILABLE: 可用余额
     * UNAVAILABLE: 不可用余额
     * OPERATION: 运营账户
     * BASIC: 基本账户（含可用余额和不可用余额）
     * ECNY_BASIC: 数字人民币基本账户
     */
    private String funds_account;

    /**
     * 【金额信息】订单退款金额信息
     */
    private AmountBean amount;

    /**
     * 【优惠退款详情】 订单各个代金券的退款详情，订单使用了代金券且代金券发生退款时返回。
     */
    private List<PromotionDetailBean> promotion_detail;

    @NoArgsConstructor
    @Data
    public static class AmountBean {


        private int total;
        private int refund;
        private int payer_total;
        private int payer_refund;
        private int settlement_refund;
        private int settlement_total;
        private int discount_refund;
        private String currency;
        private int refund_fee;
        private List<FromBean> from;

        @NoArgsConstructor
        @Data
        public static class FromBean {


            private String account;
            private int amount;
        }
    }

    @NoArgsConstructor
    @Data
    public static class PromotionDetailBean {


        private String promotion_id;
        private String scope;
        private String type;
        private int amount;
        private int refund_amount;
        private List<GoodsDetailBean> goods_detail;

        @NoArgsConstructor
        @Data
        public static class GoodsDetailBean {


            private String merchant_goods_id;
            private String wechatpay_goods_id;
            private String goods_name;
            private int unit_price;
            private int refund_amount;
            private int refund_quantity;
        }
    }
}
