package com.ruoyi.system.util;

import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.json.JSONUtil;
import com.ruoyi.common.config.RabbitMqConfig;
import com.ruoyi.common.enums.YnEnum;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.RabbitUtils;
import com.ruoyi.system.domain.MqMsg;
import com.ruoyi.system.domain.dto.MqMsgDTO;
import com.ruoyi.system.enums.MsgBizTypeEnum;
import com.ruoyi.system.mapper.MqMsgMapper;

import java.util.Date;

/**
 * mq相关的业务工具类
 *
 * @author tongdashuai
 * @version 1.0
 * @description
 * @module
 * @date 2024/8/18 16:55
 */
public class MqBizUtils {

    private static MqMsgMapper MQ_MSG_MAPPER = SpringUtil.getBean(MqMsgMapper.class);

    /**
     * 发送mq延时消息
     * 该方法会记录消息到数据库
     * @param routeKey 路由名字
     * @param bizId 业务id
     * @param msgBizTypeEnum 业务类型
     * @param delaySeconds 延时时间，单位秒
     */
    public static void sendDelayMqMsg(String routeKey, String bizId, MsgBizTypeEnum msgBizTypeEnum, int delaySeconds) {
        MqMsgDTO msgDTO = MqMsgDTO.builder()
                .bizId(bizId)
                .bizType(msgBizTypeEnum.getCode())
                .build();
        sendDelayMqMsg(RabbitMqConfig.DEFAULT_DELAY_EXCHANGE_NAME, routeKey, msgDTO, delaySeconds);
    }

    /**
     * 发送mq延时消息
     * 该方法会记录消息到数据库
     * @param exchangeName 交换机名字
     * @param routeKey 路由名字
     * @param msg 消息内容
     * @param delaySeconds 延时时间，单位秒
     */
    public static void sendDelayMqMsg(String exchangeName, String routeKey, MqMsgDTO msg, int delaySeconds) {
        // 记录到数据库mq消息是防止投递mq消息失败后重试处理
        String msgJson = JSONUtil.toJsonStr(msg);
        MqMsg mqMsg = new MqMsg();
        mqMsg.setBizId(msg.getBizId());
        mqMsg.setBizType(msg.getBizType());
        mqMsg.setIsDelay(YnEnum.YES.getCode());
        mqMsg.setTimes(0L);
        mqMsg.setExchangeName(exchangeName);
        mqMsg.setRouteKey(routeKey);
        mqMsg.setRepushTime(DateUtils.addSeconds(new Date(), delaySeconds));
        mqMsg.setMsg(msgJson);
        MQ_MSG_MAPPER.insert(mqMsg);

        // 消息投递到mq
        RabbitUtils.sendToDelayQueue(exchangeName, routeKey, msgJson, delaySeconds, mqMsg.getId());
    }

    /**
     * 重新发送mq延时消息
     * 该方法会记录消息到数据库
     * @param mqMsg 原mq消息
     * @param delaySeconds 延时时间，单位秒
     */
    public static void reSendDelayMqMsg(MqMsg mqMsg, int delaySeconds) {
        mqMsg.setRepushTime(DateUtils.addSeconds(new Date(), delaySeconds));
        MQ_MSG_MAPPER.updateRepush(mqMsg);

        // 消息投递到mq
        RabbitUtils.sendToDelayQueue(mqMsg.getExchangeName(), mqMsg.getRouteKey(), mqMsg.getMsg(), delaySeconds, mqMsg.getId());
    }
}
