package com.ruoyi.web.controller.pay;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.annotation.RepeatSubmit;
import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.LockUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.order.domain.PayOrder;
import com.ruoyi.order.enums.PayStatusEnum;
import com.ruoyi.order.mapper.PayOrderMapper;
import com.ruoyi.pay.domain.dto.PayQrLinkInfoDTO;
import com.ruoyi.pay.domain.dto.QrPayInfoDTO;
import com.ruoyi.pay.domain.req.AlipayRefundReq;
import com.ruoyi.pay.domain.req.PayQrLinkReq;
import com.ruoyi.pay.domain.req.RefundReq;
import com.ruoyi.pay.domain.vo.AlipayRefundVO;
import com.ruoyi.pay.domain.vo.PayQrLinkVO;
import com.ruoyi.pay.domain.vo.RefundVO;
import com.ruoyi.pay.enums.QrLinkStatusEnum;
import com.ruoyi.pay.service.PayManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;

/**
 *
 *
 * @author tongdashuai
 * @version 1.0
 * @description
 * @module
 * @date 2025/3/27 18:16
 */
@RestController
@RequestMapping("/pay")
public class PayController {

    @Value("${payConfig.qrPayPreUrl}")
    private String qrPayPreUrl;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private PayOrderMapper payOrderMapper;

    @Autowired
    private PayManager payManager;

    /**
     * 获取聚合支付支付二维码
     */
    @GetMapping("/getQrPayLink")
    @Log(title = "获取聚合支付支付二维码", businessType = BusinessType.OTHER)
    public AjaxResult<PayQrLinkVO> getQrPayLink(@Validated PayQrLinkReq request) {
        request.setUserId(SecurityUtils.getUserId());
        String qrLinkId = IdUtil.fastSimpleUUID();
        String key = CacheConstants.QR_PAY_INFO_KEY_PREFIX + qrLinkId;
        String qrLink = StrUtil.format("{}?qrLinkId={}", qrPayPreUrl, qrLinkId);

        PayQrLinkInfoDTO qrLinkInfo = new PayQrLinkInfoDTO(request);
        // 将支付信息存放到redis
        redisCache.setCacheObject(key, qrLinkInfo,  5, TimeUnit.MINUTES);

        PayQrLinkVO qrLinkVo = PayQrLinkVO.builder()
                .qrLinkId(qrLinkId)
                .qrLink(qrLink)
                .build();
        return AjaxResult.success(qrLinkVo);
    }

    /**
     * 获取聚合支付支付二维码订单状态
     */
    @GetMapping("/queryQrOrderStatus")
    @Log(title = "获取聚合支付支付二维码订单状态", businessType = BusinessType.OTHER)
    public AjaxResult queryQrOrderStatus(String qrLinkId) {
        String key = CacheConstants.QR_PAY_INFO_KEY_PREFIX + qrLinkId;
        PayQrLinkInfoDTO payQrLinkInfo = redisCache.getCacheObject(key);
        int status = QrLinkStatusEnum.PAYING.getCode();
        // 支付链接缓存失效
        if (payQrLinkInfo == null) {
            status = QrLinkStatusEnum.EXPIRED.getCode();
        } else if (StrUtil.isNotBlank(payQrLinkInfo.getOrderNo())) {
            // 二维码已生成对应订单，且订单状态为成功
            PayOrder payOrder = payOrderMapper.selectOne(new LambdaQueryWrapper<PayOrder>()
                    .eq(PayOrder::getOrderNo, payQrLinkInfo.getOrderNo()));
            if (payOrder != null && PayStatusEnum.SUCCESS.getCode() == payOrder.getStatus()) {
                status = QrLinkStatusEnum.SUCCESS.getCode();
            }
        }

        return AjaxResult.success(status);
    }

    /**
     * 通用退款
     * <p>退款可能立即到账，也可能延时到账，如果未立即到账，可以调用接口去查询（退款到银行卡时间由银行结算为准）。
     */
    @PostMapping("/refund")
    @RepeatSubmit
    @PreAuthorize("@ss.hasPermi('order:payOrder:refund')")
    @Log(title = "通用退款", businessType = BusinessType.OTHER)
    public AjaxResult<RefundVO> refund(@Validated @RequestBody RefundReq request) throws Exception {
        return AjaxResult.success(payManager.refund(request));
    }
}
