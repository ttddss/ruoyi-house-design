package com.ruoyi.pay.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;


/**
 *
 *
 * @author tongdashuai
 * @version 1.0
 * @description 支付宝账单类型.
 * @date 2023/3/17 8:58
 */
@Getter
@AllArgsConstructor
public enum AlipayBillTypeEnum {

	TRADE("trade", "商户基于支付宝交易收单的业务账单"),

	SIGNCUSTOMER("signcustomer", "基于商户支付宝余额收入及支出等资金变动的账务账单"),

	MERCHANT_ACT("merchant_act", "营销活动账单，包含营销活动的发放，核销记录"),

	TRADE_ZFT_MERCHANT("trade_zft_merchant", "直付通二级商户查询交易的业务账单"),

	ZFT_ACC("zft_acc", "直付通平台商查询二级商户流水使用，返回所有二级商户流水");

	private String code;
	private String name;

}
