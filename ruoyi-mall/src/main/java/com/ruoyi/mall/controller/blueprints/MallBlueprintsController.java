package com.ruoyi.mall.controller.blueprints;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruoyi.blueprints.convert.MallBlueprintsConvert;
import com.ruoyi.blueprints.domain.BlueprintsUser;
import com.ruoyi.blueprints.domain.req.QueryBlueprintsListReq;
import com.ruoyi.blueprints.domain.vo.*;
import com.ruoyi.blueprints.service.IBlueprintsService;
import com.ruoyi.blueprints.service.IBlueprintsUserService;
import com.ruoyi.blueprints.service.IBlueprintsViewService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.annotation.RepeatSubmit;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.enums.OperatorType;
import com.ruoyi.common.utils.PageUtils;
import com.ruoyi.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 商城-图纸相关接口
 * 
 * @author tds
 * @date 2025-03-03
 */
@RestController
@RequestMapping("/mall/blueprints")
public class MallBlueprintsController extends BaseController
{
    @Autowired
    private IBlueprintsService blueprintsService;

    @Autowired
    private IBlueprintsViewService blueprintsViewService;

    @Autowired
    private IBlueprintsUserService blueprintsUserService;

    /**
     * 查询图纸列表
     */
    @GetMapping("/list")
    @Log(title = "查询图纸列表", businessType = BusinessType.OTHER, operatorType = OperatorType.WEB, isSaveResponseData = false)
    public TableDataInfo<List<MallBlueprintsListVO>> list(QueryBlueprintsListReq queryReq)
    {
        startPage();
        List<BlueprintsListVO> list = blueprintsService.selectBlueprintsList(queryReq);
        return getDataTable(PageUtils.newPage(list, MallBlueprintsConvert.INSTANCE.convert(list)));
    }

    /**
     * 我的收藏图纸
     */
    @GetMapping("/myCollect")
    @Log(title = "我的收藏图纸", businessType = BusinessType.OTHER, operatorType = OperatorType.WEB, isSaveResponseData = false)
    public TableDataInfo<List<MallBlueprintsListVO>> listCollect(QueryBlueprintsListReq queryReq)
    {
        queryReq.setCollectUserId(SecurityUtils.getUserId());
        startPage();
        List<BlueprintsListVO> list = blueprintsService.selectBlueprintsList(queryReq);
        return getDataTable(PageUtils.newPage(list, MallBlueprintsConvert.INSTANCE.convert(list)));
    }


    /**
     * 获取图纸详细信息
     */
    @GetMapping(value = "/{id}")
    @Log(title = "查询图纸详细信息", businessType = BusinessType.OTHER, operatorType = OperatorType.WEB, isSaveResponseData = false)
    public AjaxResult<MallBlueprintsInfoVO> getInfo(@PathVariable("id") Long id)
    {
        // todo 查询图纸详情可以走缓存，增加查询效率
        // 查询图纸信息
        BlueprintsInfoVO blueprintsInfoVO = blueprintsService.selectBlueprintsById(id);
        MallBlueprintsInfoVO mallBlueprintsInfoVO = MallBlueprintsConvert.INSTANCE.convert(blueprintsInfoVO);
        mallBlueprintsInfoVO.setPreviewImageUrlList(StrUtil.split(blueprintsInfoVO.getPreviewImageUrl(), ','));
        mallBlueprintsInfoVO.setIntroductionImageUrlList(StrUtil.split(blueprintsInfoVO.getIntroductionImageUrl(), ','));

        // todo 查询用户是否已购买图纸，也可以增加缓存（这里可以用redis的bitmap，记录用户已购买的图纸，或者图纸被哪些人购买了）
        // 查询用户是否已购买图纸
        BlueprintsUser blueprintsUser = blueprintsUserService.getOne(new LambdaQueryWrapper<BlueprintsUser>()
                .eq(BlueprintsUser::getBlueprintsId, id).eq(BlueprintsUser::getUserId, SecurityUtils.getUserId(false)));
        if (blueprintsUser == null) {
            mallBlueprintsInfoVO.setFileUrl(null);
            mallBlueprintsInfoVO.setIsBuy(false);
        } else {
            mallBlueprintsInfoVO.setIsBuy(true);
        }

        // todo 此处应该异步处理，增加访问效率
        if (SecurityUtils.getLoginUser(false) != null) {
            // 记录用户图纸访问记录
            blueprintsViewService.recordPv(id);
        }

        return success(mallBlueprintsInfoVO);
    }

    /**
     * 购买图纸前的处理
     * 计算实付金额
     */
    @GetMapping(value = "/buyPre/{id}")
    @Log(title = "购买图纸前的处理", businessType = BusinessType.OTHER, operatorType = OperatorType.WEB, isSaveResponseData = false)
    public AjaxResult<MallBuyPreVO> buyPre(@PathVariable("id") Long id)
    {
        return success(blueprintsService.buyPre(id));
    }

    /**
     * 购买图纸
     */
    @PostMapping(value = "/buy/{id}")
    @RepeatSubmit
    @Log(title = "购买图纸", businessType = BusinessType.OTHER, operatorType = OperatorType.WEB, isSaveResponseData = false)
    public AjaxResult buy(@PathVariable("id") Long id)
    {
        blueprintsService.buy(id);
        return success();
    }


    /**
     * 获取图纸分类树
     */
    @GetMapping("/categoryTree")
    public AjaxResult<List<BlueprintsCategoryVO>> getBlueprintsCategoryTree()
    {
        return success(blueprintsService.getBlueprintsCategoryTree());
    }

    /**
     * 收藏图纸
     */
    @PostMapping("/collect/{id}")
    @Log(title = "收藏图纸", businessType = BusinessType.OTHER, operatorType = OperatorType.WEB, isSaveResponseData = false)
    public AjaxResult collect(@PathVariable("id") Long id) {
        blueprintsService.collect(id);
        return success();
    }



    /**
     * 取消收藏图纸
     */
    @PostMapping("/uncollect/{id}")
    @Log(title = "取消收藏图纸", businessType = BusinessType.OTHER, operatorType = OperatorType.WEB, isSaveResponseData = false)
    public AjaxResult uncollect(@PathVariable("id") Long id) {
        blueprintsService.uncollect(id);
        return success();
    }
}
