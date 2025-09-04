package com.ruoyi.common.utils;


import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.aliyun.oss.ClientBuilderConfiguration;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.common.auth.CredentialsProvider;
import com.aliyun.oss.common.comm.SignVersion;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.aliyuncs.sts.model.v20150401.AssumeRoleRequest;
import com.aliyuncs.sts.model.v20150401.AssumeRoleResponse;
import com.ruoyi.common.config.AliyunOssConfig;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.spring.SpringUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;




/**
 *
 * 华为云oos工具类
 * 相关文档：https://help.aliyun.com/zh/oss/developer-reference/installation?spm=a2c4g.11186623.help-menu-31815.d_5_2_8_0.5689b919bf3MtI#b909e07045ygm
 * @author tongdashuai
 * @version 1.0
 * @description
 * @date 2025/3/18 9:37
 */
@Slf4j
@Component
public class OssUtils {

    /**
     * STS服务接入点，例如sts.cn-hangzhou.aliyuncs.com。您可以通过公网或者VPC接入STS服务。
     */
    public static final String ENDPOINT = "sts.cn-hangzhou.aliyuncs.com";

    /**
     * oss服务接入点
     */

    public static final String OSS_ENDPOINT = "https://oss-cn-hangzhou.aliyuncs.com";

    public static final String STS_PRODUCT = "Sts";

    /**
     * 自定义角色会话名称，用来区分不同的令牌，例如可填写为SessionTest。
     */
    public static final String ROLE_SESSION_NAME = "ossRoleSession";

    /**
     * 默认的桶
     */
    public static final String DEFAULT_BUCKET_NAME = "house-design";



    /**
     * 获取sts凭证信息
     * sts相关文档：https://help.aliyun.com/zh/oss/developer-reference/use-temporary-access-credentials-provided-by-sts-to-access-oss?spm=a2c4g.11186623.0.0.21251aeczoZdop#concept-xzh-nzk-2gb
     * @return
     */
    public static CredentialVO getCredential() {
        AliyunOssConfig aliyunOssConfig = SpringUtils.getBean(AliyunOssConfig.class);
        return getCredential(aliyunOssConfig.getAccessKeyId(), aliyunOssConfig.getAccessKeySecret()
                , aliyunOssConfig.getRoleArn(), aliyunOssConfig.getDurationSeconds());
    }


    /**
     * 获取sts凭证信息
     *
     * @param accessKeyId 访问凭证id
     * @param accessKeySecret 访问凭证密钥
     * @param roleArn 角色arn
     * @return
     */
    public static CredentialVO getCredential(String accessKeyId, String accessKeySecret, String roleArn, Long durationSeconds) {
        try {
            // 临时访问凭证将获得角色拥有的所有权限。
            String policy = null;
            // 发起STS请求所在的地域。建议保留默认值，默认值为空字符串（""）。
            String regionId = "";
            // 添加endpoint。适用于Java SDK 3.12.0及以上版本。
            DefaultProfile.addEndpoint(regionId, STS_PRODUCT, ENDPOINT);
            IClientProfile profile = DefaultProfile.getProfile(regionId, accessKeyId, accessKeySecret);
            // 构造client。
            DefaultAcsClient client = new DefaultAcsClient(profile);
            final AssumeRoleRequest request = new AssumeRoleRequest();
            request.setSysMethod(MethodType.POST);
            request.setRoleArn(roleArn);
            request.setRoleSessionName(ROLE_SESSION_NAME);
            request.setPolicy(policy);
            request.setDurationSeconds(durationSeconds);
            final AssumeRoleResponse response = client.getAcsResponse(request);
            AssumeRoleResponse.Credentials credentials = response.getCredentials();
            log.info("获取sts凭证信息返回Credentials:{}, RequestId:{}", JSONUtil.toJsonStr(credentials), response.getRequestId());
            return new CredentialVO(credentials.getAccessKeyId(), credentials.getAccessKeySecret(), credentials.getSecurityToken(),
                    credentials.getExpiration());
        } catch (ClientException e) {
            log.error("获取sts凭证信息出错, 堆栈异常：", e);
            log.error("errorCode:{}, errorMessage:{}, requestId:{}", e.getErrCode(), e.getErrMsg(), e.getRequestId());
            throw new ServiceException("获取sts凭证信息异常");
        }
    }


    /**
     * oss上传文件
     *
     * @param accessKeyId 访问凭证id。sts临时访问传临时访问凭证id
     * @param accessKeySecret 访问凭证密钥。sts临时访问传临时访问凭证
     * @param securityToken sts临时访问凭证安全令牌。非sts临时访问传null。
     * @param bucketName 桶名称
     * @param file 文件名称
     */
    public static String uploadFile(String accessKeyId, String accessKeySecret, String securityToken, String bucketName, File file) {
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(OSS_ENDPOINT, accessKeyId, accessKeySecret, securityToken);
        // oss对象名称
        String objectName = IdUtil.fastSimpleUUID() + "." + FileUtil.getSuffix(file);
        // 将本地文件file上传至bucketName桶。
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, objectName, file);
        try {
            // 上传文件。
            ossClient.putObject(putObjectRequest);
            return StrUtil.format("https://{}.oss-cn-hangzhou.aliyuncs.com/{}", bucketName, objectName);
        } catch (OSSException oe) {
            log.error("oss上传文件异常，异常堆栈信息：", oe);
            log.error("errorCode:{}, errorMessage:{}, requestId:{}", oe.getErrorCode(), oe.getErrorMessage(), oe.getRequestId());
            throw new ServiceException("oss文件上传出错");
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }

    public static void main(String[] args) throws IOException {
        // 从环境变量中获取步骤1.1生成的RAM用户的访问密钥（AccessKey ID和AccessKey Secret）。
        String accessKeyId = "xxx";
        String accessKeySecret = "xx";
        // 从环境变量中获取步骤1.3生成的RAM角色的RamRoleArn。
        String roleArn = "acs:ram::1570241060691783:role/oss-role";
        String bucketName = "house-design";
        // 自定义角色会话名称，用来区分不同的令牌，例如可填写为SessionTest。
        // 临时访问凭证的有效时间，单位为秒。最小值为900，最大值以当前角色设定的最大会话时间为准。当前角色最大会话时间取值范围为3600秒~43200秒，默认值为3600秒。
        // 在上传大文件或者其他较耗时的使用场景中，建议合理设置临时访问凭证的有效时间，确保在完成目标任务前无需反复调用STS服务以获取临时访问凭证。
        Long durationSeconds = 3600L;
        CredentialVO credential = getCredential(accessKeyId, accessKeySecret, roleArn, durationSeconds);


        // 上传文件到oss
//        File file = new File("C:\\Users\\zity\\Desktop\\tmp\\tmp\\logo.png");
//        String imageUrl = uploadFile(accessKeyId, accessKeySecret, null, bucketName, file);
//        System.out.println(imageUrl);

        // 使用临时凭证上传文件到oss
        File file = new File("C:\\Users\\zity\\Desktop\\tmp\\tmp\\logo.png");
        String imageUrl = uploadFile(credential.getAccessKeyId(), credential.getAccessKeySecret(), credential.getSecurityToken(), bucketName, file);
        System.out.println(imageUrl);

    }


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CredentialVO {

        /**
         * 访问凭证
         */
        private String accessKeyId;

        /**
         * 访问凭证
         */
        private String accessKeySecret;

        /**
         * 安全令牌
         */
        private String securityToken;

        /**
         * 失效时间
         */
        private String expiration;

    }
}
