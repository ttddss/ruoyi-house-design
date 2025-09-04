package com.ruoyi.pay.enums;

import com.ruoyi.order.enums.PayStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;


/**
 *
 *
 * @author tongdashuai
 * @version 1.0
 * @description 交易状态，枚举值：
 * 					SUCCESS：支付成功
 * 					REFUND：转入退款
 * 					NOTPAY：未支付
 * 					CLOSED：已关闭
 * 					REVOKED：已撤销（仅付款码支付会返回）
 * 					USERPAYING：用户支付中（仅付款码支付会返回）
 * 					PAYERROR：支付失败（仅付款码支付会返回）
 * @date 2023/3/24 15:12
 */
@Getter
@AllArgsConstructor
public enum WxpayTradeStateEnum {

	SUCCESS("SUCCESS", "支付成功", PayStatusEnum.SUCCESS.getCode()),
	REFUND("REFUND", "转入退款", PayStatusEnum.REFUNDED.getCode()),
	NOTPAY("NOTPAY", "未支付", PayStatusEnum.PAYING.getCode()),
	CLOSED("CLOSED", "已关闭", PayStatusEnum.CLOSE.getCode()),
	REVOKED("REVOKED", "已撤销（仅付款码支付会返回）", PayStatusEnum.CANCEL.getCode()),
	USERPAYING("USERPAYING", "用户支付中（仅付款码支付会返回）", PayStatusEnum.PAYING.getCode()),
	PAYERROR("PAYERROR", "支付失败（仅付款码支付会返回）", PayStatusEnum.ERROR.getCode());

	private String code;
	private String name;

	/**
	 * PayOrder的status
	 */
	private int orderStatus;

	private static Map<String, WxpayTradeStateEnum> cacheMap = new HashMap<>();

	static {
		for (WxpayTradeStateEnum item : values()) {
			cacheMap.put(item.getCode(), item);
		}
	}

	public static WxpayTradeStateEnum valueCodeOf(String code) {
		return cacheMap.get(code);
	}



}
