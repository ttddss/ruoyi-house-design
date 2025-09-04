package com.ruoyi.member.service;

import java.util.List;
import com.ruoyi.member.domain.Vip;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.member.domain.req.AddVipReq;
import com.ruoyi.member.domain.req.EditVipReq;
import com.ruoyi.member.domain.req.QueryVipListReq;
import com.ruoyi.member.domain.req.UpdateVipStatusReq;
import com.ruoyi.member.domain.vo.MallVipListVO;
import com.ruoyi.member.domain.vo.VipInfoVO;
import com.ruoyi.member.domain.vo.VipListVO;

/**
 * vipService接口
 * 
 * @author tds
 * @date 2025-03-13
 */
public interface IVipService extends IService<Vip>
{
    /**
     * 查询vip
     * 
     * @param id vip主键
     * @return vip
     */
    VipInfoVO selectVipById(Long id);

    /**
     * 查询vip列表
     * 
     * @param queryReq vip
     * @return vip集合
     */
    List<VipListVO> selectVipList(QueryVipListReq queryReq);

    /**
     * 新增vip
     * 
     * @param addReq vip
     * @return 结果
     */
    boolean insertVip(AddVipReq addReq);

    /**
     * 修改vip
     * 
     * @param editReq vip
     * @return 结果
     */
    boolean updateVip(EditVipReq editReq);

    /**
     * 批量删除vip
     * 
     * @param ids 需要删除的vip主键集合
     * @return 结果
     */
    boolean deleteVipByIds(Long[] ids);

    /**
     * 更新vip状态
     * @param updateVipStatusReq
     * @return
     */
    int updateStatus(UpdateVipStatusReq updateVipStatusReq);

    /**
     * 查询vip信息集合
     * @return
     */
    List<MallVipListVO> selectMallVipList();

    /**
     * 查询用户vip信息
     *
     * @param userId
     * @return
     */
    Vip getVipByUserId(Long userId);
}
