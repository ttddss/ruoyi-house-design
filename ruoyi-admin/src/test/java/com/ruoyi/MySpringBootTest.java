package com.ruoyi;

import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.member.domain.VipUser;
import com.ruoyi.member.mapper.VipUserMapper;
import com.ruoyi.system.mapper.SysUserMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.SimpleBeanDefinitionRegistry;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 *
 *
 * @author tongdashuai
 * @version 1.0
 * @description
 * @module
 * @date 2024/8/6 10:39
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class MySpringBootTest {

    @Resource
    private SysUserMapper userMapper;

    @Resource
    private VipUserMapper vipUserMapper;

    @Test
    @Transactional
    public void test1() {
        // 开启了事务（使用的同一个sqlsession），两个一样的sql查询第二次走的mybatis的一级缓存
        SysUser sysUser = userMapper.selectUserById(1L);
        System.out.println("1:" + sysUser.getUserId());
        sysUser = userMapper.selectUserById(1L);
        System.out.println("2:" + sysUser.getUserId());

        // 执行更新语句后，缓存会失效，更新的表不需要和查询的表一致
        VipUser vipUser = new VipUser();
        vipUser.setId(1000L);
        int i = vipUserMapper.updateById(vipUser);
        System.out.println("更新了" + i + "行");

        sysUser = userMapper.selectUserById(1L);
        System.out.println("3:" + sysUser.getUserId());
    }

    @Test
    public void test2() {
        // 这里没开启事务，不是同一个sqlsession，但是如果开启了二级缓存，那么会走二级缓存
        // 二级缓存是针对一个mapper的
        // <cache eviction="FIFO" flushInterval="60000" size="512" readOnly="true"/>
        SysUser sysUser = userMapper.selectUserById(1L);
        System.out.println("1:" + sysUser.getUserId());
        sysUser = userMapper.selectUserById(1L);
        System.out.println("2:" + sysUser.getUserId());

        // 执行更新语句后，二级缓存会失效
        SysUser sysUser2 = new SysUser();
        sysUser2.setUserId(1000L);
        int i = userMapper.updateUser(sysUser2);
        System.out.println("更新了" + i + "行");

        sysUser = userMapper.selectUserById(1L);
        System.out.println("3:" + sysUser.getUserId());
    }


}
