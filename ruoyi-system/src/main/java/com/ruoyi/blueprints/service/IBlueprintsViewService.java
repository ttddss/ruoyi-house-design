package com.ruoyi.blueprints.service;

import java.util.List;
import com.ruoyi.blueprints.domain.BlueprintsView;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 图纸浏览记录Service接口
 * 
 * @author tds
 * @date 2025-03-09
 */
public interface IBlueprintsViewService extends IService<BlueprintsView>
{


    /**
     * 记录图纸访问记录
     *
     * @param id 图纸id
     */
    void recordPv(Long id);
}
