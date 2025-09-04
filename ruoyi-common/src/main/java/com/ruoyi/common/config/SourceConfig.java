package com.ruoyi.common.config;

import lombok.Data;
import me.zhyd.oauth.config.AuthConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 应用来源配置
 * 
 * @author ruoyi
 */
@Component
@Data
@ConfigurationProperties(prefix = "source")
public class SourceConfig
{
    private AuthConfig gitee;

    private AuthConfig wechatMini;
}
