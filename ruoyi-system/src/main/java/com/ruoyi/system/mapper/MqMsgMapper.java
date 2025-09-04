package com.ruoyi.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.system.domain.MqMsg;


/**
 * 队列消息Mapper接口
 * 
 * @author ruoyi
 * @date 2024-08-18
 */
public interface MqMsgMapper extends BaseMapper<MqMsg>
{


    /**
     * 对mq消息备份
     *
     * @param id
     */
    void bak(Long id);

    /**
     * 重推mq消息更新
     *
     * @param mqMsg
     */
    int updateRepush(MqMsg mqMsg);
}
