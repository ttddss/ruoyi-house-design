package com.ruoyi.bean.postprocessor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.PriorityOrdered;
import org.springframework.stereotype.Component;

/**
 * @author tds
 * @version 1.0.0
 * @Company
 * @ClassName MyPostProcessorOne
 * @Description TODO（这里用一句话描述这个类的作用)
 * @Date 2025-07-30 20:53
 */
//@Component
@Slf4j
public class MyPostProcessorFour implements BeanPostProcessor, PriorityOrdered {

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (log.isInfoEnabled()) {
            log.info("MyPostProcessorFour.postProcessBeforeInitialization");
        }
        return BeanPostProcessor.super.postProcessBeforeInitialization(bean, beanName);
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (log.isInfoEnabled()) {
            log.info("MyPostProcessorFour.postProcessAfterInitialization");
        }
        return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
    }
}
