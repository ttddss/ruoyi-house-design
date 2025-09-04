package com.ruoyi.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 *
 *
 * @author tongdashuai
 * @version 1.0
 * @description
 * @module
 * @date 2025/2/12 15:58
 */
@Component
@ConfigurationProperties(prefix = "aliyun.oss")
@Data
public class AliyunOssConfig {



    /**
     * STS服务接入点，例如sts.cn-hangzhou.aliyuncs.com。您可以通过公网或者VPC接入STS服务。
     */
    private String endpoint;

    /**
     * 访问凭证
     */
    private String accessKeyId;

    /**
     * 访问凭证
     */
    private String accessKeySecret;

    /**
     * RAM角色的RamRoleArn
     */
    private String roleArn;

    /**
     * 临时访问凭证的有效时间，单位为秒。最小值为900，最大值以当前角色设定的最大会话时间为准。当前角色最大会话时间取值范围为3600秒~43200秒，默认值为3600秒。
     * 在上传大文件或者其他较耗时的使用场景中，建议合理设置临时访问凭证的有效时间，确保在完成目标任务前无需反复调用STS服务以获取临时访问凭证。
     */
    private Long durationSeconds;
}
