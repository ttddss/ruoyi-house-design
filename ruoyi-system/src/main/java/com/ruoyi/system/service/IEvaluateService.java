package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.Evaluate;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.system.domain.req.AddEvaluateReq;
import com.ruoyi.system.domain.req.EditEvaluateReq;
import com.ruoyi.system.domain.req.QueryEvaluateListReq;
import com.ruoyi.system.domain.vo.EvaluateInfoVO;
import com.ruoyi.system.domain.vo.EvaluateListVO;
import com.ruoyi.system.domain.vo.MallEvaluateStaticsVO;

/**
 * 评价Service接口
 * 
 * @author tds
 * @date 2025-04-02
 */
public interface IEvaluateService extends IService<Evaluate>
{
    /**
     * 查询评价
     * 
     * @param id 评价主键
     * @return 评价
     */
    EvaluateInfoVO selectEvaluateById(Long id);

    /**
     * 查询评价列表
     * 
     * @param queryReq 评价
     * @return 评价集合
     */
    List<EvaluateListVO> selectEvaluateList(QueryEvaluateListReq queryReq);

    /**
     * 新增评价
     * 
     * @param addReq 评价
     * @return 结果
     */
    boolean insertEvaluate(AddEvaluateReq addReq);

    /**
     * 修改评价
     * 
     * @param editReq 评价
     * @return 结果
     */
    boolean updateEvaluate(EditEvaluateReq editReq);

    /**
     * 批量删除评价
     * 
     * @param ids 需要删除的评价主键集合
     * @return 结果
     */
    boolean deleteEvaluateByIds(Long[] ids);

    /**
     * 查询图纸评论统计
     * @param blueprintsId
     * @return
     */
    MallEvaluateStaticsVO evaluateStatics(Long blueprintsId);
}
