package com.ruoyi.system.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * 系统配置枚举
 *
 * @author tongdashuai
 * @version 1.0
 * @description
 * @module
 * @date 2024/8/5 13:39
 */
@AllArgsConstructor
@Getter
public enum ConfigEnum {

    CUSTOMER_SERVICE_PHONE("sys.customerservice.phone","客服电话"),

    CUSTOMER_SERVICE_WX("sys.customerservice.wx","客服微信") ,

    CUSTOMER_SERVICE_QQ("sys.customerservice.qq","客服qq"),

    CUSTOMER_SERVICE_WORK_TIME("sys.customerservice.worktime","客服工作时间"),

    File_UPLOAD_WAY("sys.fileUploadWay", "文件上传方式：0-本地 1-阿里云oss");

    private String code;

    private String name;



    private static final Map<String, ConfigEnum> CACHE = new HashMap<>();

    static {
        for (ConfigEnum bizTypeEnum : values()) {
            CACHE.put(bizTypeEnum.getCode(), bizTypeEnum);
        }
    }

    public static ConfigEnum getByCode(String code) {
        return CACHE.get(code);
    }
}
