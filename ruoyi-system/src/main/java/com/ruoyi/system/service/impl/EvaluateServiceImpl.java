package com.ruoyi.system.service.impl;

import java.util.List;
import java.util.Arrays;
import cn.hutool.core.util.ArrayUtil;
import com.ruoyi.blueprints.domain.vo.BlueprintsInfoVO;
import com.ruoyi.blueprints.mapper.BlueprintsMapper;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.system.domain.dto.EvaluateScoreStaticsDTO;
import com.ruoyi.system.domain.vo.MallEvaluateStaticsVO;
import com.ruoyi.system.mapper.SysUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.EvaluateMapper;
import com.ruoyi.system.domain.Evaluate;
import com.ruoyi.system.service.IEvaluateService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.system.convert.EvaluateConvert;
import org.springframework.transaction.annotation.Transactional;

import com.ruoyi.system.domain.req.AddEvaluateReq;
import com.ruoyi.system.domain.req.EditEvaluateReq;
import com.ruoyi.system.domain.req.QueryEvaluateListReq;
import com.ruoyi.system.domain.vo.EvaluateInfoVO;
import com.ruoyi.system.domain.vo.EvaluateListVO;
import com.ruoyi.common.utils.AssertUtils;
import com.ruoyi.common.utils.PageUtils;

/**
 * 评价Service业务层处理
 * 
 * @author tds
 * @date 2025-04-02
 */
@Service
public class EvaluateServiceImpl extends ServiceImpl<EvaluateMapper, Evaluate> implements IEvaluateService
{

    @Autowired
    private BlueprintsMapper blueprintsMapper;

    @Autowired
    private SysUserMapper sysUserMapper;

    /**
     * 查询评价
     * 
     * @param id 评价主键
     * @return 评价
     */
    @Override
    public EvaluateInfoVO selectEvaluateById(Long id)
    {
        EvaluateInfoVO infoVO = EvaluateConvert.INSTANCE.convert(this.getById(id));
        if (infoVO == null) {
            return null;
        }

        // 查询图纸信息
        BlueprintsInfoVO blueprintsInfoVO = blueprintsMapper.selectBlueprintsById(infoVO.getBlueprintsId());
        if (blueprintsInfoVO != null) {
            infoVO.setBlueprintsName(blueprintsInfoVO.getName());
        }

        // 查询用户信息
        SysUser sysUser = sysUserMapper.selectUserById(infoVO.getUserId());
        if (sysUser != null) {
            infoVO.setNickName(sysUser.getNickName());
        }

        return infoVO;
    }

    /**
     * 查询评价列表
     * 
     * @param queryReq 评价
     * @return 评价
     */
    @Override
    public List<EvaluateListVO> selectEvaluateList(QueryEvaluateListReq queryReq)
    {
        return getBaseMapper().selectEvaluateList(queryReq);
    }

    /**
     * 新增评价
     * 
     * @param addReq 评价
     * @return 结果
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean insertEvaluate(AddEvaluateReq addReq)
    {
        Evaluate addDo = EvaluateConvert.INSTANCE.convert(addReq);
        addDo.setUserId(SecurityUtils.getUserId());
        return this.save(addDo);
    }

    /**
     * 修改评价
     * 
     * @param editReq 评价
     * @return 结果
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateEvaluate(EditEvaluateReq editReq)
    {
        Evaluate editDo = EvaluateConvert.INSTANCE.convert(editReq);
        // todo 其他业务逻辑
        return this.updateById(editDo);
    }

    /**
     * 批量删除评价
     * 
     * @param ids 需要删除的评价主键
     * @return 结果
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteEvaluateByIds(Long[] ids)
    {
        AssertUtils.isTrue(ArrayUtil.isNotEmpty(ids), "参数错误，ids不能为空");
        return this.removeByIds(Arrays.asList(ids));
    }

    @Override
    public MallEvaluateStaticsVO evaluateStatics(Long blueprintsId) {
        // 各个评分的评论数统计
        List<EvaluateScoreStaticsDTO> staticss = baseMapper.evaluateStatics(blueprintsId);
        // 总评论数
        Long totalNum = 0L;
        // 好评数
        Long goodNum = 0L;
        // 中评数
        Long normalNum = 0L;
        // 差评数
        Long badNum = 0L;

        for (EvaluateScoreStaticsDTO dto : staticss) {
            totalNum += dto.getNum();
            if (dto.getScore() < 3) {
                badNum += dto.getNum();
            }
            if (dto.getScore() == 3) {
                normalNum += dto.getNum();
            }
            if (dto.getScore() > 3) {
                goodNum += dto.getNum();
            }
        }
        MallEvaluateStaticsVO vo = new MallEvaluateStaticsVO();
        vo.setTotalNum(totalNum);
        vo.setGoodNum(goodNum);
        vo.setBadNum(badNum);
        vo.setNormalNum(normalNum);
        return vo;
    }


}
