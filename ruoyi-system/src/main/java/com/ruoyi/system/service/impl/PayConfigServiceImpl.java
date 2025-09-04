package com.ruoyi.system.service.impl;

import java.util.List;
import java.util.Arrays;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.PayConfigMapper;
import com.ruoyi.system.domain.PayConfig;
import com.ruoyi.system.service.IPayConfigService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.system.convert.PayConfigConvert;
import org.springframework.transaction.annotation.Transactional;

import com.ruoyi.system.domain.req.AddPayConfigReq;
import com.ruoyi.system.domain.req.EditPayConfigReq;
import com.ruoyi.system.domain.req.QueryPayConfigListReq;
import com.ruoyi.system.domain.vo.PayConfigInfoVO;
import com.ruoyi.system.domain.vo.PayConfigListVO;
import com.ruoyi.common.utils.AssertUtils;
import com.ruoyi.common.utils.PageUtils;

/**
 * 支付配置Service业务层处理
 * 
 * @author tds
 * @date 2025-03-21
 */
@Service
public class PayConfigServiceImpl extends ServiceImpl<PayConfigMapper, PayConfig> implements IPayConfigService
{

    @Autowired
    private RedisCache redisCache;

    /**
     * 查询支付配置
     * 
     * @param id 支付配置主键
     * @return 支付配置
     */
    @Override
    public PayConfigInfoVO selectPayConfigById(Long id)
    {
        PayConfigInfoVO infoVO = PayConfigConvert.INSTANCE.convert(this.getById(id));
        return infoVO;
    }

    /**
     * 查询支付配置列表
     * 
     * @param queryReq 支付配置
     * @return 支付配置
     */
    @Override
    public List<PayConfigListVO> selectPayConfigList(QueryPayConfigListReq queryReq)
    {
        List<PayConfig> payConfigs = getBaseMapper().selectPayConfigList(queryReq);
        List<PayConfigListVO> voList = PayConfigConvert.INSTANCE.convert(payConfigs);
        return PageUtils.newPage(payConfigs, voList);
    }

    /**
     * 新增支付配置
     * 
     * @param addReq 支付配置
     * @return 结果
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean insertPayConfig(AddPayConfigReq addReq)
    {
        PayConfig addDo = PayConfigConvert.INSTANCE.convert(addReq);
        addDo.setCreateBy(SecurityUtils.getUsername());

        // 记入缓存
        String key = CacheConstants.PAY_CONFIG_KEY + Constants.COLON_DELIMETER + addReq.getChannel()
                + Constants.COLON_DELIMETER + addReq.getSubChannel();
        redisCache.setCacheObject(key, addDo);

        return this.save(addDo);
    }

    /**
     * 修改支付配置
     * 
     * @param editReq 支付配置
     * @return 结果
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updatePayConfig(EditPayConfigReq editReq)
    {
        PayConfig editDo = PayConfigConvert.INSTANCE.convert(editReq);
        editDo.setUpdateBy(SecurityUtils.getUsername());

        PayConfig payConfig = this.getById(editDo.getId());
        AssertUtils.notNull(payConfig, "支付配置为空");
        editDo.setVersion(payConfig.getVersion() + 1);

        // 记入缓存
        String key = CacheConstants.PAY_CONFIG_KEY + Constants.COLON_DELIMETER + payConfig.getChannel()
                + Constants.COLON_DELIMETER + payConfig.getSubChannel();
        redisCache.setCacheObject(key, editDo);
        return this.updateById(editDo);
    }

    /**
     * 批量删除支付配置
     * 
     * @param ids 需要删除的支付配置主键
     * @return 结果
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deletePayConfigByIds(Long[] ids)
    {
        AssertUtils.isTrue(ArrayUtil.isNotEmpty(ids), "参数错误，ids不能为空");
        List<PayConfig> payConfigs = baseMapper.selectBatchIds(CollUtil.newArrayList(ids));
        if (CollUtil.isEmpty(payConfigs)) {
            return true;
        }

        // 清除缓存配置
        for (PayConfig payConfig : payConfigs) {
            String key = CacheConstants.PAY_CONFIG_KEY + Constants.COLON_DELIMETER + payConfig.getChannel()
                    + Constants.COLON_DELIMETER + payConfig.getSubChannel();
            redisCache.deleteObject(key);
        }

        return this.removeByIds(Arrays.asList(ids));
    }

    @Override
    public PayConfig selectPayConfigInfo(int channel, int subChannel) {
        String key = CacheConstants.PAY_CONFIG_KEY + Constants.COLON_DELIMETER + channel
                + Constants.COLON_DELIMETER + subChannel;
        PayConfig payConfig = redisCache.getCacheObject(key);
        if (payConfig == null) {
            List<PayConfig> payConfigs = baseMapper.selectList(new LambdaQueryWrapper<PayConfig>()
                    .eq(PayConfig::getChannel, channel)
                    .eq(PayConfig::getSubChannel, subChannel).orderByDesc(PayConfig::getId));
            payConfig = CollUtil.isEmpty(payConfigs) ? null : payConfigs.get(0);
        }
        return payConfig;
    }




}
