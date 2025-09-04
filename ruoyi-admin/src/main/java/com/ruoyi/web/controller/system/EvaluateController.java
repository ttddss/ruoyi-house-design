package com.ruoyi.web.controller.system;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.Evaluate;
import com.ruoyi.system.service.IEvaluateService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.system.domain.req.AddEvaluateReq;
import com.ruoyi.system.domain.req.EditEvaluateReq;
import com.ruoyi.system.domain.req.QueryEvaluateListReq;
import com.ruoyi.system.domain.vo.EvaluateInfoVO;
import com.ruoyi.system.domain.vo.EvaluateListVO;
import org.springframework.validation.annotation.Validated;

/**
 * 评价Controller
 * 
 * @author tds
 * @date 2025-04-02
 */
@RestController
@RequestMapping("/system/evaluate")
public class EvaluateController extends BaseController
{
    @Autowired
    private IEvaluateService evaluateService;

    /**
     * 查询评价列表
     */
    @PreAuthorize("@ss.hasPermi('system:evaluate:list')")
    @GetMapping("/list")
    public TableDataInfo<List<EvaluateListVO>> list(QueryEvaluateListReq queryReq)
    {
        startPage();
        List<EvaluateListVO> list = evaluateService.selectEvaluateList(queryReq);
        return getDataTable(list);
    }

    /**
     * 导出评价列表
     */
    @PreAuthorize("@ss.hasPermi('system:evaluate:export')")
    @Log(title = "评价", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, QueryEvaluateListReq queryReq)
    {
        List<EvaluateListVO> list = evaluateService.selectEvaluateList(queryReq);
        ExcelUtil<EvaluateListVO> util = new ExcelUtil<>(EvaluateListVO.class);
        util.exportExcel(response, list, "评价数据");
    }

    /**
     * 获取评价详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:evaluate:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult<EvaluateInfoVO> getInfo(@PathVariable("id") Long id)
    {
        return success(evaluateService.selectEvaluateById(id));
    }



    /**
     * 删除评价
     */
    @PreAuthorize("@ss.hasPermi('system:evaluate:remove')")
    @Log(title = "评价", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(evaluateService.deleteEvaluateByIds(ids));
    }
}
