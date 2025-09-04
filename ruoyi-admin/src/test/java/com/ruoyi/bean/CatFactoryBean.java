package com.ruoyi.bean;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.stereotype.Component;

/**
 * @author tds
 * @version 1.0.0
 * @Company
 * @ClassName DogFactoryBean
 * @Description TODO（这里用一句话描述这个类的作用)
 * @Date 2025-07-31 9:51
 */
//@Component("catFactoryBean")
public class CatFactoryBean implements FactoryBean<Cat> {

    @Override
    public Cat getObject() throws BeansException {
        return Cat.builder().name("hhhh").build();
    }

    @Override
    public Class<?> getObjectType() {
        return Cat.class;
    }

}
