package com.ruoyi.common.core.domain.dto;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;


@Data
@NoArgsConstructor
public class StudentUserDTO implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 名称 */
    private String name;

    /** 学员编号 */
    private String studentNo;

    /** 性别：0-男 1-女 2-未知 */
    private Integer sex;

    /**
     * 年龄
     */
    private Integer age;

    /** 手机号码 */
    private String phoneNumber;

    /** 身份证号 */
    private String idNumber;

    /** 家属姓名 */
    private String relativeName;

    /** 家属手机号 */
    private String relativePhoneNumber;

    /** 家庭住址 */
    private String address;

    /** 备注 */
    private String remark;

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

    /** 删除标志：0-未删除 其他-已删除 */
    private Long delFlag;




}
