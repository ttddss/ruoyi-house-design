package com.ruoyi.pay.enums;

import com.ruoyi.order.enums.RefundStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;


/**
 *
 *
 * @author tongdashuai
 * @version 1.0
 * @description 退款状态：
 * 					SUCCESS：退款成功
 * 					CLOSED：退款关闭
 * 					PROCESSING：退款处理中
 * 					ABNORMAL：退款异常
 * @date 2023/3/24 15:12
 */
@Getter
@AllArgsConstructor
public enum WxpayRefundStatusEnum {

	SUCCESS("SUCCESS", "退款成功", RefundStatusEnum.SUCCESS.getCode()),
	CLOSED("CLOSED", "退款关闭", RefundStatusEnum.FAIL.getCode()),
	PROCESSING("PROCESSING", "退款处理中", RefundStatusEnum.REFUNDING.getCode()),
	ABNORMAL("ABNORMAL", "退款异常", RefundStatusEnum.ERROR.getCode());

	private String code;
	private String name;

	private int refundStatus;

	private static Map<String, WxpayRefundStatusEnum> cacheMap = new HashMap<>();

	static {
		for (WxpayRefundStatusEnum item : values()) {
			cacheMap.put(item.getCode(), item);
		}
	}

	public static WxpayRefundStatusEnum valueCodeOf(String code) {
		return cacheMap.get(code);
	}



}
