package com.ruoyi.blueprints.domain.req;

import lombok.Data;
import java.io.Serializable;

/**
 * 查询用户图纸对象
 * 
 * @author tds
 * @date 2025-03-26
 */
@Data
public class QueryBlueprintsUserListReq implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 图纸id */
    private Long blueprintsId;

    /** 用户ID */
    private Long userId;

}
