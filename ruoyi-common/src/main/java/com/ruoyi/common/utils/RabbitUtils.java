package com.ruoyi.common.utils;

import cn.hutool.extra.spring.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AbstractExchange;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

/**
 *
 *
 * @author sun
 * @version 1.0
 * @description
 * @module
 * @date 2019/3/27
 */
@Slf4j
public class RabbitUtils {

    private static RabbitTemplate RABBIT_TEMPLATE = SpringUtil.getBean(RabbitTemplate.class);


    private static  RabbitAdmin RABBIT_ADMIN = SpringUtil.getBean(RabbitAdmin.class);



    /**
     * 给queue发送延迟消息
     *
     * @param exchangeName 交换机名称
     * @param queueName    队列名称
     * @param msg          信息
     * @param seconds      延迟消息，单位秒
     * @param msgId 消息id
     */
    public static String sendToDelayQueue(String exchangeName, String queueName, String msg, int seconds, Object msgId) {
        String messageId = msgId.toString();
        log.info("messageid:{}", messageId);
        Message message = MessageBuilder
                .withBody(msg.getBytes())
                .setContentEncoding("utf-8")
                .setMessageId(messageId == null ? null : messageId.toString())
                .build();
        message.getMessageProperties().setDelay( seconds * 1000);
        RABBIT_TEMPLATE.convertAndSend(exchangeName, queueName, message, new CorrelationData(messageId));
        return messageId;
    }

    /**
     * 给direct交换机指定queue发送消息
     *
     * @param exchangeName 交换机名称
     * @param queueName 队列名称
     * @param msg 消息内容
     * @param msgId 消息id
     */
    public static void sendToQueue(String exchangeName, String queueName, String msg, Object msgId) {
        String messageId =  msgId.toString();
        log.info("messageId:{}", messageId);
        //设置消息内容的类型，默认是 application/octet-stream 会是 ASCII 码值
        Message message = MessageBuilder.withBody(msg.getBytes())
                .setContentEncoding("utf-8")
                .setMessageId(messageId)
                .build();
        RABBIT_TEMPLATE.convertAndSend(exchangeName, queueName, message, new CorrelationData(messageId));
    }





//    /**
//     * 给queue发送消息
//     *
//     * @param exchangeName 交换机名称
//     * @param queueName    队列名称
//     * @param msg          信息
//     */
//    public static void sendToQueue(String exchangeName, String queueName, String msg) {
//        sendToQueue(new DirectExchange(exchangeName), queueName, msg);
//    }
//
//    /**
//     * 给queue发送延迟消息
//     *
//     * @param exchangeName 交换机名称
//     * @param queueName    队列名称
//     * @param msg          信息
//     * @param seconds      秒
//     */
//    public static void sendToDelayQueue(String exchangeName, String queueName, String msg, int seconds) {
//        Map<String, Object> params = new HashMap<>();
//        params.put("x-delayed-type", "direct");
//        //参数二为类型：必须是x-delayed-message
//        sendToDelayQueue(new CustomExchange(exchangeName, "x-delayed-message", true, false, params), queueName, msg, seconds);
//    }
//
//    /**
//     * 给direct交换机指定queue发送消息
//     *
//     * @param directExchange
//     * @param queueName
//     * @param msg
//     */
//    public static void sendToQueue(DirectExchange directExchange, String queueName, String msg) {
//        String messageId = queueName + "-" + IdUtil.fastSimpleUUID();
//        log.info("messageId:{}", messageId);
//        //设置消息内容的类型，默认是 application/octet-stream 会是 ASCII 码值
//        Message message = MessageBuilder.withBody(msg.getBytes())
//                .setContentEncoding("utf-8")
//                .setMessageId(messageId)
//                .build();
//        RABBIT_TEMPLATE.convertAndSend(directExchange.getName(), queueName, message, new CorrelationData(messageId));
//    }
//
//
//    /**
//     * 给延时交换机指定queue发送消息
//     *
//     * @param customExchange
//     * @param queueName
//     * @param msg
//     * @param seconds
//     */
//    public static void sendToDelayQueue(CustomExchange customExchange, String queueName, String msg, int seconds) {
////        RABBIT_ADMIN.declareExchange(customExchange);
////        Queue queue = new Queue(queueName);
////        RABBIT_ADMIN.declareQueue(queue);
////        Binding binding = BindingBuilder.bind(queue).to(customExchange).with(queueName).noargs();
////        RABBIT_ADMIN.declareBinding(binding);
//
//        //设置消息内容的类型，默认是 application/octet-stream 会是 ASCII 码值
//        String messageId = queueName + "-" + IdUtil.fastSimpleUUID();
//        log.info("messageid:{}", messageId);
//        Message message = MessageBuilder
//                .withBody(msg.getBytes())
//                .setContentEncoding("utf-8")
//                .setMessageId(messageId)
//                .setHeader("x-delay", seconds * 1000)
//                .build();
//        RABBIT_TEMPLATE.convertAndSend(customExchange.getName(), queueName, message, new CorrelationData(messageId));
//    }

    /**
     * 创建Exchange
     *
     * @param exchange
     */
    public static void addExchange(AbstractExchange exchange) {
        RABBIT_ADMIN.declareExchange(exchange);
    }

    /**
     * 创建一个指定的Queue
     *
     * @param queue
     * @return queueName
     */
    public static String addQueue(Queue queue) {
        return RABBIT_ADMIN.declareQueue(queue);
    }

}
