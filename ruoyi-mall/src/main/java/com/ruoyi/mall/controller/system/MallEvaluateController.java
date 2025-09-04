package com.ruoyi.mall.controller.system;

import com.ruoyi.blueprints.convert.MallBlueprintsConvert;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.annotation.RepeatSubmit;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.enums.OperatorType;
import com.ruoyi.common.enums.YnEnum;
import com.ruoyi.common.utils.PageUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.convert.EvaluateConvert;
import com.ruoyi.system.domain.req.AddEvaluateReq;
import com.ruoyi.system.domain.req.QueryEvaluateListReq;
import com.ruoyi.system.domain.vo.EvaluateInfoVO;
import com.ruoyi.system.domain.vo.EvaluateListVO;
import com.ruoyi.system.domain.vo.MallEvaluateListVO;
import com.ruoyi.system.domain.vo.MallEvaluateStaticsVO;
import com.ruoyi.system.service.IEvaluateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 商城-评价Controller
 * 
 * @author tds
 * @date 2025-04-02
 */
@RestController
@RequestMapping("/mall/evaluate")
public class MallEvaluateController extends BaseController
{
    @Autowired
    private IEvaluateService evaluateService;

    private static final String ANONYMOUS_NICK_NAME = "匿名用户";

    /**
     * 查询评价列表
     */
    @GetMapping("/list")
    @Log(title = "查询评价列表", businessType = BusinessType.OTHER, operatorType = OperatorType.WEB, isSaveRequestData = false)
    public TableDataInfo<List<MallEvaluateListVO>> list(QueryEvaluateListReq queryReq)
    {
        startPage();
        List<EvaluateListVO> list = evaluateService.selectEvaluateList(queryReq);
        for (EvaluateListVO vo : list) {
            if (vo.getAnonymous() == YnEnum.YES.getCode()) {
                vo.setNickName(ANONYMOUS_NICK_NAME);
            }
        }
        return getDataTable(PageUtils.newPage(list, EvaluateConvert.INSTANCE.convertMallList(list)));
    }


    /**
     * 查询图纸评论统计
     */
    @GetMapping("/statics")
    @Log(title = "查询图纸评论统计", businessType = BusinessType.OTHER, operatorType = OperatorType.WEB)
    public AjaxResult<MallEvaluateStaticsVO> evaluateStatics(Long blueprintsId) {
        MallEvaluateStaticsVO vo = evaluateService.evaluateStatics(blueprintsId);
        return AjaxResult.success(vo);
    }

    /**
     * 新增评价
     */
    @Log(title = "评价", businessType = BusinessType.INSERT, operatorType = OperatorType.WEB)
    @RepeatSubmit
    @PostMapping
    public AjaxResult add(@Validated @RequestBody AddEvaluateReq addReq)
    {
        return toAjax(evaluateService.insertEvaluate(addReq));
    }



    /**
     * 删除评价
     */
    @Log(title = "删除评价", businessType = BusinessType.DELETE, operatorType = OperatorType.WEB)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(evaluateService.deleteEvaluateByIds(ids));
    }
}
