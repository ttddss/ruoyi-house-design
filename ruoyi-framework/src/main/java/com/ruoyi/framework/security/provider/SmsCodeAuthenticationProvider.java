package com.ruoyi.framework.security.provider;

import com.ruoyi.framework.security.authtoken.SmsCodeAuthenticationToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * 短信验证码认证认证
 * 移动端加了短信验证码验证，不需要校验密码，所以重写了自带的密码校验，如果是移动端用户那么不去校验密码
 *
 * @author tongdashuai
 * @version 1.0
 * @description
 * @module
 * @date 2024/8/12 15:53
 */
@Slf4j
public class SmsCodeAuthenticationProvider implements AuthenticationProvider {

    private UserDetailsService userDetailsService;

    public SmsCodeAuthenticationProvider(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UserDetails user = userDetailsService.loadUserByUsername((String) authentication.getPrincipal());
        SmsCodeAuthenticationToken result = new SmsCodeAuthenticationToken(user, authentication.getCredentials());
        result.setDetails(authentication.getDetails());
        log.debug("Authenticated user");
        return result;
    }

    /**
     * 判断只有传入UserAuthenticationToken的时候才使用这个Provider
     * supports会在AuthenticationManager层被调用
     * @param authentication
     * @return
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return SmsCodeAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
