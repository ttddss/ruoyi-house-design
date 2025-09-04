package com.ruoyi.system.domain.req;

import lombok.Data;
import java.io.Serializable;

/**
 * 查询评价对象
 * 
 * @author tds
 * @date 2025-04-02
 */
@Data
public class QueryEvaluateListReq implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 评分 */
    private Long score;

    /** 图纸id */
    private Long blueprintsId;

    /** 订单号 */
    private String orderNo;

    /** 用户id */
    private Long userId;

    /**
     * 排序方式：0-默认排序 1-时间排序
     */
    private Long orderWay;

    /** 最小评分 */
    private Long minScore;

    /** 最大评分 */
    private Long maxScore;



}
