package com.ruoyi.system.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * 图片上传的文件对应的业务类型：0-默认 1-图纸图片 2-图纸文件
 *
 * @author tongdashuai
 * @version 1.0
 * @description
 * @module
 * @date 2024/8/5 13:39
 */
@AllArgsConstructor
@Getter
public enum FileBizTypeEnum {

    DEFAULT(0,"默认", "/default"),

    BLUEPRINTS_PICTURE(1,"图纸图片", "/blueprintsPicture") ,

    BLUEPRINTS_FILE(2,"图纸文件", "/blueprintsFile"),

    BLUEPRINTS_PREVIEW_PICTURE(3,"图纸概览图片", "/blueprintsPreviewPicture"),

    BLUEPRINTS_INTRODUCTION_PICTURE(4,"图纸说明图片", "/blueprintsIntroductionPicture"),

    FEEDBACK_PICTURE(5,"反馈图片", "/feedbackPicture"),

    VIP_PICTURE(6,"vip图片", "/vipPicture");

    private Integer code;

    private String name;

    private String dir;

    private static final Map<Integer, FileBizTypeEnum> CACHE = new HashMap<>();

    static {
        for (FileBizTypeEnum bizTypeEnum : values()) {
            CACHE.put(bizTypeEnum.getCode(), bizTypeEnum);
        }
    }

    public static FileBizTypeEnum getByCode(Integer code) {
        return CACHE.get(code) == null ? DEFAULT : CACHE.get(code);
    }
}
