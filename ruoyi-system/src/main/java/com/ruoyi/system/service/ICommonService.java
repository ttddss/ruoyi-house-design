package com.ruoyi.system.service;

import com.ruoyi.system.domain.vo.AppInfoVO;

/**
 * @author tds
 * @version 1.0.0
 * @Company
 * @ClassName ICommonService
 * @Description 通用业务功能
 * @Date 2025-03-11 23:40
 */
public interface ICommonService {


    /**
     * 获取应用信息
     * @return
     */
    AppInfoVO getAppInfo();
}
