package com.ruoyi.framework.aspectj;

import com.ruoyi.common.utils.StringUtils;
import org.slf4j.MDC;

import java.util.UUID;

/**
 * @author xzh
 * @since 2022/7/5 9:43 AM
 */
public class MdcUtil {
    public static final String TRACE_ID = "traceId";

    /**
     * 往ThreadLocal里放一个 requestId
     * <p>
     * 如果不存在，则put一个
     * <p>
     * 如果存在，则不处理
     */
    public static void putTraceIdIfTraceIdIsNull() {
        String requestId = MDC.get(TRACE_ID);
        if (StringUtils.isEmpty(requestId)) {
            MDC.put(TRACE_ID, UUID.randomUUID().toString().replace("-", ""));
        }
    }

    public static String getTraceId() {
        return MDC.get(TRACE_ID);
    }
}
