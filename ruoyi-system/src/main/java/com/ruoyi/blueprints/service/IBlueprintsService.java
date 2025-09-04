package com.ruoyi.blueprints.service;

import java.math.BigDecimal;
import java.util.List;
import com.ruoyi.blueprints.domain.Blueprints;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.blueprints.domain.req.AddBlueprintsReq;
import com.ruoyi.blueprints.domain.req.ChangeBlueprintsListingStatusReq;
import com.ruoyi.blueprints.domain.req.EditBlueprintsReq;
import com.ruoyi.blueprints.domain.req.QueryBlueprintsListReq;
import com.ruoyi.blueprints.domain.vo.BlueprintsCategoryVO;
import com.ruoyi.blueprints.domain.vo.BlueprintsInfoVO;
import com.ruoyi.blueprints.domain.vo.BlueprintsListVO;
import com.ruoyi.blueprints.domain.vo.MallBuyPreVO;

/**
 * 图纸列表Service接口
 * 
 * @author tds
 * @date 2025-03-03
 */
public interface IBlueprintsService extends IService<Blueprints>
{
    /**
     * 查询图纸列表
     * 
     * @param id 图纸列表主键
     * @return 图纸列表
     */
    BlueprintsInfoVO selectBlueprintsById(Long id);

    /**
     * 查询图纸列表列表
     * 
     * @param queryReq 图纸列表
     * @return 图纸列表集合
     */
    List<BlueprintsListVO> selectBlueprintsList(QueryBlueprintsListReq queryReq);

    /**
     * 新增图纸列表
     * 
     * @param addReq 图纸列表
     * @return 结果
     */
    boolean insertBlueprints(AddBlueprintsReq addReq);

    /**
     * 修改图纸列表
     * 
     * @param editReq 图纸列表
     * @return 结果
     */
    boolean updateBlueprints(EditBlueprintsReq editReq);

    /**
     * 批量删除图纸列表
     * 
     * @param ids 需要删除的图纸列表主键集合
     * @return 结果
     */
    boolean deleteBlueprintsByIds(Long[] ids);

    /**
     * 获取图纸分类树
     *
     * @return
     */
    List<BlueprintsCategoryVO> getBlueprintsCategoryTree();

    /**
     * 改变图纸上架状态
     *
     * @param changeListingStatusReq
     * @return
     */
    void changeListingStatus(ChangeBlueprintsListingStatusReq changeListingStatusReq);


    /**
     * 收藏图纸
     * @param id 图纸id
     */
    void collect(Long id);

    /**
     * 取消收藏图纸
     * @param id
     */
    void uncollect(Long id);

    /**
     * 购买图纸前的处理
     * @param id
     */
    MallBuyPreVO buyPre(Long id);

    /**
     * 购买图纸
     * @param id
     */
    void buy(Long id);
}
