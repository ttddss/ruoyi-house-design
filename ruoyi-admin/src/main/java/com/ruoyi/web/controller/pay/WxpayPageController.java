package com.ruoyi.web.controller.pay;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruoyi.common.constant.PageConstants;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.order.domain.PayOrder;
import com.ruoyi.order.mapper.PayOrderMapper;
import com.ruoyi.pay.domain.dto.QrPayInfoDTO;
import com.ruoyi.pay.domain.req.PayQrLinkReq;
import com.ruoyi.pay.domain.req.WxpayJsapiPayReq;
import com.ruoyi.pay.domain.req.WxpayQueryAuthReq;
import com.ruoyi.pay.domain.res.WxpayJsapiPayRes;
import com.ruoyi.pay.domain.vo.WxpayQueryAuthVO;
import com.ruoyi.pay.service.IWxpayService;
import com.ruoyi.system.service.IPayConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;



/**
 *
 *
 * @author tongdashuai
 * @version 1.0
 * @description
 * @date 2025/3/27 17:56
 */
@Slf4j
@Controller
@RequestMapping("/page/wxpay")
public class WxpayPageController {

    @Value("${ruoyi.homeUrl}")
    private String homeUrl;

    @Autowired
    private IWxpayService wxpayService;

    @Autowired
    private PayOrderMapper payOrderMapper;

    @Autowired
    private IPayConfigService payConfigService;

    @Autowired
    private RedisCache redisCache;

    /**
     * 微信授权回调并调用jsapi支付，返回支付跳转页
     */
    @GetMapping("/jsapiRedirect")
    public String authAndPay(@RequestParam("code") String code,
                             @RequestParam(value = "state", required = false) String state,
                             @RequestParam(value = "appid", required = false) String appid,
                             @RequestParam("key") String key,
                             @RequestParam("amount") String amount,
                             Model model) {
        model.addAttribute(PageConstants.HOME_URL, homeUrl);
        model.addAttribute(PageConstants.APPID, appid);

        // 获取openid
        WxpayQueryAuthReq queryAuthFeignRequest = new WxpayQueryAuthReq();
        queryAuthFeignRequest.setCode(code);
        queryAuthFeignRequest.setWxAppid(appid);
        WxpayQueryAuthVO authInfo = wxpayService.getAuthInfo(queryAuthFeignRequest);

        // 获取支付信息
        PayQrLinkReq payInfo = null;
        if (StrUtil.isNotBlank(key)) {
            payInfo = redisCache.getCacheObject(key);
            if (payInfo == null) {
                model.addAttribute(PageConstants.MSG, "支付信息已失效");
                return "common/payError.html";
            }
        }

        try {
            // 发送jsapi支付
            WxpayJsapiPayReq jsapiFeignRequest = new WxpayJsapiPayReq();

            WxpayJsapiPayRes jsapiFeignResponse = wxpayService.jsapiPay(jsapiFeignRequest);
            model.addAttribute(PageConstants.PAY_INFO, jsapiFeignResponse);
            model.addAttribute(PageConstants.ORDER_NO, "");
        } catch (Exception e) {
            log.error("调用微信jsapi支付失败，错误信息：", e);
            model.addAttribute(PageConstants.MSG, "调用jsapi支付失败");
            return "common/payError.html";
        }

        return "wxpay/jsapi/pay.html";
    }

    /**
     * jsapi支付成功页-服务商商家小票页
     */
    @GetMapping("/jsapiSuccess")
    public String jsapiSuccess(Model model, @RequestParam("out_trade_no") String orderNo) {
        // check_code校验：https://wx.gtimg.com/pay/download/goldplan/goldplan_product_description_v2.pdf
        PayOrder order = payOrderMapper.selectOne(new LambdaQueryWrapper<PayOrder>().eq(PayOrder::getOrderNo, orderNo));
        model.addAttribute(PageConstants.HOME_URL, homeUrl);
        model.addAttribute(PageConstants.APPID, order.getAppid());
        model.addAttribute(PageConstants.PAY_ORDER, order);
        return "wxpay/jsapi/jsapiSuccess.html";
    }

    /**
     * jsapi支付成功页
     */
    @GetMapping("/jsapiSuccessFinal")
    public String jsapiSuccessFinal(Model model, @RequestParam("orderNo") String orderNo) {
        PayOrder order = payOrderMapper.selectOne(new LambdaQueryWrapper<PayOrder>().eq(PayOrder::getOrderNo, orderNo));
        model.addAttribute(PageConstants.HOME_URL, homeUrl);
        model.addAttribute(PageConstants.APPID, order.getAppid());
        model.addAttribute(PageConstants.PAY_ORDER, order);
        return "wxpay/jsapi/jsapiSuccessFinal.html";
    }


}
