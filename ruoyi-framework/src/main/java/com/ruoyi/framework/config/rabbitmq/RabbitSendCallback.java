package com.ruoyi.framework.config.rabbitmq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 *
 *
 * @author sun
 * @version 1.0
 * @description
 * @module
 * @date 2019/3/29
 */
@Component
@Slf4j
public class RabbitSendCallback implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnsCallback {

    /**
     * 延迟交换机前缀
     */
    @Value("${rabbitMq.delay_prefix}")
    private String defaultDelayExchangeName;


    @Autowired
    private RabbitTemplate rabbitTemplate;

    @PostConstruct
    public void init() {
        rabbitTemplate.setConfirmCallback(this);
        rabbitTemplate.setReturnsCallback(this);
    }

    /**
     * 消息发送到交换机的回调
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        // 设置确认回调
        if (ack) {
            log.info("消息发送到交换机成功:{}", correlationData.getId());
        } else {
            log.error("消息发送到交换机失败,消息id：{}", correlationData.getId());
            log.error("消息发送到交换机失败,失敗原因：:{}", cause);
        }

    }


    /**
     * Returned message callback.
     *
     * RabbitMQ延迟插件不支持mandatory=true参数，如果启用会同时收到延迟消息和路由失败消息。
     * 延时消息是从磁盘读取消息然后发送（后台任务），发送消息的时候无法保证两点：
     * 1、发送时消息路由的队列还存在
     * 2、发送时原连接仍然支持回调方法
     * 原因：消息写磁盘和从磁盘读取消息发送存在时间差，两个时间点的队列和连接情况可能不同。所以不支持Mandatory设置
     *
     * @param returnedMessage
     */
    @Override
    public void returnedMessage(ReturnedMessage returnedMessage) {
        // 延时队列跳过（延时队列无论是否成功都会返回失败，这里跳过）
        if (returnedMessage.getExchange().startsWith(defaultDelayExchangeName)) {
            return;
        }
        // 设置返回回调
        log.error("消息发送到队列失败，消息id:{}", returnedMessage.getMessage().getMessageProperties().getMessageId());
        log.error("消息发送到队列失败，body：{}", new String(returnedMessage.getMessage().getBody()));
        log.error("消息发送到队列失败,返回消息：{}", returnedMessage);
    }
}
