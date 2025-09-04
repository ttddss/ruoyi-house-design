package com.ruoyi.pay.enums;

import com.ruoyi.order.enums.PayStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;


/**
 *
 * @ClassName AlipayTradeStatusEnum
 * @Description 支付宝交易状态：TRADE_FINISHED-交易完成 TRADE_SUCCESS-支付成功 WAIT_BUYER_PAY-交易创建 TRADE_CLOSED-交易关闭
 * @author tds
 * @Date 2021-03-23 9:35
 * @version 1.0.0
 */
@Getter
@AllArgsConstructor
public enum AlipayTradeStatusEnum {

	TRADE_FINISHED("TRADE_FINISHED", "交易完成", PayStatusEnum.SUCCESS.getCode()),
	TRADE_SUCCESS("TRADE_SUCCESS", "支付成功", PayStatusEnum.SUCCESS.getCode()),
	WAIT_BUYER_PAY("WAIT_BUYER_PAY", "交易创建",PayStatusEnum.PAYING.getCode()),
	TRADE_CLOSED("TRADE_CLOSED", "交易关闭", PayStatusEnum.CLOSE.getCode());

	private String code;
	private String name;

	/**
	 * 对应PayOrder的status
	 */
	private int orderStatus;

	private static final Map<String, AlipayTradeStatusEnum> CACHE_MAP = new HashMap<>();

	static {
		for (AlipayTradeStatusEnum item : values()) {
			CACHE_MAP.put(item.getCode(), item);
		}
	}

	public boolean isSuccess() {
		return this.code.equals(TRADE_FINISHED.code) || this.code.equals(TRADE_SUCCESS.code);
	}

	public static AlipayTradeStatusEnum valueCodeOf(String code) {
		return CACHE_MAP.get(code);
	}



}
