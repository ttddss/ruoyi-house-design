package com.ruoyi.mall.controller.common;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.domain.vo.AppInfoVO;
import com.ruoyi.system.service.ICommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 商城通用Controller
 * 
 * @author tds
 * @date 2025-03-03
 */
@RestController
@RequestMapping("/mall/common")
public class MallCommonController extends BaseController
{
    @Autowired
    private ICommonService commonService;


    /**
     * 获取应用信息
     * @return
     */
    @GetMapping("appInfo")
    public AjaxResult<AppInfoVO> getAppInfo() {
        // 获取应用信息
        AppInfoVO appInfo = commonService.getAppInfo();
        return AjaxResult.success(appInfo);
    }

}
