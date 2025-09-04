package com.ruoyi.bean.config;

import org.apache.catalina.startup.Tomcat;
import org.apache.coyote.UpgradeProtocol;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.SearchStrategy;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.servlet.Servlet;
import java.util.concurrent.ThreadPoolExecutor;
//import io.undertow.Undertow;

/**
 * @author tds
 * @version 1.0.0
 * @Company
 * @ClassName MyConfiguration
 * @Description TODO（这里用一句话描述这个类的作用)
 * @Date 2025-07-31 6:42
 */
@Configuration
@EnableAsync
@ComponentScan("com.ruoyi.bean")
//@ConditionalOnClass({ Servlet.class, Undertow.class })
@EnableAspectJAutoProxy
public class MyConfiguration {

    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(10);
        threadPoolTaskExecutor.setMaxPoolSize(10);
        threadPoolTaskExecutor.setQueueCapacity(100);
        threadPoolTaskExecutor.setThreadGroupName("测试线程");
        threadPoolTaskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        return threadPoolTaskExecutor;
    }
}
