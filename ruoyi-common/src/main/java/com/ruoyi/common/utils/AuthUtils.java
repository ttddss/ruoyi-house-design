package com.ruoyi.common.utils;

import com.ruoyi.common.enums.SourceEnum;
import me.zhyd.oauth.cache.AuthStateCache;
import me.zhyd.oauth.config.AuthConfig;
import me.zhyd.oauth.exception.AuthException;
import me.zhyd.oauth.request.*;

/**
 * 认证授权工具类
 * 
 * @author ruoyi
 */
public class AuthUtils
{
    @SuppressWarnings("deprecation")
    public static AuthRequest getAuthRequest(String source, String clientId, String clientSecret, String redirectUri,
            AuthStateCache authStateCache)
    {
        AuthRequest authRequest = null;
        SourceEnum sourceEnum = SourceEnum.getByCode(source.toLowerCase());
        AssertUtils.notNull(sourceEnum, "未知的来源" + source);
        switch (sourceEnum)
        {
            case DINGTALK:
                authRequest = new AuthDingTalkRequest(AuthConfig.builder().clientId(clientId).clientSecret(clientSecret)
                        .redirectUri(redirectUri).build(), authStateCache);
                break;
            case BAIDU:
                authRequest = new AuthBaiduRequest(AuthConfig.builder().clientId(clientId).clientSecret(clientSecret)
                        .redirectUri(redirectUri).build(), authStateCache);
                break;
            case GITHUB:
                authRequest = new AuthGithubRequest(AuthConfig.builder().clientId(clientId).clientSecret(clientSecret)
                        .redirectUri(redirectUri).build(), authStateCache);
                break;
            case GITEE:
                authRequest = new AuthGiteeRequest(AuthConfig.builder().clientId(clientId).clientSecret(clientSecret)
                        .redirectUri(redirectUri).build(), authStateCache);
                break;
            case WEIBO:
                authRequest = new AuthWeiboRequest(AuthConfig.builder().clientId(clientId).clientSecret(clientSecret)
                        .redirectUri(redirectUri).build(), authStateCache);
                break;

            case ALIPAY:
                // 支付宝在创建回调地址时，不允许使用localhost或者127.0.0.1，所以这儿的回调地址使用的局域网内的ip
                authRequest = new AuthAlipayRequest(AuthConfig.builder().clientId(clientId).clientSecret(clientSecret)
                        .alipayPublicKey("").redirectUri(redirectUri).build(), authStateCache);
                break;
            case QQ:
                authRequest = new AuthQqRequest(AuthConfig.builder().clientId(clientId).clientSecret(clientSecret)
                        .redirectUri(redirectUri).build(), authStateCache);
                break;
            case WECHAT_OPEN:
                authRequest = new AuthWeChatOpenRequest(AuthConfig.builder().clientId(clientId)
                        .clientSecret(clientSecret).redirectUri(redirectUri).build(), authStateCache);
                break;
            case WECHAT_MP:
                authRequest = new AuthWeChatMpRequest(AuthConfig.builder().clientId(clientId).clientSecret(clientSecret)
                        .redirectUri(redirectUri).build(), authStateCache);
                break;
            case ALIYUN:
                authRequest = new AuthAliyunRequest(AuthConfig.builder().clientId(clientId).clientSecret(clientSecret)
                        .redirectUri(redirectUri).build(), authStateCache);
                break;
            case WECHAT_MINI:
                authRequest = new AuthWechatMiniProgramRequest(AuthConfig.builder()
                        .clientId(clientId)
                        .clientSecret(clientSecret)
                        .ignoreCheckRedirectUri(true)
                        .ignoreCheckState(true)
                        .build());
            default:
                break;
        }
        if (null == authRequest)
        {
            throw new AuthException("未获取到有效的Auth配置");
        }
        return authRequest;
    }
}
