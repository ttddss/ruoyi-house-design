package com.ruoyi.system.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import com.ruoyi.common.annotation.Excel;
import java.io.Serializable;
import java.util.Date;

/**
 * 评价集合vo对象
 * 
 * @author tds
 * @date 2025-04-02
 */
@Data
public class EvaluateListVO implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    @Excel(name = "主键")
    private Long id;

    /** 评分 */
    @Excel(name = "评分")
    private Long score;

    /** 图纸id */
    private Long blueprintsId;

    /** 图纸名称 */
    @Excel(name = "图纸名称")
    private String blueprintsName;

    /** 订单号 */
    @Excel(name = "订单号")
    private String orderNo;

    /** 用户id */
    private Long userId;

    /** 用户昵称 */
    @Excel(name = "用户昵称")
    private String nickName;

    /** 用户头像 */
    private String avatar;

    /** 图片 */
    @Excel(name = "图片")
    private String imageUrl;

    /** 是否匿名 */
    @Excel(name = "是否匿名")
    private Integer anonymous;

    /** 评价内容 */
    @Excel(name = "评价内容")
    private String content;

    /** 备注 */
    @Excel(name = "备注")
    private String remark;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

}
