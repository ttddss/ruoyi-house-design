package com.ruoyi.web.controller.blueprints;

import com.ruoyi.blueprints.domain.req.AddBlueprintsReq;
import com.ruoyi.blueprints.domain.req.ChangeBlueprintsListingStatusReq;
import com.ruoyi.blueprints.domain.req.EditBlueprintsReq;
import com.ruoyi.blueprints.domain.req.QueryBlueprintsListReq;
import com.ruoyi.blueprints.domain.vo.BlueprintsInfoVO;
import com.ruoyi.blueprints.domain.vo.BlueprintsListVO;
import com.ruoyi.blueprints.service.IBlueprintsService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 图纸列表Controller
 * 
 * @author tds
 * @date 2025-03-03
 */
@RestController
@RequestMapping("/blueprints/blueprints")
public class BlueprintsController extends BaseController
{
    @Autowired
    private IBlueprintsService blueprintsService;

    /**
     * 查询图纸列表列表
     */
    @PreAuthorize("@ss.hasPermi('blueprints:blueprints:list')")
    @GetMapping("/list")
    public TableDataInfo<List<BlueprintsListVO>> list(QueryBlueprintsListReq queryReq)
    {
        startPage();
        List<BlueprintsListVO> list = blueprintsService.selectBlueprintsList(queryReq);
        return getDataTable(list);
    }

    /**
     * 导出图纸列表列表
     */
    @PreAuthorize("@ss.hasPermi('blueprints:blueprints:export')")
    @Log(title = "图纸列表", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, QueryBlueprintsListReq queryReq)
    {
        List<BlueprintsListVO> list = blueprintsService.selectBlueprintsList(queryReq);
        ExcelUtil<BlueprintsListVO> util = new ExcelUtil<>(BlueprintsListVO.class);
        util.exportExcel(response, list, "图纸列表数据");
    }

    /**
     * 获取图纸列表详细信息
     */
    @PreAuthorize("@ss.hasPermi('blueprints:blueprints:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult<BlueprintsInfoVO> getInfo(@PathVariable("id") Long id)
    {
        return success(blueprintsService.selectBlueprintsById(id));
    }

    /**
     * 新增图纸列表
     */
    @PreAuthorize("@ss.hasPermi('blueprints:blueprints:add')")
    @Log(title = "图纸列表", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody AddBlueprintsReq addReq)
    {
        return toAjax(blueprintsService.insertBlueprints(addReq));
    }

    /**
     * 修改图纸列表
     */
    @PreAuthorize("@ss.hasPermi('blueprints:blueprints:edit')")
    @Log(title = "图纸列表", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody EditBlueprintsReq editReq)
    {
        return toAjax(blueprintsService.updateBlueprints(editReq));
    }

    /**
     * 删除图纸列表
     */
    @PreAuthorize("@ss.hasPermi('blueprints:blueprints:remove')")
    @Log(title = "图纸列表", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(blueprintsService.deleteBlueprintsByIds(ids));
    }


    /**
     * 修改图纸列表
     */
    @PreAuthorize("@ss.hasPermi('blueprints:blueprints:edit')")
    @Log(title = "图纸列表", businessType = BusinessType.UPDATE)
    @PostMapping("/changeListingStatus")
    public AjaxResult changeListingStatus(@Validated @RequestBody ChangeBlueprintsListingStatusReq changeListingStatusReq)
    {
        blueprintsService.changeListingStatus(changeListingStatusReq);
        return success();
    }
}
