package com.ruoyi.common.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * rabbitmq配置
 * 延时交换机的名字要求有“beichen-delay”前缀！！RabbitSendCallback那边会用到这个
 *
 * @author tongdashuai
 * @version 1.0
 * @description
 * @module
 * @date 2024/8/18 15:02
 */
@Configuration
@Component
public class RabbitMqConfig {

    /**
     * 查询订单状态队列
     */
    public static String QUEUE_QUERY_ORDER_NAME;

    /**
     * 超时自动关闭订单队列
     */
    public static String QUEUE_AUTO_CLOSE_ORDER_NAME;

    /**
     * 查询退款单状态队列
     */
    public static String QUEUE_QUERY_REFUND_NAME;

    /**
     * 默认延迟交换机名称
     */
    public static String DEFAULT_DELAY_EXCHANGE_NAME;

    /**
     * 默认交换机名称
     */
    public static String DEFAULT_EXCHANGE_NAME;

    @Value("${rabbitMq.queue.queue_query_order_name}")
    public void setQueueQueryOrderName(String queueQueryOrderName){
        QUEUE_QUERY_ORDER_NAME = queueQueryOrderName;
    }

    @Value("${rabbitMq.queue.queue_auto_close_order_name}")
    public void setQueueAutoCloseOrderName(String queueAutoCloseOrderName){
        QUEUE_AUTO_CLOSE_ORDER_NAME = queueAutoCloseOrderName;
    }

    @Value("${rabbitMq.queue.queue_query_refund_name}")
    public void setQueueQueryRefundName(String queueQueryRefundName){
        QUEUE_QUERY_REFUND_NAME = queueQueryRefundName;
    }

    @Value("${rabbitMq.exchange.default_exchange_name}")
    public void setDefaultExchangeName(String defaultExchangeName){
        DEFAULT_EXCHANGE_NAME = defaultExchangeName;
    }

    @Value("${rabbitMq.exchange.default_delay_exchange_name}")
    public void setDefaultDelayExchangeName(String defaultDelayExchangeName){
        DEFAULT_DELAY_EXCHANGE_NAME = defaultDelayExchangeName;
    }

    @Bean
    public Queue queryOrderQueue() {
        return new Queue(QUEUE_QUERY_ORDER_NAME);
    }

    @Bean
    public Queue autoCloseOrderQueue() {
        return new Queue(QUEUE_AUTO_CLOSE_ORDER_NAME);
    }

    @Bean
    public Queue queryRefundQueue() {
        return new Queue(QUEUE_QUERY_REFUND_NAME);
    }


    @Bean
    public DirectExchange defaultExchange() {
        DirectExchange exchange = new DirectExchange(DEFAULT_EXCHANGE_NAME,  true, false);
        return exchange;
    }

    @Bean
    public DirectExchange defaultDelayExchange() {
        DirectExchange exchange = new DirectExchange(DEFAULT_DELAY_EXCHANGE_NAME,  true, false);
        exchange.setDelayed(true);
        return exchange;
    }

    @Bean
    public Binding bindingQueryOrder() {
        return BindingBuilder.bind(queryOrderQueue()).to(defaultDelayExchange()).with(QUEUE_QUERY_ORDER_NAME);
    }

    @Bean
    public Binding bindingAutoCloseOrder() {
        return BindingBuilder.bind(autoCloseOrderQueue()).to(defaultDelayExchange()).with(QUEUE_AUTO_CLOSE_ORDER_NAME);
    }

    @Bean
    public Binding bindingQueryRefund() {
        return BindingBuilder.bind(queryRefundQueue()).to(defaultDelayExchange()).with(QUEUE_QUERY_REFUND_NAME);
    }

}
