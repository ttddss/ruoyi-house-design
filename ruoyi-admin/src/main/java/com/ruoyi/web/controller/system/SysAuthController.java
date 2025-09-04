package com.ruoyi.web.controller.system;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

import cn.hutool.extra.spring.SpringUtil;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.config.SourceConfig;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.enums.SourceEnum;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.framework.web.service.SysLoginService;
import me.zhyd.oauth.config.AuthConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.enums.UserStatus;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.AuthUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.web.service.SysPermissionService;
import com.ruoyi.framework.web.service.TokenService;
import com.ruoyi.system.domain.SysAuthUser;
import com.ruoyi.system.mapper.SysUserMapper;
import com.ruoyi.system.service.ISysUserService;
import me.zhyd.oauth.cache.AuthDefaultStateCache;
import me.zhyd.oauth.cache.AuthStateCache;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.model.AuthResponse;
import me.zhyd.oauth.model.AuthUser;
import me.zhyd.oauth.request.AuthRequest;
import me.zhyd.oauth.utils.AuthStateUtils;

/**
 * 第三方认证授权处理
 * 
 * @author ruoyi
 */
@RestController
@RequestMapping("/system/auth")
public class SysAuthController extends BaseController
{

    /**
     * 三方授权用户默认密码
     */
    private static final String DEFAULT_THIRD_AUTH_PASS = "123456";

    private AuthStateCache authStateCache;

    @Autowired
    private ISysUserService userService;

    @Autowired
    private SysPermissionService permissionService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private SysUserMapper userMapper;

    @Autowired
    private SysLoginService sysLoginService;

    private final static Map<String, AuthConfig> auths = new HashMap();

    {
        SourceConfig sourceConfig = SpringUtils.getBean(SourceConfig.class);
        auths.put(SourceEnum.GITEE.getCode(), sourceConfig.getGitee());
        auths.put(SourceEnum.WECHAT_MINI.getCode(), sourceConfig.getWechatMini());
        authStateCache = AuthDefaultStateCache.INSTANCE;
    }

    /**
     * 认证授权
     * 
     * @param source
     * @throws IOException
     */
    @GetMapping("/binding/{source}")
    @Log(title = "认证授权", businessType = BusinessType.GRANT)
    @ResponseBody
    public AjaxResult authBinding(@PathVariable("source") String source, HttpServletRequest request) throws IOException
    {
        LoginUser tokenUser = tokenService.getLoginUser(request);
        if (StringUtils.isNotNull(tokenUser) && userMapper.checkAuthUser(tokenUser.getUserId(), source) > 0)
        {
            return error(source + "平台账号已经绑定");
        }

        AuthConfig authConfig = auths.get(source);
        if (authConfig == null)
        {
            return error(source + "平台账号暂不支持");
        }
        AuthRequest authRequest = AuthUtils.getAuthRequest(source, authConfig.getClientId(), authConfig.getClientSecret(),
                authConfig.getRedirectUri(), authStateCache);
        String authorizeUrl = authRequest.authorize(AuthStateUtils.createState());
        return success(authorizeUrl);
    }

    @SuppressWarnings("unchecked")
    @GetMapping("/social-login/{source}")
    @Log(title = "三方登录", businessType = BusinessType.GRANT)
    public AjaxResult socialLogin(@PathVariable("source") String source, AuthCallback callback, HttpServletRequest request)
    {
        AuthConfig authConfig = auths.get(source);
        if (authConfig == null)
        {
            return AjaxResult.error(10002, "第三方平台系统不支持或未提供来源");
        }
        AuthRequest authRequest = AuthUtils.getAuthRequest(source, authConfig.getClientId(),
                authConfig.getClientSecret(), authConfig.getRedirectUri(), authStateCache);
        AuthResponse<AuthUser> response = authRequest.login(callback);
        if (response.ok())
        {
            LoginUser tokenUser = tokenService.getLoginUser(request);
            if (StringUtils.isNotNull(tokenUser))
            {
                SysUser user = userMapper.selectAuthUserByUuid(source + response.getData().getUuid());
                if (StringUtils.isNotNull(user))
                {
                    String token = tokenService.createToken(SecurityUtils.getLoginUser());
                    return success().put(Constants.TOKEN, token);
                }
                // 若已经登录则直接绑定系统账号
                SysAuthUser authUser = new SysAuthUser();
                authUser.setAvatar(response.getData().getAvatar());
                authUser.setUuid(source + response.getData().getUuid());
                authUser.setUserId(SecurityUtils.getUserId());
                authUser.setUserName(response.getData().getUsername());
                authUser.setNickName(response.getData().getNickname());
                authUser.setEmail(response.getData().getEmail());
                authUser.setSource(source);
                userMapper.insertAuthUser(authUser);
                String token = tokenService.createToken(SecurityUtils.getLoginUser());
                return success().put(Constants.TOKEN, token);
            }
            SysUser authUser = userMapper.selectAuthUserByUuid(source + response.getData().getUuid());
            if (StringUtils.isNotNull(authUser))
            {
                SysUser user = userService.selectUserByUserName(authUser.getUserName());
                if (StringUtils.isNull(user))
                {
                    throw new ServiceException("登录用户：" + user.getUserName() + " 不存在");
                }
                else if (UserStatus.DELETED.getCode().equals(user.getDelFlag()))
                {
                    throw new ServiceException("对不起，您的账号：" + user.getUserName() + " 已被删除");
                }
                else if (UserStatus.DISABLE.getCode().equals(user.getStatus()))
                {
                    throw new ServiceException("对不起，您的账号：" + user.getUserName() + " 已停用");
                }
                LoginUser loginUser = new LoginUser(user.getUserId(), user.getDeptId(), user, permissionService.getMenuPermission(user));
                String token = tokenService.createToken(loginUser);
                return success().put(Constants.TOKEN, token);
            }
            else
            {
                // 创建一个用户
                SysUser sysUser = new SysUser();
                sysUser.setAvatar(response.getData().getAvatar());
                sysUser.setEmail(response.getData().getEmail());
                sysUser.setNickName(response.getData().getNickname());
                sysUser.setUserName(source + response.getData().getUuid());
                sysUser.setPassword(SecurityUtils.encryptPassword(DEFAULT_THIRD_AUTH_PASS));
                userMapper.insertUser(sysUser);

                // 绑定系统账号
                SysAuthUser authUserDo = new SysAuthUser();
                authUserDo.setAvatar(response.getData().getAvatar());
                authUserDo.setUuid(source + response.getData().getUuid());
                authUserDo.setUserId(sysUser.getUserId());
                authUserDo.setUserName(response.getData().getUsername());
                authUserDo.setNickName(response.getData().getNickname());
                authUserDo.setEmail(response.getData().getEmail());
                authUserDo.setSource(source);
                userMapper.insertAuthUser(authUserDo);

                String token = sysLoginService.login(sysUser.getUserName(), DEFAULT_THIRD_AUTH_PASS, null, null);
                return success().put(Constants.TOKEN, token);
//                return AjaxResult.error(10002, "对不起，您没有绑定注册用户，请先注册后在个人中心绑定第三方授权信息！");
            }
        }
        return AjaxResult.error(10002, "对不起，授权信息验证不通过，请联系管理员");
    }

    /**
     * 取消授权
     */
    @DeleteMapping(value = "/unlock/{authId}")
    @Log(title = "取消授权", businessType = BusinessType.GRANT)
    public AjaxResult unlockAuth(@PathVariable Long authId)
    {
        userService.unlockAuth(authId);
        return AjaxResult.success();
    }
}
