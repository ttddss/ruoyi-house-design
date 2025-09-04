package com.ruoyi.pay.domain.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 退款返回vo.
 *
 * @author tongdashuai
 * @version 1.0
 * @description
 * @module
 * @date 2025/3/24 14:35
 */
@Data
public class RefundVO implements Serializable {


    private static final long serialVersionUID = 718975053997804786L;


    /**
     * 退款状态：0-退款中 1-退款成功 2-退款失败 3-系统异常
     */
    private Integer status;



}
