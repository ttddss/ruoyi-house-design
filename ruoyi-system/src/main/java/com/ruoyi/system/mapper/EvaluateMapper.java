package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.Evaluate;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.system.domain.dto.EvaluateScoreStaticsDTO;
import com.ruoyi.system.domain.req.QueryEvaluateListReq;
import com.ruoyi.system.domain.vo.EvaluateListVO;
import com.ruoyi.system.domain.vo.MallEvaluateStaticsVO;

/**
 * 评价Mapper接口
 * 
 * @author tds
 * @date 2025-04-02
 */
public interface EvaluateMapper extends BaseMapper<Evaluate>
{


    /**
     * 查询评价列表
     * 
     * @param queryReq 评价
     * @return 评价集合
     */
    List<EvaluateListVO> selectEvaluateList(QueryEvaluateListReq queryReq);

    /**
     * 查询图纸评论统计
     *
     * @param blueprintsId 图纸id
     * @return
     */
    List<EvaluateScoreStaticsDTO> evaluateStatics(Long blueprintsId);
}
