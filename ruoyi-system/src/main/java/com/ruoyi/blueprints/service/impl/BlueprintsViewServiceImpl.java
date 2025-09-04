package com.ruoyi.blueprints.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.blueprints.domain.Blueprints;
import com.ruoyi.blueprints.domain.BlueprintsView;
import com.ruoyi.blueprints.mapper.BlueprintsMapper;
import com.ruoyi.blueprints.mapper.BlueprintsViewMapper;
import com.ruoyi.blueprints.service.IBlueprintsViewService;
import com.ruoyi.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 图纸浏览记录Service业务层处理
 * 
 * @author tds
 * @date 2025-03-09
 */
@Service
public class BlueprintsViewServiceImpl extends ServiceImpl<BlueprintsViewMapper, BlueprintsView> implements IBlueprintsViewService
{

    @Autowired
    private BlueprintsMapper blueprintsMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void recordPv(Long id) {
        // 查询图纸是否有被当前登录用户访问过
        boolean isView = baseMapper.exists(new LambdaQueryWrapper<BlueprintsView>()
                .eq(BlueprintsView::getBlueprintsId, id)
                .eq(BlueprintsView::getUserId, SecurityUtils.getUserId()));
        if (!isView) {
            // 更新图纸访问量
            Blueprints updateDo = new Blueprints();
            updateDo.setId(id);
            blueprintsMapper.updateViewNum(id);
        }

        // 记录图纸访问记录
        BlueprintsView blueprintsView = new BlueprintsView();
        blueprintsView.setBlueprintsId(id);
        blueprintsView.setUserId(SecurityUtils.getUserId());
        blueprintsView.setCreateBy(SecurityUtils.getUsername());
        this.save(blueprintsView);
    }
}
