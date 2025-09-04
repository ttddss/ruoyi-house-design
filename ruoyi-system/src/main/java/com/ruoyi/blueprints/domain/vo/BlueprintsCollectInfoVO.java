package com.ruoyi.blueprints.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.io.Serializable;
import java.util.Date;

/**
 * 用户设计图纸收藏vo对象
 * 
 * @author tds
 * @date 2025-03-03
 */
@Data
public class BlueprintsCollectInfoVO implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 用户ID */
    private Long userId;

    /** 图纸ID */
    private Long blueprintsId;

    /** 删除标志: 0-代表存在 其他代表删除 */
    private Long delFlag;

    /** 创建人 */
    private String createBy;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createTime;

    /** 更新人 */
    private String updateBy;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date updateTime;

}
