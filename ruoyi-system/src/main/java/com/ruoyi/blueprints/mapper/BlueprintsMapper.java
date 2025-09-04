package com.ruoyi.blueprints.mapper;

import java.util.List;
import com.ruoyi.blueprints.domain.Blueprints;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.blueprints.domain.req.QueryBlueprintsListReq;
import com.ruoyi.blueprints.domain.vo.BlueprintsInfoVO;
import com.ruoyi.blueprints.domain.vo.BlueprintsListVO;
import org.apache.ibatis.annotations.Param;

/**
 * 图纸列表Mapper接口
 * 
 * @author tds
 * @date 2025-03-03
 */
public interface BlueprintsMapper extends BaseMapper<Blueprints>
{


    /**
     * 查询图纸列表列表
     * 
     * @param queryReq 图纸列表
     * @return 图纸列表集合
     */
    List<BlueprintsListVO> selectBlueprintsList(QueryBlueprintsListReq queryReq);

    /**
     * 查询图纸信息
     * @param id
     * @return
     */
    BlueprintsInfoVO selectBlueprintsById(Long id);

    /**
     * 更新图纸信息
     * @param editDo
     * @return
     */
    int updateBlueprintsById(Blueprints editDo);

    /**
     * 更新图纸浏览量
     * @param id 图纸id
     * @return
     */
    int updateViewNum(@Param("id") Long id);

    /**
     * 更新图纸收藏人数
     * @param id 图纸id
     * @param num 收藏人数递增数
     * @return
     */
    int updateCollectNum(@Param("id") Long id, @Param("num") Integer num);

    /**
     * 更新图纸销售数量
     * @param id 图纸id
     * @param num 销售量递增数
     * @return
     */
    int updateSaleNum(@Param("id") Long id, @Param("num") Integer num);
}
