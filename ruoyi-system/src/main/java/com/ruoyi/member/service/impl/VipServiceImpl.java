package com.ruoyi.member.service.impl;

import java.util.*;
import java.util.stream.Collectors;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.member.domain.VipUser;
import com.ruoyi.member.domain.req.UpdateVipStatusReq;
import com.ruoyi.member.domain.vo.MallVipListVO;
import com.ruoyi.member.enums.VipStatusEnum;
import com.ruoyi.member.mapper.VipUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.member.mapper.VipMapper;
import com.ruoyi.member.domain.Vip;
import com.ruoyi.member.service.IVipService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.member.convert.VipConvert;
import org.springframework.transaction.annotation.Transactional;

import com.ruoyi.member.domain.req.AddVipReq;
import com.ruoyi.member.domain.req.EditVipReq;
import com.ruoyi.member.domain.req.QueryVipListReq;
import com.ruoyi.member.domain.vo.VipInfoVO;
import com.ruoyi.member.domain.vo.VipListVO;
import com.ruoyi.common.utils.AssertUtils;
import com.ruoyi.common.utils.PageUtils;

/**
 * vipService业务层处理
 * 
 * @author tds
 * @date 2025-03-13
 */
@Service
public class VipServiceImpl extends ServiceImpl<VipMapper, Vip> implements IVipService
{

    @Autowired
    private VipUserMapper vipUserMapper;

    /**
     * 查询vip
     * 
     * @param id vip主键
     * @return vip
     */
    @Override
    public VipInfoVO selectVipById(Long id)
    {
        Vip vip = this.getById(id);
        AssertUtils.notNull(vip, "vip信息不存在");
        VipInfoVO infoVO = VipConvert.INSTANCE.convert(vip);
        // todo 其他业务逻辑
        return infoVO;
    }

    /**
     * 查询vip列表
     * 
     * @param queryReq vip
     * @return vip
     */
    @Override
    public List<VipListVO> selectVipList(QueryVipListReq queryReq)
    {
        List<Vip> vips = getBaseMapper().selectVipList(queryReq);
        List<VipListVO> voList = VipConvert.INSTANCE.convert(vips);
        return PageUtils.newPage(vips, voList);
    }

    /**
     * 新增vip
     * 
     * @param addReq vip
     * @return 结果
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean insertVip(AddVipReq addReq)
    {
        Vip addDo = VipConvert.INSTANCE.convert(addReq);
        List<Vip> vips = baseMapper.selectList(new LambdaQueryWrapper<Vip>().eq(Vip::getName, addReq.getName()));
        AssertUtils.isTrue(CollUtil.isEmpty(vips), "vip名称已存在");
        addDo.setCreateBy(SecurityUtils.getUsername());
        return this.save(addDo);
    }

    /**
     * 修改vip
     * 
     * @param editReq vip
     * @return 结果
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateVip(EditVipReq editReq)
    {
        Vip vipDb = baseMapper.selectById(editReq.getId());
        AssertUtils.notNull(vipDb, "vip信息不存在");
        Vip editDo = VipConvert.INSTANCE.convert(editReq);

        List<Vip> vips = baseMapper.selectList(new LambdaQueryWrapper<Vip>()
                .eq(Vip::getName, editReq.getName())
                .ne(Vip::getName, vipDb.getName()));
        AssertUtils.isTrue(CollUtil.isEmpty(vips), "vip名称已存在");

        editDo.setUpdateBy(SecurityUtils.getUsername());
        return this.updateById(editDo);
    }

    /**
     * 批量删除vip
     * 
     * @param ids 需要删除的vip主键
     * @return 结果
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteVipByIds(Long[] ids)
    {
        AssertUtils.isTrue(ArrayUtil.isNotEmpty(ids), "参数错误，ids不能为空");

        List<VipUser> vipUsers = vipUserMapper.selectList(new LambdaQueryWrapper<VipUser>()
                .in(VipUser::getVipId, ids));
        AssertUtils.isTrue(CollUtil.isEmpty(vipUsers), "vip已被使用，无法删除");

        return this.removeByIds(Arrays.asList(ids));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int updateStatus(UpdateVipStatusReq updateVipStatusReq) {
        Vip vipDo = new Vip();
        vipDo.setId(updateVipStatusReq.getId());
        vipDo.setStatus(updateVipStatusReq.getStatus());
        vipDo.setUpdateBy(SecurityUtils.getUsername());
        return baseMapper.updateById(vipDo);
    }

    @Override
    public List<MallVipListVO> selectMallVipList() {
        // 查询所有vip信息
        List<Vip> vips = baseMapper.selectList(new LambdaQueryWrapper<Vip>()
                .eq(Vip::getStatus, VipStatusEnum.ENABLE.getCode())
                .orderByAsc(Vip::getOrderNum));

        // 查询用户已经拥有的使用中的vip
        List<VipUser> vipUsers = vipUserMapper.selectList(new LambdaQueryWrapper<VipUser>()
                .eq(VipUser::getUserId, SecurityUtils.getUserId())
                .gt(VipUser::getExpireTime, new Date()));
        Map<Long, VipUser> vipUserMap = vipUsers.stream().collect(Collectors.toMap(VipUser::getVipId, vipUser -> vipUser));

        // 组装返回参数
        List<MallVipListVO> mallVipList = new ArrayList<>();
        for (Vip vip : vips) {
            MallVipListVO vipVo = VipConvert.INSTANCE.convertMallVo(vip);
            VipUser vipUser = vipUserMap.get(vip.getId());
            if (vipUser != null) {
                vipVo.setExpireTime(vipUser.getExpireTime());
                vipVo.setUserId(vipUser.getUserId());
            }

            mallVipList.add(vipVo);
        }
        return mallVipList;
    }

    @Override
    public Vip getVipByUserId(Long userId) {
        // 查询用户使用中的折扣力度最高的vip信息
        return baseMapper.selectUserVip(userId);
    }


}
