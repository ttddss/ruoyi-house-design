package com.ruoyi.mall.controller.order;

import com.ruoyi.blueprints.convert.MallBlueprintsConvert;
import com.ruoyi.blueprints.domain.vo.MallPayOrderListVO;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.enums.OperatorType;
import com.ruoyi.common.utils.PageUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.order.domain.req.QueryPayOrderListReq;
import com.ruoyi.order.domain.vo.PayOrderListVO;
import com.ruoyi.order.enums.PayStatusEnum;
import com.ruoyi.order.service.IPayOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 商城-订单相关接口
 * 
 * @author tds
 * @date 2025-03-03
 */
@RestController
@RequestMapping("/mall/order")
public class MallOrderController extends BaseController
{
    @Autowired
    private IPayOrderService payOrderService;


    /**
     * 我的订单
     */
    @GetMapping("/myOrder")
    @Log(title = "我的订单", businessType = BusinessType.OTHER, operatorType = OperatorType.WEB, isSaveResponseData = false)
    public TableDataInfo<List<MallPayOrderListVO>> listCollect(QueryPayOrderListReq queryReq)
    {
        queryReq.setUserId(SecurityUtils.getUserId());
        queryReq.setStatus(PayStatusEnum.SUCCESS.getCode());
        startPage();
        List<PayOrderListVO> list = payOrderService.selectPayOrderList(queryReq);
        return getDataTable(PageUtils.newPage(list, MallBlueprintsConvert.INSTANCE.convertOrderList(list)));
    }


}
