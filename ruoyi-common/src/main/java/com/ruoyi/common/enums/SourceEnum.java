package com.ruoyi.common.enums;

import io.jsonwebtoken.impl.crypto.MacProvider;

import java.util.HashMap;
import java.util.Map;

/**
 * 来源
 * 
 * @author tds
 */
public enum SourceEnum
{
    DINGTALK("dingtalk", "钉钉"),
    BAIDU("baidu", "百度"),
    GITHUB("github", "github"),
    GITEE("gitee", "gitee"),
    WEIBO("weibo", "微博"),
    ALIPAY("alipay", "支付宝"),
    QQ("qq", "qq"),
    WECHAT_OPEN("wechat_open", "微信开放平台"),
    WECHAT_MP("wechat_mp", "微信公众平台"),
    ALIYUN("aliyun", "阿里云"),

    WECHAT_MINI("wechat_mini", "微信小程序")

    ;

    private final String code;
    private final String info;

    private static final Map<String, SourceEnum> CACHE_MAP = new HashMap();
    static {
        for (SourceEnum sourceEnum : values()) {
            CACHE_MAP.put(sourceEnum.getCode(), sourceEnum);
        }
    }

    public static SourceEnum getByCode(String code) {
        return CACHE_MAP.get(code);
    }


    SourceEnum(String code, String info)
    {
        this.code = code;
        this.info = info;
    }

    public String getCode()
    {
        return code;
    }

    public String getInfo()
    {
        return info;
    }
}
