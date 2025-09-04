package com.ruoyi.member.domain;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.*;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.util.Date;

/**
 * vip用户对象 vip_user
 * 
 * @author tds
 * @date 2025-03-13
 */
@TableName("vip_user")
@Data
@NoArgsConstructor
public class VipUser implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 用户id */
    private Long userId;

    /** vip id */
    private Long vipId;

    /**
     * 到期时间
     */
    private Date expireTime;

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
