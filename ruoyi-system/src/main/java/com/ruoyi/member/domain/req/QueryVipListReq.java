package com.ruoyi.member.domain.req;

import lombok.Data;
import java.io.Serializable;

/**
 * 查询vip对象
 * 
 * @author tds
 * @date 2025-03-13
 */
@Data
public class QueryVipListReq implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 名称 */
    private String name;

    /** 状态 */
    private Integer status;

    /**
     * 用户id
     */
    private Long userId;

}
