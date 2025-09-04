package com.ruoyi.system.domain.req;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.util.Date;

/**
 * 新增评价对象
 * 
 * @author tds
 * @date 2025-04-02
 */
@Data
public class AddEvaluateReq implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 评分 */
    private Long score;

    /** 图纸id */
    private Long blueprintsId;

    /** 订单号 */
    private String orderNo;

    /** 图片 */
    private String imageUrl;

    /** 是否匿名 */
    private Integer anonymous;

    /** 评价内容 */
    private String content;


}
