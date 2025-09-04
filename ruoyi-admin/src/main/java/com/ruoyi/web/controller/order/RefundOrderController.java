package com.ruoyi.web.controller.order;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.order.domain.req.QueryRefundOrderListReq;
import com.ruoyi.order.domain.vo.RefundOrderInfoVO;
import com.ruoyi.order.domain.vo.RefundOrderListVO;
import com.ruoyi.order.service.IRefundOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 退款单Controller
 * 
 * @author tds
 * @date 2025-03-21
 */
@RestController
@RequestMapping("/order/refundOrder")
public class RefundOrderController extends BaseController
{
    @Autowired
    private IRefundOrderService refundOrderService;

    /**
     * 查询退款单列表
     */
    @PreAuthorize("@ss.hasPermi('order:refundOrder:list')")
    @GetMapping("/list")
    public TableDataInfo<List<RefundOrderListVO>> list(QueryRefundOrderListReq queryReq)
    {
        startPage();
        List<RefundOrderListVO> list = refundOrderService.selectRefundOrderList(queryReq);
        return getDataTable(list);
    }

    /**
     * 导出退款单列表
     */
    @PreAuthorize("@ss.hasPermi('order:refundOrder:export')")
    @Log(title = "退款单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, QueryRefundOrderListReq queryReq)
    {
        List<RefundOrderListVO> list = refundOrderService.selectRefundOrderList(queryReq);
        ExcelUtil<RefundOrderListVO> util = new ExcelUtil<>(RefundOrderListVO.class);
        util.exportExcel(response, list, "退款单数据");
    }

    /**
     * 获取退款单详细信息
     */
    @PreAuthorize("@ss.hasPermi('order:refundOrder:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult<RefundOrderInfoVO> getInfo(@PathVariable("id") Long id)
    {
        return success(refundOrderService.selectRefundOrderById(id));
    }

}
