package com.ruoyi.system.service.impl;

import com.ruoyi.system.domain.vo.AppInfoVO;
import com.ruoyi.system.domain.vo.SysConfigVO;
import com.ruoyi.system.enums.ConfigEnum;
import com.ruoyi.system.mapper.SysConfigMapper;
import com.ruoyi.system.service.ICommonService;
import com.ruoyi.system.service.ISysConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author tds
 * @version 1.0.0
 * @Company
 * @ClassName CommonServiceImpl
 * @Description TODO（这里用一句话描述这个类的作用)
 * @Date 2025-03-11 23:40
 */
@Service
public class CommonServiceImpl implements ICommonService {

    @Autowired
    private ISysConfigService sysConfigService;

    @Override
    public AppInfoVO getAppInfo() {
        AppInfoVO appInfo = new AppInfoVO();

        SysConfigVO config = new SysConfigVO();
        appInfo.setConfig(config);
        config.setCustomerServicePhone(sysConfigService.selectConfigByKey(ConfigEnum.CUSTOMER_SERVICE_PHONE.getCode()));
        config.setCustomerServiceQq(sysConfigService.selectConfigByKey(ConfigEnum.CUSTOMER_SERVICE_QQ.getCode()));
        config.setCustomerServiceWx(sysConfigService.selectConfigByKey(ConfigEnum.CUSTOMER_SERVICE_WX.getCode()));
        config.setCustomerServiceWorkTime(sysConfigService.selectConfigByKey(ConfigEnum.CUSTOMER_SERVICE_WORK_TIME.getCode()));
        config.setFileUploadWay(sysConfigService.selectConfigByKey(ConfigEnum.File_UPLOAD_WAY.getCode()));

        return appInfo;
    }
}
