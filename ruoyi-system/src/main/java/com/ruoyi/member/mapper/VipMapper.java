package com.ruoyi.member.mapper;

import java.util.List;
import com.ruoyi.member.domain.Vip;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.member.domain.req.QueryVipListReq;

/**
 * vipMapper接口
 * 
 * @author tds
 * @date 2025-03-13
 */
public interface VipMapper extends BaseMapper<Vip>
{


    /**
     * 查询vip列表
     * 
     * @param queryReq vip
     * @return vip集合
     */
    List<Vip> selectVipList(QueryVipListReq queryReq);

    /**
     * 查询用户使用中的折扣力度最高的vip信息
     *
     * @param userId
     * @return
     */
    Vip selectUserVip(Long userId);
}
