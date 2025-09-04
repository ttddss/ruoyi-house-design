package com.ruoyi.web.controller.order;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.order.domain.req.QueryPayOrderListReq;
import com.ruoyi.order.domain.vo.PayOrderInfoVO;
import com.ruoyi.order.domain.vo.PayOrderListVO;
import com.ruoyi.order.service.IPayOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 支付订单Controller
 * 
 * @author tds
 * @date 2025-03-21
 */
@RestController
@RequestMapping("/order/payOrder")
public class PayOrderController extends BaseController
{
    @Autowired
    private IPayOrderService payOrderService;

    /**
     * 查询支付订单列表
     */
    @PreAuthorize("@ss.hasPermi('order:payOrder:list')")
    @GetMapping("/list")
    public TableDataInfo<List<PayOrderListVO>> list(QueryPayOrderListReq queryReq)
    {
        startPage();
        List<PayOrderListVO> list = payOrderService.selectPayOrderList(queryReq);
        return getDataTable(list);
    }

    /**
     * 导出支付订单列表
     */
    @PreAuthorize("@ss.hasPermi('order:payOrder:export')")
    @Log(title = "支付订单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, QueryPayOrderListReq queryReq)
    {
        List<PayOrderListVO> list = payOrderService.selectPayOrderList(queryReq);
        ExcelUtil<PayOrderListVO> util = new ExcelUtil<>(PayOrderListVO.class);
        util.exportExcel(response, list, "支付订单数据");
    }

    /**
     * 获取支付订单详细信息
     */
    @PreAuthorize("@ss.hasPermi('order:payOrder:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult<PayOrderInfoVO> getInfo(@PathVariable("id") Long id)
    {
        return success(payOrderService.selectPayOrderById(id));
    }


}
