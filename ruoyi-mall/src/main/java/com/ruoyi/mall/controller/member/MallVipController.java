package com.ruoyi.mall.controller.member;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.enums.OperatorType;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.member.domain.vo.MallVipListVO;
import com.ruoyi.member.service.IVipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.PermitAll;
import java.util.List;

/**
 * 商城-vip相关接口
 * 
 * @author tds
 * @date 2025-03-13
 */
@RestController
@RequestMapping("/mall/member/vip")
public class MallVipController extends BaseController
{
    @Autowired
    private IVipService vipService;

    /**
     * 查询vip列表
     */
    @GetMapping("/list")
    @Log(title = "查询vip列表", businessType = BusinessType.OTHER, operatorType = OperatorType.WEB)
    public AjaxResult<List<MallVipListVO>> list()
    {
        List<MallVipListVO> list = vipService.selectMallVipList();
        return success(list);
    }

}
