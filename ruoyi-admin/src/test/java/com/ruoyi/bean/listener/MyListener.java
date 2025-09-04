package com.ruoyi.bean.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * @author tds
 * @version 1.0.0
 * @Company
 * @ClassName MyListener
 * @Description TODO（这里用一句话描述这个类的作用)
 * @Date 2025-07-31 7:14
 */
@Component
@Slf4j
public class MyListener implements ApplicationListener<ContextRefreshedEvent> {

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        log.info("监听到容器完毕...{}", event);
    }
}
