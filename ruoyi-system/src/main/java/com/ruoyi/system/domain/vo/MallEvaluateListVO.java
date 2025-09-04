package com.ruoyi.system.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 评价集合vo对象
 * 
 * @author tds
 * @date 2025-04-02
 */
@Data
public class MallEvaluateListVO implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 评分 */
    private Long score;

    /** 图纸id */
    private Long blueprintsId;

    /** 图纸名称 */
    private String blueprintsName;

    /** 订单号 */
    private String orderNo;

    /** 用户id */
    private Long userId;

    /** 用户昵称 */
    private String nickName;

    /** 用户头像 */
    private String avatar;

    /** 图片 */
    private String imageUrl;

    /** 是否匿名 */
    private Integer anonymous;

    /** 评价内容 */
    private String content;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createTime;

}
