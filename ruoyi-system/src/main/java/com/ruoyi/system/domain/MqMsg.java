package com.ruoyi.system.domain;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.*;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.util.Date;

/**
 * 队列消息对象 mq_msq
 * 
 * @author ruoyi
 * @date 2024-08-18
 */
@TableName("mq_msg")
@Data
@NoArgsConstructor
public class MqMsg implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 业务id */
    private String bizId;

    /**
     * 业务类型：0-自动订单关闭 1-查询订单状态
     */
    private Integer bizType;

    /** 交换机名字 */
    private String exchangeName;

    /** 路由键 */
    private String routeKey;

    /** 重推次数 */
    private Long times;

    /** 重新推送的时间 */
    private Date repushTime;


    /**
     * 是否是延迟队列交换机
     */
    private Integer isDelay;

    /** 消息内容 */
    private String msg;

    /** 创建人 */
    private String createBy;

    /** 创建时间 */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /** 更新人 */
    private String updateBy;

    /** 更新时间 */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;




}
