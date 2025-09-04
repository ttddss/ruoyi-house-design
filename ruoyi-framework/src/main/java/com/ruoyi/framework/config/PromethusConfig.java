package com.ruoyi.framework.config;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author tds
 * @version 1.0.0
 * @Company
 * @ClassName PromethusConfig
 * @Description TODO（这里用一句话描述这个类的作用)
 * @Date 2025-07-03 21:26
 */
@Configuration
public class PromethusConfig {

    @Bean
    MeterRegistryCustomizer<MeterRegistry> configurer(@Value("${spring.application.name}") String name) {
        return registry -> registry.config().commonTags("application", name);
    }

}
