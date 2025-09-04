package com.ruoyi.pay.domain;

import lombok.Data;

/**
 *
 *
 * @author tongdashuai
 * @version 1.0
 * @description apiv3返回给微信的通用应答信息
 * @module
 * @date 2023/3/24 17:17
 */
@Data
public class WxResult {

    /**
     * 错误码，SUCCESS为清算机构接收成功，其他错误码为失败。
     * 示例值：FAIL
     */
    private String code;

    /**
     * 返回信息，如非空，为错误原因。
     * 示例值：失败
     */
    private String message;

    /**
     * 成功码
     */
    public static final String SUCCESS_CODE = "SUCCESS";

    /**
     * 通用失败码
     */
    public static final String FAIL_CODE = "FAIL";


    public static WxResult success() {
        WxResult wxResult = new WxResult();
        wxResult.setCode(SUCCESS_CODE);
        wxResult.setMessage("成功");
        return wxResult;
    }

    public static WxResult fail(String message) {
        WxResult wxResult = new WxResult();
        wxResult.setCode(FAIL_CODE);
        wxResult.setMessage(message);
        return wxResult;
    }

}
