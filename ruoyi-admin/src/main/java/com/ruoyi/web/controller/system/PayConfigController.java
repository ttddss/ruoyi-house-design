package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.req.AddPayConfigReq;
import com.ruoyi.system.domain.req.EditPayConfigReq;
import com.ruoyi.system.domain.req.QueryPayConfigListReq;
import com.ruoyi.system.domain.vo.PayConfigInfoVO;
import com.ruoyi.system.domain.vo.PayConfigListVO;
import com.ruoyi.system.service.IPayConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 支付配置Controller
 * 
 * @author tds
 * @date 2025-03-21
 */
@RestController
@RequestMapping("/system/payConfig")
public class PayConfigController extends BaseController
{
    @Autowired
    private IPayConfigService payConfigService;

    /**
     * 查询支付配置列表
     */
    @PreAuthorize("@ss.hasPermi('system:payConfig:list')")
    @GetMapping("/list")
    public TableDataInfo<List<PayConfigListVO>> list(QueryPayConfigListReq queryReq)
    {
        startPage();
        List<PayConfigListVO> list = payConfigService.selectPayConfigList(queryReq);
        return getDataTable(list);
    }

    /**
     * 导出支付配置列表
     */
    @PreAuthorize("@ss.hasPermi('system:payConfig:export')")
    @Log(title = "支付配置", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, QueryPayConfigListReq queryReq)
    {
        List<PayConfigListVO> list = payConfigService.selectPayConfigList(queryReq);
        ExcelUtil<PayConfigListVO> util = new ExcelUtil<>(PayConfigListVO.class);
        util.exportExcel(response, list, "支付配置数据");
    }

    /**
     * 获取支付配置详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:payConfig:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult<PayConfigInfoVO> getInfo(@PathVariable("id") Long id)
    {
        return success(payConfigService.selectPayConfigById(id));
    }

    /**
     * 新增支付配置
     */
    @PreAuthorize("@ss.hasPermi('system:payConfig:add')")
    @Log(title = "支付配置", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody AddPayConfigReq addReq)
    {
        return toAjax(payConfigService.insertPayConfig(addReq));
    }

    /**
     * 修改支付配置
     */
    @PreAuthorize("@ss.hasPermi('system:payConfig:edit')")
    @Log(title = "支付配置", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody EditPayConfigReq editReq)
    {
        return toAjax(payConfigService.updatePayConfig(editReq));
    }

    /**
     * 删除支付配置
     */
    @PreAuthorize("@ss.hasPermi('system:payConfig:remove')")
    @Log(title = "支付配置", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(payConfigService.deletePayConfigByIds(ids));
    }
}
