package com.ruoyi.system.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 评价vo对象
 * 
 * @author tds
 * @date 2025-04-02
 */
@Data
public class EvaluateInfoVO implements Serializable
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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /** 更新人 */
    private String updateBy;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date updateTime;

    /** 删除标志: 0-代表存在 其他代表删除 */
    private Long delFlag;

}
