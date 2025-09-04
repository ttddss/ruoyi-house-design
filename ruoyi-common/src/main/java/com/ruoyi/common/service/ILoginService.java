package com.ruoyi.common.service;

import com.ruoyi.common.enums.LoginTypeEnum;
import org.springframework.security.core.userdetails.UserDetails;

/**
 *
 *
 * @author tongdashuai
 * @version 1.0
 * @description
 * @module
 * @date 2024/8/12 13:33
 */
public interface ILoginService {

    /**
     * 获取登录类型
     *
     * @return
     */
    LoginTypeEnum getLoginType();


    /**
     * 登录验证
     *
     * @param username 登录账号
     * @return
     */
    UserDetails loadUserByUsername(String username);
}
