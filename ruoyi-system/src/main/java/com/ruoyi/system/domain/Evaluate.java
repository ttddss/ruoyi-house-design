package com.ruoyi.system.domain;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.*;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.util.Date;

/**
 * 评价对象 evaluate
 * 
 * @author tds
 * @date 2025-04-02
 */
@TableName("evaluate")
@Data
@NoArgsConstructor
public class Evaluate implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 评分 */
    private Long score;

    /** 图纸id */
    private Long blueprintsId;

    /** 订单号 */
    private String orderNo;

    /** 用户id */
    private Long userId;

    /** 图片 */
    private String imageUrl;

    /** 是否匿名 */
    private Integer anonymous;

    /** 评价内容 */
    private String content;

    /** 备注 */
    private String remark;

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

    /** 删除标志: 0-代表存在 其他代表删除 */
    private Long delFlag;




}
