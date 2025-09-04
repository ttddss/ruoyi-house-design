package com.ruoyi.framework.config;

import com.ruoyi.framework.aspectj.MdcUtil;
import lombok.extern.log4j.Log4j2;
import org.slf4j.MDC;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskDecorator;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 线程池配置
 *
 * @author xzh
 * @since 2020/6/5 10:50 上午
 */
@Configuration
public class AsyncTaskConfig implements AsyncConfigurer {
    // ThredPoolTaskExcutor的处理流程
    // 当池子大小小于corePoolSize，就新建线程，并处理请求
    // 当池子大小等于corePoolSize，把请求放入workQueue中，池子里的空闲线程就去workQueue中取任务并处理
    // 当workQueue放不下任务时，就新建线程入池，并处理请求，如果池子大小撑到了maximumPoolSize，就用RejectedExecutionHandler来做拒绝处理
    // 当池子的线程数大于corePoolSize时，多余的线程会等待keepAliveTime长时间，如果无请求可处理就自行销毁

    @Override
    @Bean(name = "myAsync")
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor threadPool = new ThreadPoolTaskExecutor();
        //设置核心线程数
        threadPool.setCorePoolSize(100);
        //设置最大线程数
        threadPool.setMaxPoolSize(100);
        //线程池所使用的缓冲队列
        threadPool.setQueueCapacity(20);
        //设置队列
//        threadPool.
        //等待任务在关机时完成--表明等待所有线程执行完
        threadPool.setWaitForTasksToCompleteOnShutdown(true);
        // 等待时间 （默认为0，此时立即停止），并没等待xx秒后强制停止
        threadPool.setAwaitTerminationSeconds(60);
        //  线程名称前缀
        threadPool.setThreadNamePrefix("zidingyi-Async-");
        threadPool.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        threadPool.setTaskDecorator(new MdcTaskDecorator());
        // 初始化线程
        threadPool.initialize();
        return threadPool;
    }
    @Bean(name = "timing")
    public Executor timing() {
        ThreadPoolTaskExecutor threadPool = new ThreadPoolTaskExecutor();
        //设置核心线程数
        threadPool.setCorePoolSize(100);
        //设置最大线程数
        threadPool.setMaxPoolSize(100);
        //线程池所使用的缓冲队列
        threadPool.setQueueCapacity(20);
        //设置队列
//        threadPool.
        //等待任务在关机时完成--表明等待所有线程执行完
        threadPool.setWaitForTasksToCompleteOnShutdown(true);
        // 等待时间 （默认为0，此时立即停止），并没等待xx秒后强制停止
        threadPool.setAwaitTerminationSeconds(60);
        //  线程名称前缀
        threadPool.setThreadNamePrefix("timing-Async-");
        threadPool.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        threadPool.setTaskDecorator(new MdcTaskDecorator());
        // 初始化线程
        threadPool.initialize();
        return threadPool;
    }
    @Bean(name = "asyncHandlerPool")
    public Executor asyncHandlerPool() {
        ThreadPoolTaskExecutor threadPool = new ThreadPoolTaskExecutor();
        //设置核心线程数
        threadPool.setCorePoolSize(100);
        //设置最大线程数
        threadPool.setMaxPoolSize(100);
        //线程池所使用的缓冲队列
        threadPool.setQueueCapacity(20);
        //设置队列
//        threadPool.
        //等待任务在关机时完成--表明等待所有线程执行完
        threadPool.setWaitForTasksToCompleteOnShutdown(true);
        // 等待时间 （默认为0，此时立即停止），并没等待xx秒后强制停止
        threadPool.setAwaitTerminationSeconds(60);
        //  线程名称前缀
        threadPool.setThreadNamePrefix("asyncHandlerPool-Async-");
        threadPool.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        threadPool.setTaskDecorator(new MdcTaskDecorator());
        // 初始化线程
        threadPool.initialize();
        return threadPool;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new TaskExecutePoolAsyncUncaughtExceptionHandler();
    }

}

/**
 * 异步任务中异常处理
 *
 * @author thght
 */
@Log4j2
class TaskExecutePoolAsyncUncaughtExceptionHandler implements AsyncUncaughtExceptionHandler {
    @Override
    public void handleUncaughtException(Throwable arg0, Method arg1, Object... arg2) {
        log.error("==========================" + arg0.getMessage() + "=======================", arg0);
        log.error("exception method:" + arg1.getName());
    }
}

/**
 * 让@Async注解可以获取到mdc中的内容<br/>
 * decorate()方法的参数是一个Runnable对象，返回结果也是另一个Runnable对象,这里，只是把原始的Runnable对象包装了一下，首先取得MDC数据，然后把它放到了委托的run方法里.
 */
class MdcTaskDecorator implements TaskDecorator {

    @Override
    public Runnable decorate(Runnable runnable) {
        // Right now: Web thread context !
        // Grab the current thread MDC data
        Map<String, String> copyOfContextMap = MDC.getCopyOfContextMap();
        if (null == copyOfContextMap) {
            copyOfContextMap = Collections.emptyMap();
        }

        Map<String, String> finalCopyOfContextMap = copyOfContextMap;
        return () -> {
            // Right now: @Async thread context !
            // Restore the Web thread context's MDC data
            MDC.setContextMap(finalCopyOfContextMap);
            MdcUtil.putTraceIdIfTraceIdIsNull();
            runnable.run();
        };
    }

}