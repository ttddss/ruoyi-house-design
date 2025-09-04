package com.ruoyi.framework.interceptor.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson2.JSON;
import com.ruoyi.common.annotation.RepeatSubmit;
import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.filter.RepeatedlyRequestWrapper;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.http.HttpHelper;
import com.ruoyi.common.utils.sign.Md5Utils;
import com.ruoyi.framework.interceptor.RepeatSubmitInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

/**
 * 判断请求url和数据是否和上一次相同，
 * 如果和上次相同，则是重复提交表单。 有效时间为10秒内。
 * 
 * @author ruoyi
 */
@Component
public class SameUrlDataInterceptor extends RepeatSubmitInterceptor
{
    public final String REPEAT_PARAMS = "repeatParams";

    public final String REPEAT_TIME = "repeatTime";

    private static final ThreadLocal<String> CACHE_KEY_TL = new ThreadLocal<>();

    // 令牌自定义标识
    @Value("${token.header}")
    private String header;

    @Autowired
    private RedisCache redisCache;

    @SuppressWarnings("unchecked")
    @Override
    public boolean isRepeatSubmit(HttpServletRequest request, RepeatSubmit annotation)
    {
        String nowParams = "";
        if (request instanceof RepeatedlyRequestWrapper)
        {
            RepeatedlyRequestWrapper repeatedlyRequest = (RepeatedlyRequestWrapper) request;
            nowParams = HttpHelper.getBodyString(repeatedlyRequest);
        }

        // body参数为空，获取Parameter的数据
        if (StringUtils.isEmpty(nowParams))
        {
            nowParams = JSON.toJSONString(request.getParameterMap());
        }

        // 请求地址（作为存放cache的key值）
        String url = request.getRequestURI();

        // 唯一标识（指定key + url + 请求参数）
        String cacheRepeatKey = CacheConstants.REPEAT_SUBMIT_KEY + Md5Utils.hash(url + nowParams);
        // 如果指定了key表达式，使用表达式获取key
        if (StrUtil.isNotBlank(annotation.keyExpression())) {
            Object cacheKey = JSONUtil.parseObj(nowParams).getByPath(annotation.keyExpression());
            if (cacheKey != null) {
                cacheRepeatKey = cacheKey.toString();
            }
        }
        CACHE_KEY_TL.set(cacheRepeatKey);

        if(redisCache.setCacheIfAbsent(cacheRepeatKey, 0, annotation.interval(), TimeUnit.MILLISECONDS)) {
            return false;
        }

        return true;
    }


    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 图片文件等资源访问，不处理
        if (handler instanceof ResourceHttpRequestHandler) {
            return;
        }
        RepeatSubmit annotation = ((HandlerMethod) handler).getMethod().getAnnotation(RepeatSubmit.class);
        if (annotation != null) {
            if (annotation.releaseAfter()) {
                redisCache.deleteObject(CACHE_KEY_TL.get());
            }
            CACHE_KEY_TL.remove();
        }

    }
}
