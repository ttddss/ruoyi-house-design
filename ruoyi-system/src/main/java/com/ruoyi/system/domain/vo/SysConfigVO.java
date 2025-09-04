package com.ruoyi.system.domain.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author tds
 * @version 1.0.0
 * @Company
 * @ClassName SysConfigVO
 * @Description TODO（这里用一句话描述这个类的作用)
 * @Date 2025-03-11 23:49
 */
@Data
public class SysConfigVO implements Serializable {

    private static final long serialVersionUID = 8627365235698166760L;

    /**
     * 客服手机号
     */
    private String customerServicePhone;

    /**
     * 客服qq
     */
    private String customerServiceQq;

    /**
     * 客服微信
     */
    private String customerServiceWx;


    /**
     * 客服工作时间
     */
    private String customerServiceWorkTime;

    /**
     * 文件上传方式：0-本地 1-阿里云oss
     */
    private String fileUploadWay;

}
