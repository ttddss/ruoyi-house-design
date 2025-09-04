package com.ruoyi.system.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 业务类型：0-自动订单关闭 1-查询订单状态 2-查询退款单状态
 *
 * @author tongdashuai
 * @version 1.0
 * @description
 * @module
 * @date 2024/8/5 13:39
 */
@AllArgsConstructor
@Getter
public enum MsgBizTypeEnum {

    AUTO_CLOSE(0,"自动订单关闭"),

    QUERY_ORDER_STATUS(1,"查询订单状态"),

    QUERY_REFUND_STATUS(2, "查询退款单状态");

    private Integer code;

    private String name;
}
