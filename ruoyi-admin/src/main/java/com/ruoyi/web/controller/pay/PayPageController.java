package com.ruoyi.web.controller.pay;

import cn.hutool.core.net.URLEncodeUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.constant.PageConstants;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.order.domain.PayOrder;
import com.ruoyi.pay.domain.dto.PayQrLinkInfoDTO;
import com.ruoyi.pay.domain.dto.QrPayInfoDTO;
import com.ruoyi.pay.domain.req.AlipayTradePreCreateReq;
import com.ruoyi.pay.domain.req.PayQrLinkReq;
import com.ruoyi.pay.domain.res.AlipayTradePreCreateRes;
import com.ruoyi.pay.service.IAlipayService;
import com.ruoyi.pay.service.IWxpayService;
import com.ruoyi.system.domain.PayConfig;
import com.ruoyi.system.enums.PayChannelEnum;
import com.ruoyi.system.enums.PaySubChannelEnum;
import com.ruoyi.system.service.IPayConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;

/**
 *
 *
 * @author tongdashuai
 * @version 1.0
 * @description
 * @module
 * @date 2025/3/27 17:37
 */
@Slf4j
@Controller
@RequestMapping("/page/pay")
public class PayPageController {

    /**
     * 项目的根路径字段名称
     */

    @Value("${ruoyi.homeUrl}")
    private String homeUrl;

    @Autowired
    private IAlipayService alipayService;


    @Autowired
    private IPayConfigService payConfigService;

    @Autowired
    private RedisCache redisCache;

    private static final String PAY_ERROR_PAGE = "common/payError.html";



    /**
     * 统一支付入口页
     */
    @GetMapping("/payPre")
    public String payPre(@RequestHeader("user-agent") String userAgent,
                         @RequestParam(value = "qrLinkId", required = false) String qrLinkId,
                         Model model) {
        model.addAttribute(PageConstants.HOME_URL, homeUrl);
        // 判断客户端类型是否是微信或支付宝
        if (!userAgent.contains(PageConstants.WX_BROWSER_MARK) && !userAgent.contains(PageConstants.ALIPAY_BROWSER_MARK)) {
            model.addAttribute(PageConstants.MSG, "请使用支付宝或微信客户端");
            return PAY_ERROR_PAGE;
        }

        String key = CacheConstants.QR_PAY_INFO_KEY_PREFIX + qrLinkId;
        // 获取支付信息
        PayQrLinkInfoDTO payInfo = redisCache.getCacheObject(key);
        if (payInfo == null) {
            model.addAttribute(PageConstants.MSG, "支付信息已失效");
            return PAY_ERROR_PAGE;
        }
        model.addAttribute(PageConstants.PAYINFO, payInfo);
        model.addAttribute(PageConstants.QR_LINK_ID, qrLinkId);

        return "common/payPre.html";
    }

    /**
     * 统一支付。返回支付跳转链接
     */
    @GetMapping("/pay")
    public String payLink(@RequestHeader("user-agent") String userAgent,
                          @RequestParam(value = "qrLinkId", required = false) String qrLinkId,
                          Model model) {
        model.addAttribute(PageConstants.HOME_URL, homeUrl);

        // 获取支付信息
        PayQrLinkInfoDTO payInfo = redisCache.getCacheObject(CacheConstants.QR_PAY_INFO_KEY_PREFIX + qrLinkId);
        if (payInfo == null) {
            model.addAttribute(PageConstants.MSG, "支付信息已失效");
            return PAY_ERROR_PAGE;
        }

        // 微信客户端
        if (userAgent.contains(PageConstants.WX_BROWSER_MARK)) {
            // 查询对应的jsapi配置
            PayConfig payConfig = payConfigService.selectPayConfigInfo(PayChannelEnum.WX.getCode(), PaySubChannelEnum.WX_JSAPI.getCode());
            // 微信使用jsapi支付。这里重定向到授权页面
            String appid = payConfig.getAppid();
            String encodeUrl = URLEncodeUtil.encodeAll(StrUtil.format("{}/page/wxpay/jsapiRedirect?"
                    + "&amount={}&qrLinkId={}&appid={}", homeUrl, null, qrLinkId, appid));
            String redirectUrl = StrUtil.format("https://open.weixin.qq.com/connect/oauth2/authorize?appid={}&redirect_uri={}" +
                    "&response_type=code&scope=snsapi_base&state=#wechat_redirect", appid, encodeUrl);
            return "redirect:" + redirectUrl;
        }

        // 支付宝客户端
        if (userAgent.contains(PageConstants.ALIPAY_BROWSER_MARK)) {
            try {
                // 支付宝使用扫码付
                AlipayTradePreCreateReq request = new AlipayTradePreCreateReq(payInfo);
                AlipayTradePreCreateRes response = alipayService.tradePreCreate(request);

                // 记录订单号到二维码链接缓存，用于后续页面查询订单状态
                payInfo.setOrderNo(response.getOrderNo());
                redisCache.setCacheObject(CacheConstants.QR_PAY_INFO_KEY_PREFIX + qrLinkId, payInfo, 5, TimeUnit.MINUTES);
                return "redirect:" + response.getQrCode();
            } catch (Exception e) {
                log.error("调用支付宝扫码付失败，错误信息：", e);
                model.addAttribute(PageConstants.MSG, "调用支付宝扫码付失败");
                return PAY_ERROR_PAGE;
            }
        }

        model.addAttribute(PageConstants.MSG, "请使用支付宝或微信客户端");
        return PAY_ERROR_PAGE;
    }
}
