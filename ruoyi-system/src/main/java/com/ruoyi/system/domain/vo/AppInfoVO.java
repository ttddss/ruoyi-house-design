package com.ruoyi.system.domain.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author tds
 * @version 1.0.0
 * @Company
 * @ClassName AppInfoVO
 * @Description TODO（这里用一句话描述这个类的作用)
 * @Date 2025-03-11 23:43
 */
@Data
public class AppInfoVO implements Serializable {

    private static final long serialVersionUID = -499549753356618266L;

    /**
     * 应用配置信息
     */
    private SysConfigVO config;
}
