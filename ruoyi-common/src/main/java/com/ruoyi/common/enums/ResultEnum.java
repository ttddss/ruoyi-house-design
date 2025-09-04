package com.ruoyi.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 通用响应结果
 *
 * 统一错误码规则：
 * 000000-099999：系统级响应码
 * 100000-299999：业务级响应码
 * 300000-399999: 第三方级响应码
 * 400000-499999: 自定义级响应码
 * 500000-xxxxxx: 保留级响应码
 *
 * 注：排除掉http的状态码，详见HttpStatus枚举。部分报错会返回http状态码，所以业务错误码中尽量排除掉这些编码
 *
 *
 * @author tongdashuai
 * @version 1.0
 * @description
 * @module
 * @date 2024/8/5 13:39
 */
@AllArgsConstructor
@Getter
public enum ResultEnum {

    // =========================== 系统级响应码 ==============================
    BIZ_ERROR(1, "000001", "业务异常"),

    // =========================== 业务级响应码 ==============================

    ORDER_IS_CLOSED(100000, "100000", "订单已关闭"),

    ORDER_IS_CANCELED(100001,"100001", "订单已取消")


    ;

    private Integer code;

    private String scode;

    private String name;
}
