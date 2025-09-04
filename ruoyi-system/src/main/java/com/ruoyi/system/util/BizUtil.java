package com.ruoyi.system.util;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.spring.SpringUtils;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * 常用业务工具类
 *
 * @author tongdashuai
 * @version 1.0
 * @description
 * @module
 * @date 2024/8/6 9:50
 */
public class BizUtil {


    /**
     * 生成支付账户编号
     * 生成规则：年月日（yyyyMMdd） + 2位递增数字。理论上1天最多99条是足够的了
     * @return
     */
    public static String generatePayAccountNo() {
        String timeStr = DateUtil.format(new Date(), "yyyyMMdd");
        Long num = SpringUtils.getBean(RedisCache.class)
                .incrementAndExpireFirst(CacheConstants.GEN_ACCOUNT_NO_KEY + timeStr, 1, TimeUnit.DAYS);
        if (num > 99) {
            throw new ServiceException("生成账户编号失败");
        }
        return timeStr + StrUtil.padPre(num.toString(), 2, '0');
    }


    /**
     * 生成订单号
     * 生成规则：1位随机数 + 年月日（yyyyMMddHH） + 4位递增数字 + 2位随机数
     *
     * @return
     */
    public static String generateOrderNo() {
        String timeStr = DateUtil.format(new Date(), "yyyyMMddHH");
        Long num = SpringUtils.getBean(RedisCache.class)
                .incrementAndExpireFirst(CacheConstants.ORDER_NO_KEY + timeStr, 1, TimeUnit.DAYS);
        return RandomUtil.randomNumbers(1) + timeStr + StrUtil.padPre(num.toString(), 4, '0') + RandomUtil.randomNumbers(3);
    }

}
