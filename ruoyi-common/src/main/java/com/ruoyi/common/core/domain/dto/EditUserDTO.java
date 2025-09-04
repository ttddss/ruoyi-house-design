package com.ruoyi.common.core.domain.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户对象 sys_user
 * 
 * @author ruoyi
 */
@Data
public class EditUserDTO implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 用户ID */
    private Long userId;



    /** 用户昵称 */
    private String nickName;

    /** 用户邮箱 */
    private String email;

    /** 手机号码 */
    private String phonenumber;

    /** 用户性别 */
    private String sex;

    /**
     * 年龄
     */
    private Integer age;

}
