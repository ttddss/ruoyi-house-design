package com.ruoyi.framework.web.service;

import com.ruoyi.common.enums.LoginTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * 用户验证处理
 *
 * @author ruoyi
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService
{


    @Autowired
    private LoginServiceContext loginServiceContext;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {
        return loginServiceContext.loadUserByUsername(LoginTypeEnum.MANAGEMENT_USER, username);
    }

}
