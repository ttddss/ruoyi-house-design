package com.ruoyi.system.domain.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 评价统计vo对象
 * 
 * @author tds
 * @date 2025-04-02
 */
@Data
public class MallEvaluateStaticsVO implements Serializable
{
    private static final long serialVersionUID = 1L;


    /** 总评数 */
    private Long totalNum;

    /** 好评数 */
    private Long goodNum;

    /** 中评数 */
    private Long normalNum;

    /** 差评数 */
    private Long badNum;

}
