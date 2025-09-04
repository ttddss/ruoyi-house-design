package com.ruoyi.system.domain.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 *
 *
 * @author tongdashuai
 * @version 1.0
 * @description
 * @module
 * @date 2024/8/18 17:03
 */
@Data
@Builder
public class MqMsgDTO implements Serializable {

    private static final long serialVersionUID = -6935186335604698415L;


    /**
     * 业务id
     */
    private String bizId;

    /**
     * 业务类型：0-自动订单关闭 1-查询订单状态
     */
    private Integer bizType;
}
