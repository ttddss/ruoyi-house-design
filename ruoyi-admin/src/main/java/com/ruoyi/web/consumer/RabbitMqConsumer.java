package com.ruoyi.web.consumer;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.rabbitmq.client.Channel;
import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.utils.LockUtils;
import com.ruoyi.common.utils.function.CustomConsumer;
import com.ruoyi.framework.aspectj.MdcUtil;
import com.ruoyi.order.domain.PayOrder;
import com.ruoyi.order.mapper.PayOrderMapper;
import com.ruoyi.order.service.IPayOrderService;
import com.ruoyi.pay.service.PayManager;
import com.ruoyi.system.domain.MqMsg;
import com.ruoyi.system.domain.dto.MqMsgDTO;
import com.ruoyi.order.enums.PayStatusEnum;
import com.ruoyi.system.enums.PayChannelEnum;
import com.ruoyi.system.mapper.MqMsgMapper;
import com.ruoyi.system.util.MqBizUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * rabbitmq消息消费者
 *
 * @author tongdashuai
 * @version 1.0
 * @description
 * @module
 * @date 2024/8/18 17:23
 */
@Component
@Slf4j
public class RabbitMqConsumer {

    /**
     * 默认的最大重试次数
     */
    private static final int MAX_RETRY_TIMES = 3;

    @Resource
    private MqMsgMapper mqMsgMapper;

    @Autowired
    private IPayOrderService payOrderService;

    @Autowired
    private PayOrderMapper payOrderMapper;

    @Autowired
    private PayManager payManager;


    /**
     * 自动关闭订单消息消费
     */
    // @RabbitListener(bindings = @QueueBinding(value = @Queue(value = "#{canteenOrderQueue.name}"), exchange = @Exchange(value = "#{canteenOrderQueue.name}", delayed = "true"), key = {"canteenOrderQueue.name"}))
    @RabbitListener(queues = {"#{autoCloseOrderQueue.name}"}, concurrency = "5")
    public void autoCLoseOrderConsume(byte[] msg, Message message, Channel channel)  {
        commonDelayMsgHandle(msg, message, channel, "自动关闭订单",  (msgDto) -> {
            // 订单处理加锁
            LockUtils.tryLock(CacheConstants.ORDER_LOCK_KEY + msgDto.getBizId(), "订单处理中，请稍后重试",
                    msgDto.getBizId(), (t) -> {
                        // 查询订单信息
                        PayOrder payOrder = payOrderService.getOne(new LambdaQueryWrapper<PayOrder>()
                                .eq(PayOrder::getOrderNo, t));
                        if (PayStatusEnum.PAYING.getCode() != payOrder.getStatus()) {
                            log.info("该订单无法关闭，订单状态:{}-{}", payOrder.getStatus(), PayStatusEnum.getNameByCode(payOrder.getStatus()));
                            return;
                        }
                        // 自动关闭订单
                        payOrderMapper.closeOrder(t);
                    });
        });
    }



    /**
     * 查询订单状态消息消费
     */
    @RabbitListener(queues = {"#{queryOrderQueue.name}"}, concurrency = "5")
    public void queryOrderStatusConsume(byte[] msg, Message message, Channel channel)  {
        commonDelayMsgHandle(msg, message, channel, "查询订单状态",  (msgDto) -> {
            // 查询订单状态处理
            payManager.refreshOrder(msgDto.getBizId());
        });
    }

    /**
     * 查询退款单状态消息消费
     */
    @RabbitListener(queues = {"#{queryRefundQueue.name}"}, concurrency = "5")
    public void queryRefundStatusConsume(byte[] msg, Message message, Channel channel)  {
        commonDelayMsgHandle(msg, message, channel, "查询退款单状态",  (msgDto) -> {
            // 查询订单状态处理
            payManager.refreshRefund(msgDto.getBizId());
        });
    }

    /**
     * 通用的延时消息处理
     * @param msg
     * @param message
     * @param channel
     * @param note 说明
     * @param consumer
     */
    private  void commonDelayMsgHandle(byte[] msg, Message message, Channel channel, String note, CustomConsumer<MqMsgDTO> consumer) {
        MdcUtil.putTraceIdIfTraceIdIsNull();
        String msgStr = new String(msg);
        log.info("{}监听的消息:{}", note, msgStr);
        String messageId = message.getMessageProperties().getMessageId();
        log.info("{}监听的消息id:{}", note, messageId);
        if (messageId == null) {
            log.error("消息id为空");
            return;
        }
        // 查询消息
        MqMsg mqMsg = mqMsgMapper.selectById(Long.valueOf(messageId));
        if (mqMsg == null) {
            log.error("mq消息不存在：{}", messageId);
            return;
        }
        log.info("第{}次处理消息", mqMsg.getTimes() + 1);

        try {
            // 将msg转为bean
            MqMsgDTO msgDto = JSONUtil.toBean(msgStr, MqMsgDTO.class);

            consumer.accept(msgDto);

            // 删除mq消息
            mqMsgMapper.bak(mqMsg.getId());
            mqMsgMapper.deleteById(mqMsg.getId());
        } catch (Exception e) {
            log.error(note + "消息处理异常，错误堆栈信息：", e);
            log.error("{}消息处理异常，消息：{}", note, msgStr);

            if (mqMsg.getTimes() >= MAX_RETRY_TIMES) {
                log.error("已经超过最大可重试次数，不在重试，请调查处理，msgId:{}", mqMsg.getId());
                return;
            }

            // times分钟后重试
            MqBizUtils.reSendDelayMqMsg(mqMsg, (int) (60 * (mqMsg.getTimes() + 1)));
        } finally {
            // 如果手动ACK,消息会被监听消费,但是消息在队列中依旧存在,如果 未配置 acknowledge-mode 默认是会在消费完毕后自动ACK掉
            // 通知 MQ 消息已被成功消费,可以ACK了
            try {
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            } catch (IOException e) {
                log.error(note + "消息ack失败，错误堆栈信息：", e);
                log.error("{}消息ack失败，消息：{}", note, msgStr);
            }
        }
    }
}
