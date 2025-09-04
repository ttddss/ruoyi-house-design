package com.ruoyi.system.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Arrays;
import cn.hutool.core.util.ArrayUtil;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.system.enums.FeedbackStatusEnum;
import com.ruoyi.system.mapper.SysUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.FeedbackMapper;
import com.ruoyi.system.domain.Feedback;
import com.ruoyi.system.service.IFeedbackService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.system.convert.FeedbackConvert;
import org.springframework.transaction.annotation.Transactional;

import com.ruoyi.system.domain.req.AddFeedbackReq;
import com.ruoyi.system.domain.req.ReplyFeedbackReq;
import com.ruoyi.system.domain.req.QueryFeedbackListReq;
import com.ruoyi.system.domain.vo.FeedbackInfoVO;
import com.ruoyi.system.domain.vo.FeedbackListVO;
import com.ruoyi.common.utils.AssertUtils;

/**
 * 反馈Service业务层处理
 * 
 * @author tds
 * @date 2025-03-12
 */
@Service
public class FeedbackServiceImpl extends ServiceImpl<FeedbackMapper, Feedback> implements IFeedbackService
{

    @Autowired
    private SysUserMapper sysUserMapper;

    /**
     * 查询反馈
     * 
     * @param id 反馈主键
     * @return 反馈
     */
    @Override
    public FeedbackInfoVO selectFeedbackById(Long id)
    {
        Feedback feedbackDb = this.getById(id);
        AssertUtils.notNull(feedbackDb, "反馈信息不存在");
        FeedbackInfoVO infoVO = FeedbackConvert.INSTANCE.convert(feedbackDb);
        // 查询管理员信息
        SysUser adminUser = sysUserMapper.selectUserById(feedbackDb.getResponseUserId());
        if (adminUser != null) {
            infoVO.setResponseUsername(adminUser.getUserName());
        }
        return infoVO;
    }

    /**
     * 查询反馈列表
     * 
     * @param queryReq 反馈
     * @return 反馈
     */
    @Override
    public List<FeedbackListVO> selectFeedbackList(QueryFeedbackListReq queryReq)
    {
        return getBaseMapper().selectFeedbackList(queryReq);
    }

    /**
     * 新增反馈
     * 
     * @param addReq 反馈
     * @return 结果
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean insertFeedback(AddFeedbackReq addReq)
    {
        Feedback addDo = FeedbackConvert.INSTANCE.convert(addReq);
        LoginUser loginUser = SecurityUtils.getLoginUser(false);
        addDo.setUserId(loginUser == null ? null : loginUser.getUserId());
        addDo.setCreateBy(loginUser == null ? null : loginUser.getUsername());
        return this.save(addDo);
    }

    /**
     * 反馈回复
     * 
     * @param replyReq 反馈
     * @return 结果
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean replyFeedback(ReplyFeedbackReq replyReq)
    {
        Feedback feedbackDb = baseMapper.selectById(replyReq.getId());
        AssertUtils.notNull(feedbackDb, "反馈信息不存在");
        AssertUtils.isTrue(feedbackDb.getStatus() != FeedbackStatusEnum.CLOSED.getCode(), "该反馈已关闭");

        Feedback editDo = FeedbackConvert.INSTANCE.convert(replyReq);
        editDo.setUpdateBy(SecurityUtils.getUsername());
        editDo.setResponseUserId(SecurityUtils.getUserId());
        editDo.setResponseTime(new Date());
        editDo.setStatus(FeedbackStatusEnum.RESOLVED.getCode());
        return this.updateById(editDo);
    }



    @Transactional(rollbackFor = Exception.class)
    @Override
    public void closeFeedback(Long[] ids) {
        for (Long id : ids) {
            Feedback editDo = new Feedback();
            editDo.setId(id);
            editDo.setUpdateBy(SecurityUtils.getUsername());
            editDo.setStatus(FeedbackStatusEnum.CLOSED.getCode());
            this.updateById(editDo);
        }
    }


}
