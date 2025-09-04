package com.ruoyi.common.utils;

import lombok.extern.slf4j.Slf4j;

/**
 *
 *
 * @author tongdashuai
 * @version 1.0
 * @description
 * @module
 * @date 2024/8/9 16:53
 */
@Slf4j
public class SmsUtils {




    /**
     * 发送短信
     *
     * @param mobile 手机号
     * @param ip 客户端ip。非必需
     * @return
     */
    public static String sendSmsCode(String mobile, String ip) {

        return null;
    }

    public static void main(String[] args) {
        String code = sendSmsCode("15755507531", "127.0.0.1");
        System.out.println("验证码:" + code);
    }
}
