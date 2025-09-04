package com.ruoyi.common.utils;

import cn.hutool.core.util.IdcardUtil;
import com.ruoyi.common.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;

/**
 *
 *
 * @author tongdashuai
 * @version 1.0
 * @description
 * @module
 * @date 2024/8/20 15:56
 */
@Slf4j
public class IdCardUtils {

    /**
     * 根据身份编号获取年龄，只支持15或18位身份证号码
     *
     * @param idcard 身份编号
     * @return 年龄
     */
    public static int getAgeByIdCard(String idcard) {
        try {
            return IdcardUtil.getAgeByIdCard(idcard);
        } catch (Exception e) {
            log.error("身份证解析生日错误，错误信息：", e);
            throw new ServiceException("身份证格式有误，无法解析出出生日期");
        }
    }
}
