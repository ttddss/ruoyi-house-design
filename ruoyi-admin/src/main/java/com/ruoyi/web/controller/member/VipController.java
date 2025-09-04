package com.ruoyi.web.controller.member;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.member.domain.req.AddVipReq;
import com.ruoyi.member.domain.req.EditVipReq;
import com.ruoyi.member.domain.req.QueryVipListReq;
import com.ruoyi.member.domain.req.UpdateVipStatusReq;
import com.ruoyi.member.domain.vo.VipInfoVO;
import com.ruoyi.member.domain.vo.VipListVO;
import com.ruoyi.member.service.IVipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * vipController
 * 
 * @author tds
 * @date 2025-03-13
 */
@RestController
@RequestMapping("/member/vip")
public class VipController extends BaseController
{
    @Autowired
    private IVipService vipService;

    /**
     * 查询vip列表
     */
    @PreAuthorize("@ss.hasPermi('member:vip:list')")
    @GetMapping("/list")
    public TableDataInfo<List<VipListVO>> list(QueryVipListReq queryReq)
    {
        startPage();
        List<VipListVO> list = vipService.selectVipList(queryReq);
        return getDataTable(list);
    }

    /**
     * 导出vip列表
     */
    @PreAuthorize("@ss.hasPermi('member:vip:export')")
    @Log(title = "vip", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, QueryVipListReq queryReq)
    {
        List<VipListVO> list = vipService.selectVipList(queryReq);
        ExcelUtil<VipListVO> util = new ExcelUtil<>(VipListVO.class);
        util.exportExcel(response, list, "vip数据");
    }

    /**
     * 获取vip详细信息
     */
    @PreAuthorize("@ss.hasPermi('member:vip:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult<VipInfoVO> getInfo(@PathVariable("id") Long id)
    {
        return success(vipService.selectVipById(id));
    }

    /**
     * 新增vip
     */
    @PreAuthorize("@ss.hasPermi('member:vip:add')")
    @Log(title = "vip", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody AddVipReq addReq)
    {
        return toAjax(vipService.insertVip(addReq));
    }

    /**
     * 修改vip
     */
    @PreAuthorize("@ss.hasPermi('member:vip:edit')")
    @Log(title = "vip", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody EditVipReq editReq)
    {
        return toAjax(vipService.updateVip(editReq));
    }

    /**
     * 更新vip状态
     */
    @PreAuthorize("@ss.hasPermi('member:vip:edit')")
    @Log(title = "更新vip状态", businessType = BusinessType.UPDATE)
    @PutMapping("updateStatus")
    public AjaxResult updateStatus(@Validated @RequestBody UpdateVipStatusReq updateVipStatusReq)
    {
        return toAjax(vipService.updateStatus(updateVipStatusReq));
    }

    /**
     * 删除vip
     */
    @PreAuthorize("@ss.hasPermi('member:vip:remove')")
    @Log(title = "vip", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(vipService.deleteVipByIds(ids));
    }
}
