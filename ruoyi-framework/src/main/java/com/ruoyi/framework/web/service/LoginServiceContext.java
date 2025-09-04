package com.ruoyi.framework.web.service;

import com.ruoyi.common.enums.LoginTypeEnum;
import com.ruoyi.common.service.ILoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 *
 * @author tongdashuai
 * @version 1.0
 * @description
 * @module
 * @date 2024/8/12 13:34
 */
@Service
@Slf4j
public class LoginServiceContext {

    private static final Map<Integer, ILoginService> LOGIN_SERVICE_MAP = new HashMap<>();


    @Autowired
    public LoginServiceContext(List<ILoginService> loginServices) {
        for (ILoginService loginService : loginServices) {
            LOGIN_SERVICE_MAP.put(loginService.getLoginType().getCode(), loginService);
        }
    }

    public UserDetails loadUserByUsername(LoginTypeEnum loginType, String username) {
        return LOGIN_SERVICE_MAP.get(loginType.getCode()).loadUserByUsername(username);
    }
}
