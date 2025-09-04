package com.ruoyi.system.task;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruoyi.common.enums.YnEnum;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.domain.MqMsg;
import com.ruoyi.system.mapper.MqMsgMapper;
import com.ruoyi.system.util.MqBizUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 *
 *
 * @author tongdashuai
 * @version 1.0
 * @description
 * @module
 * @date 2024/8/18 18:49
 */
@Component
@Slf4j
public class SysTask {

    @Autowired
    private MqMsgMapper mqMsgMapper;

    /**
     * 对mq消息重新推送到队列
     */
    public void mqDelayMsgReppush() {
        log.info("------------------- 开始mq延时队列消息重新推送定时任务 ----------------------");
        try {
            // 查询未发送过的（times=0）延时队列消息，且已经过了处理时间10分钟了的mq消息
            List<MqMsg> mqMsgs = mqMsgMapper.selectList(new LambdaQueryWrapper<MqMsg>()
                    .eq(MqMsg::getTimes, 0)
                    .eq(MqMsg::getIsDelay, YnEnum.YES.getCode())
                    .lt(MqMsg::getRepushTime, DateUtils.addMinutes(new Date(), -10)));
            for (MqMsg mqMsg : mqMsgs) {
                try {
                    MqBizUtils.reSendDelayMqMsg(mqMsg, 0);
                } catch (Exception e) {
                    log.error("处理单条mq消息推送到队列处理异常，异常信息：", e);
                    log.error("处理单条mq消息推送到队列处理异常，msgId:{}：", mqMsg.getId());
                }
            }

            log.info("------------------- mq消息重新推送定时任务完成 ----------------------");
        } catch (Exception e) {
            log.error("处理mq消息推送到队列处理异常，异常信息：", e);
        }
    }
}
