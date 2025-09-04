package com.ruoyi.framework.aspectj;

import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson2.JSON;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.enums.BusinessStatus;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.enums.HttpMethod;
import com.ruoyi.common.enums.OperatorType;
import com.ruoyi.common.filter.PropertyPreExcludeFilter;
import com.ruoyi.common.filter.RepeatedlyRequestWrapper;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.ip.IpUtils;
import com.ruoyi.framework.manager.AsyncManager;
import com.ruoyi.framework.manager.factory.AsyncFactory;
import com.ruoyi.system.domain.SysOperLog;
import org.apache.commons.lang3.ArrayUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.NamedThreadLocal;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.Map;

/**
 * 操作日志记录处理
 *
 * @author ruoyi
 */
@Aspect
@Component
public class LogAspect {
    private static final Logger log = LoggerFactory.getLogger(LogAspect.class);

    /**
     * 排除敏感属性字段
     */
    public static final String[] EXCLUDE_PROPERTIES = {"password", "oldPassword", "newPassword", "confirmPassword"};


    /**
     * 记录操作日志
     */
    private static final ThreadLocal<SysOperLog> LOG_THREADLOCAL = new NamedThreadLocal<>("SysOperLog");

    /**
     * 处理请求前执行
     */
    @Before(value = "@annotation(controllerLog)")
    public void boBefore(JoinPoint joinPoint, Log controllerLog) {
        // 获取当前的用户
        LoginUser loginUser = SecurityUtils.getLoginUser(false);

        // *========数据库日志=========*//
        SysOperLog operLog = new SysOperLog();
        operLog.setStatus(BusinessStatus.SUCCESS.ordinal());
        // 请求的地址
        String ip = IpUtils.getIpAddr();
        operLog.setOperIp(ip);
        operLog.setOperUrl(StringUtils.substring(ServletUtils.getRequest().getRequestURI(), 0, 255));
        if (loginUser != null) {
            operLog.setOperName(loginUser.getUsername());
            SysUser currentUser = loginUser.getUser();
            if (StringUtils.isNotNull(currentUser) && StringUtils.isNotNull(currentUser.getDept())) {
                operLog.setDeptName(currentUser.getDept().getDeptName());
            }
        }

        // 设置方法名称
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        operLog.setMethod(className + "." + methodName + "()");
        // 设置请求方式
        operLog.setRequestMethod(ServletUtils.getRequest().getMethod());
        // 处理设置注解上的参数
        getControllerMethodDescription(joinPoint, controllerLog, operLog);

        // 打印请求日志
        printLog(controllerLog, operLog);

        operLog.setCostTime(System.currentTimeMillis());
        LOG_THREADLOCAL.set(operLog);
    }

    /**
     * 打印请求日志
     *
     * @param controllerLog
     * @param operLog
     */
    private void printLog(Log controllerLog, SysOperLog operLog) {
        Map<?, ?> paramsMap = ServletUtils.getParamMap(ServletUtils.getRequest());
        String requestParam = JSON.toJSONString(paramsMap, excludePropertyPreFilter(controllerLog.excludeParamNames()));
        String requestBody = null;
        // 文件上传类型请求不打印请求body
        if (ServletUtils.getRequest() instanceof RepeatedlyRequestWrapper) {
            requestBody = JSON.toJSONString(JSONUtil.parse(ServletUtil.getBody(ServletUtils.getRequest())), excludePropertyPreFilter(controllerLog.excludeParamNames()));
        }

        // 打印请求日志
        log.info("[{}] url : {}", operLog.getOperUrl(), operLog.getOperUrl());
        log.info("[{}] ip : {}", operLog.getOperUrl(), operLog.getOperIp());
        log.info("[{}] 类方法 : {}", operLog.getOperUrl(), operLog.getMethod());
        log.info("[{}] 请求参数 : {}", operLog.getOperUrl(), requestParam);
        log.info("[{}] 请求body : {}", operLog.getOperUrl(), requestBody);
        log.info("[{}] 请求类型 : {}", operLog.getOperUrl(), ServletUtils.getRequest().getMethod());
        log.info("[{}] 业务标题 : {}", operLog.getOperUrl(), operLog.getTitle());
        log.info("[{}] 业务类型 : {}", operLog.getOperUrl(), BusinessType.values()[operLog.getBusinessType()].name());
    }

    /**
     * 处理完请求后执行
     *
     * @param joinPoint 切点
     */
    @AfterReturning(pointcut = "@annotation(controllerLog)", returning = "jsonResult")
    public void doAfterReturning(JoinPoint joinPoint, Log controllerLog, Object jsonResult) {
        handleLog(controllerLog, null, jsonResult);
    }

    /**
     * 拦截异常操作
     *
     * @param joinPoint 切点
     * @param e         异常
     */
    @AfterThrowing(value = "@annotation(controllerLog)", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Log controllerLog, Exception e) {
        handleLog(controllerLog, e, null);
    }

    protected void handleLog(Log controllerLog, final Exception e, Object jsonResult) {
        try {
            // *========数据库日志=========*//
            SysOperLog operLog = LOG_THREADLOCAL.get();
            if (e != null) {
                operLog.setStatus(BusinessStatus.FAIL.ordinal());
                operLog.setErrorMsg(StringUtils.substring(e.getMessage(), 0, 2000));
            }
            log.info("[{}] 响应结果 : {}", operLog.getOperUrl(), JSON.toJSONString(jsonResult));

            // 是否需要保存response，参数和值
            if (controllerLog.isSaveResponseData() && StringUtils.isNotNull(jsonResult)) {
                operLog.setJsonResult(StringUtils.substring(JSON.toJSONString(jsonResult), 0, 2000));
            }
            // 设置消耗时间
            operLog.setCostTime(System.currentTimeMillis() - operLog.getCostTime());
            log.info("[{}] 响应耗时 : {}ms", operLog.getOperUrl(), operLog.getCostTime());

            // 仅管理端日志保存数据库
            if (OperatorType.MANAGE.equals(controllerLog.operatorType())) {
                // 保存数据库
                AsyncManager.me().execute(AsyncFactory.recordOper(operLog));
            }
        } catch (Exception exp) {
            // 记录本地异常日志
            log.error("异常信息:{}", exp.getMessage());
            exp.printStackTrace();
        } finally {
            LOG_THREADLOCAL.remove();
        }
    }


    /**
     * 获取注解中对方法的描述信息 用于Controller层注解
     *
     * @param log     日志
     * @param operLog 操作日志
     * @throws Exception
     */
    public void getControllerMethodDescription(JoinPoint joinPoint, Log log, SysOperLog operLog)  {
        // 设置action动作
        operLog.setBusinessType(log.businessType().ordinal());
        // 设置标题
        operLog.setTitle(log.title());
        // 设置操作人类别
        operLog.setOperatorType(log.operatorType().ordinal());
        // 是否需要保存request，参数和值
        if (log.isSaveRequestData()) {
            setRequestValue(joinPoint, operLog, log.excludeParamNames());
        }
    }

    /**
     * 获取请求的参数，放到log中
     *
     * @param operLog 操作日志
     * @throws Exception 异常
     */
    private void setRequestValue(JoinPoint joinPoint, SysOperLog operLog, String[] excludeParamNames)  {
        Map<?, ?> paramsMap = ServletUtils.getParamMap(ServletUtils.getRequest());
        String requestMethod = operLog.getRequestMethod();
        String requestParam = JSON.toJSONString(paramsMap, excludePropertyPreFilter(excludeParamNames));
        if (StringUtils.isEmpty(paramsMap)
                && (HttpMethod.PUT.name().equals(requestMethod) || HttpMethod.POST.name().equals(requestMethod))) {
            String params = argsArrayToString(joinPoint.getArgs(), excludeParamNames);
            operLog.setOperParam(StringUtils.substring(params, 0, 2000));
        } else {
            operLog.setOperParam(StringUtils.substring(requestParam, 0, 2000));
        }
    }

    /**
     * 参数拼装
     */
    private String argsArrayToString(Object[] paramsArray, String[] excludeParamNames) {
        String params = "";
        if (paramsArray != null && paramsArray.length > 0) {
            for (Object o : paramsArray) {
                if (StringUtils.isNotNull(o) && !isFilterObject(o)) {
                    try {
                        String jsonObj = JSON.toJSONString(o, excludePropertyPreFilter(excludeParamNames));
                        params += jsonObj.toString() + " ";
                    } catch (Exception e) {
                    }
                }
            }
        }
        return params.trim();
    }

    /**
     * 忽略敏感属性
     */
    public PropertyPreExcludeFilter excludePropertyPreFilter(String[] excludeParamNames) {
        return new PropertyPreExcludeFilter().addExcludes(ArrayUtils.addAll(EXCLUDE_PROPERTIES, excludeParamNames));
    }

    /**
     * 判断是否需要过滤的对象。
     *
     * @param o 对象信息。
     * @return 如果是需要过滤的对象，则返回true；否则返回false。
     */
    @SuppressWarnings("rawtypes")
    public boolean isFilterObject(final Object o) {
        Class<?> clazz = o.getClass();
        if (clazz.isArray()) {
            return clazz.getComponentType().isAssignableFrom(MultipartFile.class);
        } else if (Collection.class.isAssignableFrom(clazz)) {
            Collection collection = (Collection) o;
            for (Object value : collection) {
                return value instanceof MultipartFile;
            }
        } else if (Map.class.isAssignableFrom(clazz)) {
            Map map = (Map) o;
            for (Object value : map.entrySet()) {
                Map.Entry entry = (Map.Entry) value;
                return entry.getValue() instanceof MultipartFile;
            }
        }
        return o instanceof MultipartFile || o instanceof HttpServletRequest || o instanceof HttpServletResponse
                || o instanceof BindingResult;
    }
}
