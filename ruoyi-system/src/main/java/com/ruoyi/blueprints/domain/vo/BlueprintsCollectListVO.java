package com.ruoyi.blueprints.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import com.ruoyi.common.annotation.Excel;
import java.io.Serializable;
import java.util.Date;

/**
 * 用户设计图纸收藏集合vo对象
 * 
 * @author tds
 * @date 2025-03-03
 */
@Data
public class BlueprintsCollectListVO implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 用户ID */
    @Excel(name = "用户ID")
    private Long userId;

    /** 图纸ID */
    @Excel(name = "图纸ID")
    private Long blueprintsId;

}
