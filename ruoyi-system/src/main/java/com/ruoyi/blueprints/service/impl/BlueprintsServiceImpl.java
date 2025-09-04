package com.ruoyi.blueprints.service.impl;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.blueprints.convert.BlueprintsConvert;
import com.ruoyi.blueprints.domain.Blueprints;
import com.ruoyi.blueprints.domain.BlueprintsCollect;
import com.ruoyi.blueprints.domain.BlueprintsDetail;
import com.ruoyi.blueprints.domain.req.AddBlueprintsReq;
import com.ruoyi.blueprints.domain.req.ChangeBlueprintsListingStatusReq;
import com.ruoyi.blueprints.domain.req.EditBlueprintsReq;
import com.ruoyi.blueprints.domain.req.QueryBlueprintsListReq;
import com.ruoyi.blueprints.domain.vo.BlueprintsCategoryVO;
import com.ruoyi.blueprints.domain.vo.BlueprintsInfoVO;
import com.ruoyi.blueprints.domain.vo.BlueprintsListVO;
import com.ruoyi.blueprints.domain.vo.MallBuyPreVO;
import com.ruoyi.blueprints.mapper.BlueprintsCollectMapper;
import com.ruoyi.blueprints.mapper.BlueprintsDetailMapper;
import com.ruoyi.blueprints.mapper.BlueprintsMapper;
import com.ruoyi.blueprints.service.IBlueprintsService;
import com.ruoyi.common.core.domain.entity.SysDictData;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.utils.AssertUtils;
import com.ruoyi.common.utils.PageUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.order.domain.PayOrder;
import com.ruoyi.order.enums.PayStatusEnum;
import com.ruoyi.order.mapper.PayOrderMapper;
import com.ruoyi.pay.domain.dto.CalculateGoodsDTO;
import com.ruoyi.pay.domain.dto.CalculateGoodsResDTO;
import com.ruoyi.pay.domain.req.AlipayTradePreCreateReq;
import com.ruoyi.pay.strategy.GoodsStrategyManager;
import com.ruoyi.system.domain.PayConfig;
import com.ruoyi.system.enums.DictTypeEnum;
import com.ruoyi.system.enums.GoodsTypeEnum;
import com.ruoyi.system.enums.PayChannelEnum;
import com.ruoyi.system.enums.PaySubChannelEnum;
import com.ruoyi.system.service.ISysDictTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 图纸列表Service业务层处理
 * 
 * @author tds
 * @date 2025-03-03
 */
@Service
public class BlueprintsServiceImpl extends ServiceImpl<BlueprintsMapper, Blueprints> implements IBlueprintsService
{

    @Autowired
    private ISysDictTypeService dictTypeService;

    @Autowired
    private BlueprintsDetailMapper blueprintsDetailMapper;

    @Autowired
    private BlueprintsCollectMapper blueprintsCollectMapper;

    @Autowired
    private GoodsStrategyManager goodsStrategyManager;

    @Autowired
    private PayOrderMapper payOrderMapper;

    /**
     * 查询图纸列表
     * 
     * @param id 图纸列表主键
     * @return 图纸列表
     */
    @Override
    public BlueprintsInfoVO selectBlueprintsById(Long id)
    {
        // todo 增加缓存，为了防止缓存击穿，可以增加锁（因为集群不多，单体锁synchronized就可以了。）
        // 因为锁只在缓存不存在的时候会锁，对于大多数情况锁是不存在的，所以对性能影响不大
        // 缓存击穿：当一个key某个时间段非常热点，但缓存此时不存在或失效了，大量请求不走数据库，直接走到了数据库，导致性能下降甚至无法访问数据库了
        // 解决：
        // 1）热点数据缓存永不失效。缺点是增加了redis的存储负担 ，且不一定能知道那些是热点数据
        // 2）缓存预热，提前将将要被大量访问的数据缓存到redis。但是这得明确知道这个数据是热点数据
        // 3）加锁。推荐这种。在查询缓存数据时，如果不存在，就给该条数据加上锁。只有当时缓存不存在那批请求会锁住，后续缓存记录了都不会有锁了
        // 缓存雪崩：大量缓存集中过期，或者缓存服务器宕机，导致大量请求访问数据库，造成数据库瞬间压力过大，宕机
        // 解决：
        // 1）针对集中过期导致的雪崩，给缓存增加失效时间增加随机性，不要弄成一样的，这样来减少缓存集中过期
        // 2）针对缓存服务器宕机，只能在部署缓存服务器时使用哨兵模式或集群模式等，并做好缓存服务器监控告警
        // 缓存穿透：缓存和数据库都不存在，导致每次请求都会去查数据库，时的用户可能是攻击者。导致数据库压力过大甚至宕机
        // 解决：
        // 1）参数校验：比如将负数啊这些过滤掉。但是这个没法确定那些是真的有问题的参数
        // 2）缓存空对象：将空对象也给缓存下，这样即使缓存不存在，也会缓存一条数据（因为应对缓存击穿，做了锁这种情况，所以可以将空对象仅仅是某个字段为空，比如id）
        // 3）布隆过滤器：一种数据结构，占用空间小，可以用较少的空间存储更多的key数据。通过判断布隆过滤器中有无该数据判断数据是否存在。布隆过滤器
        // 其实是将一个元素进行了三次哈希运算，然后将一个bit数组对应的哈希结果的三个位置的位数据从0改为1。查询一个数据是否存在就是通过这个判断的。
        // 但是存在误判率，和位数组的大小和hash函数的分布有关。

        BlueprintsInfoVO vo = baseMapper.selectBlueprintsById(id);
        AssertUtils.notNull(vo, "图纸信息为空");

        // todo 将用户是否收藏使用redis的bitmap来判断
        // 查询图纸是否被登录用户收藏过
        BlueprintsCollect blueprintsCollect = blueprintsCollectMapper.selectOne(new LambdaQueryWrapper<BlueprintsCollect>()
                .eq(BlueprintsCollect::getBlueprintsId, id)
                .eq(BlueprintsCollect::getUserId, SecurityUtils.getUserId(false)));
        vo.setCollected(blueprintsCollect != null);

        return vo;
    }

    /**
     * 查询图纸列表列表
     * 
     * @param queryReq 图纸列表
     * @return 图纸列表
     */
    @Override
    public List<BlueprintsListVO> selectBlueprintsList(QueryBlueprintsListReq queryReq)
    {
        return getBaseMapper().selectBlueprintsList(queryReq);
    }

    /**
     * 新增图纸列表
     * 
     * @param addReq 图纸列表
     * @return 结果
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean insertBlueprints(AddBlueprintsReq addReq)
    {
        Blueprints addDo = BlueprintsConvert.INSTANCE.convert(addReq);

        LoginUser loginUser = SecurityUtils.getLoginUser();
        addDo.setUserId(loginUser.getUserId());
        addDo.setCreateBy(loginUser.getUsername());

        // 图纸编码不能重复
        Blueprints blueprints = baseMapper.selectOne(new LambdaQueryWrapper<Blueprints>().eq(Blueprints::getCode, addReq.getCode()));
        AssertUtils.isNull(blueprints, "图纸编码不能重复");
        // 保存图纸信息
        this.save(addDo);


        BlueprintsDetail detailDo = BlueprintsConvert.INSTANCE.convertDetail(addReq);
        detailDo.setBlueprintsId(addDo.getId());
        detailDo.setCreateBy(loginUser.getUsername());
        // 保存图纸详情
        blueprintsDetailMapper.insert(detailDo);

        return true;
    }

    /**
     * 修改图纸列表
     * 
     * @param editReq 图纸列表
     * @return 结果
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateBlueprints(EditBlueprintsReq editReq)
    {
        Blueprints editDo = BlueprintsConvert.INSTANCE.convert(editReq);

        LoginUser loginUser = SecurityUtils.getLoginUser();
        editDo.setUpdateBy(loginUser.getUsername());

        // 图纸编码不能重复
        Blueprints blueprints = baseMapper.selectOne(new LambdaQueryWrapper<Blueprints>()
                .eq(Blueprints::getCode, editReq.getCode()).ne(Blueprints::getId, editReq.getId()));
        AssertUtils.isNull(blueprints, "图纸编码不能重复");
        // 更新图纸信息
        baseMapper.updateBlueprintsById(editDo);

        // 查询图纸详情
        BlueprintsDetail blueprintsDetail = blueprintsDetailMapper.selectOne(new LambdaQueryWrapper<BlueprintsDetail>()
                .eq(BlueprintsDetail::getBlueprintsId, editDo.getId()));
        AssertUtils.notNull(blueprintsDetail, "图纸详情为空");

        BlueprintsDetail detailDo = BlueprintsConvert.INSTANCE.convertDetail(editReq);
        detailDo.setId(blueprintsDetail.getId());
        detailDo.setUpdateBy(loginUser.getUsername());
        // 更新图纸详情
        blueprintsDetailMapper.updateById(detailDo);

        return true;
    }

    /**
     * 批量删除图纸列表
     * 
     * @param ids 需要删除的图纸列表主键
     * @return 结果
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteBlueprintsByIds(Long[] ids)
    {
        AssertUtils.isTrue(ArrayUtil.isNotEmpty(ids), "参数错误，ids不能为空");
        // 查询图纸详情
        List<BlueprintsDetail> blueprintsDetails = blueprintsDetailMapper.selectList(new LambdaQueryWrapper<BlueprintsDetail>()
                .in(BlueprintsDetail::getBlueprintsId, ids));
        List<Long> detailIds = blueprintsDetails.stream().map(BlueprintsDetail::getId).collect(Collectors.toList());
        // 删除图纸详情
        blueprintsDetailMapper.deleteBatchIds(detailIds);

        // todo 其他业务逻辑.被购买的话无法删除.后面提供下架功能
        return this.removeByIds(Arrays.asList(ids));
    }

    @Override
    public List<BlueprintsCategoryVO> getBlueprintsCategoryTree() {
        List<SysDictData> houseTypes = dictTypeService.selectDictDataByType(DictTypeEnum.HOUSE_TYPE.getType());
        List<SysDictData> bayTypes = dictTypeService.selectDictDataByType(DictTypeEnum.BAY_TYPE.getType());
        List<SysDictData> blueprintsStyles = dictTypeService.selectDictDataByType(DictTypeEnum.BLUEPRINTS_STYLE.getType());

        List<BlueprintsCategoryVO> categoryVos = new ArrayList<>();
        categoryVos.add(new BlueprintsCategoryVO(DictTypeEnum.HOUSE_TYPE, houseTypes));
        categoryVos.add(new BlueprintsCategoryVO(DictTypeEnum.BAY_TYPE, bayTypes));
        categoryVos.add(new BlueprintsCategoryVO(DictTypeEnum.BLUEPRINTS_STYLE, blueprintsStyles));
        return categoryVos;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void changeListingStatus(ChangeBlueprintsListingStatusReq changeListingStatusReq) {
        Blueprints updateDo = new Blueprints();
        updateDo.setId(changeListingStatusReq.getId());
        updateDo.setListingStatus(changeListingStatusReq.getListingStatus());
        updateDo.setUpdateBy(SecurityUtils.getUsername());
        baseMapper.updateById(updateDo);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void collect(Long id) {
        Blueprints blueprints = baseMapper.selectById(id);
        AssertUtils.notNull(blueprints, "图纸为空");

        // 记录用户收藏的图纸
        BlueprintsCollect blueprintsCollect = new BlueprintsCollect();
        blueprintsCollect.setBlueprintsId(id);
        blueprintsCollect.setCreateBy(SecurityUtils.getUsername());
        blueprintsCollect.setUserId(SecurityUtils.getUserId());
        blueprintsCollectMapper.insert(blueprintsCollect);

        // 更新图纸收藏数
        baseMapper.updateCollectNum(id, 1);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void uncollect(Long id) {
        Blueprints blueprints = baseMapper.selectById(id);
        AssertUtils.notNull(blueprints, "图纸为空");

        // 删除用户收藏图纸关系
        blueprintsCollectMapper.delete(new LambdaQueryWrapper<BlueprintsCollect>()
                .eq(BlueprintsCollect::getBlueprintsId, id)
                .eq(BlueprintsCollect::getUserId, SecurityUtils.getUserId()));

        // 更新图纸收藏数
        baseMapper.updateCollectNum(id, -1);
    }

    @Override
    public MallBuyPreVO buyPre(Long id) {
        // 计算用户应付金额
        CalculateGoodsDTO calculateGoods = CalculateGoodsDTO.builder()
                .goodsNum(1)
                .blueprintsId(id)
                .userId(SecurityUtils.getUserId())
                .build();
        CalculateGoodsResDTO goodsRes = goodsStrategyManager.calculate(GoodsTypeEnum.BLUEPRINTS, calculateGoods);

//        // 实付金额为0，直接下单
//        if (BigDecimal.ZERO.compareTo(goodsRes.getActualAmount()) == 0) {
//            PayOrder payOrder = generateOrder(goodsRes, id);
//            goodsStrategyManager.paySuccess(GoodsTypeEnum.BLUEPRINTS, payOrder.getOrderNo());
//            return BigDecimal.ZERO;
//        }

        return MallBuyPreVO.builder()
                .actualAmount(goodsRes.getActualAmount())
                .discountAmount(goodsRes.getDiscountAmount())
                .build();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void buy(Long id) {
        // 计算用户应付金额
        CalculateGoodsDTO calculateGoods = CalculateGoodsDTO.builder()
                .goodsNum(1)
                .blueprintsId(id)
                .userId(SecurityUtils.getUserId())
                .build();
        CalculateGoodsResDTO goodsRes = goodsStrategyManager.calculate(GoodsTypeEnum.BLUEPRINTS, calculateGoods);

        if (BigDecimal.ZERO.compareTo(goodsRes.getActualAmount()) != 0) {
            throw new SecurityException("金额计算有误");
        }

        // 直接下单
        PayOrder payOrder = generateOrder(goodsRes, id);
        goodsStrategyManager.paySuccess(GoodsTypeEnum.BLUEPRINTS, payOrder.getOrderNo());
    }

    /**
     * 生成订单信息
     *
     * @param goodsRes 商品计算结果
     * @param blueprintsId 图纸id
     * @return
     */
    private PayOrder generateOrder(CalculateGoodsResDTO goodsRes, Long blueprintsId) {
        // 雪花算法生成订单号
        String orderNo = IdUtil.getSnowflakeNextIdStr();

        // 生成订单信息
        PayOrder payOrder = new PayOrder();
        payOrder.setCreateBy(SecurityUtils.getUsername(false));
        payOrder.setUserId(SecurityUtils.getUserId());
        payOrder.setOrderNo(orderNo);
        payOrder.setAmount(goodsRes.getAmount());
        payOrder.setSubject(goodsRes.getSubject());
        payOrder.setGoodsNum(1);
        payOrder.setGoodsType(GoodsTypeEnum.BLUEPRINTS.getCode());
        payOrder.setBlueprintsId(blueprintsId);
        payOrder.setChannel(PayChannelEnum.OTHER.getCode());
        payOrder.setSubChannel(PaySubChannelEnum.OTHER.getCode());
        payOrder.setStatus(PayStatusEnum.PAYING.getCode());
        payOrder.setDiscount(goodsRes.getDiscount());
        payOrder.setActualAmount(goodsRes.getActualAmount());
        payOrder.setDiscountAmount(goodsRes.getDiscountAmount());
        payOrderMapper.insert(payOrder);
        return payOrder;
    }
}
