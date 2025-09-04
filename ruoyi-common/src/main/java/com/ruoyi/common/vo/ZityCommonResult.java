package com.ruoyi.common.vo;

import lombok.Data;

/**
 * @author xzh
 * @since 2024/2/29 15:21
 */
@Data
public class ZityCommonResult {
    /**
     * 系统状态
     */
    private String code;
    /**
     * 系统返回信息
     */
    private String info;
    /**
     * 返回数据
     */
    private Object data;


}
