package com.ruoyi.system.domain.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 评价统计vo对象
 * 
 * @author tds
 * @date 2025-04-02
 */
@Data
public class EvaluateScoreStaticsDTO implements Serializable
{
    private static final long serialVersionUID = 1L;


    /** 得分 */
    private Long score;

    /** 评伦数 */
    private Long num;

}
