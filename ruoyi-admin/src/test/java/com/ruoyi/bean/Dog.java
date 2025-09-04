package com.ruoyi.bean;

import cn.hutool.db.DaoTemplate;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author tds
 * @version 1.0.0
 * @Company
 * @ClassName Dog
 * @Description TODO（这里用一句话描述这个类的作用)
 * @Date 2025-07-28 12:20
 */
@Component
//@DependsOn("dog")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Slf4j
public class Dog implements SmartInitializingSingleton {

    private String name;

    private Integer age;

    private Date birthday;

    private Dog parent;

    @Lazy
    @Autowired
    private Cat cat;


    @Override
    public void afterSingletonsInstantiated() {
        log.info("容器所有的bean都被初始化后，调用afterSingletonsInstantiated()方法");
    }

    public void callAsync() {
        cat.async();
    }
}
