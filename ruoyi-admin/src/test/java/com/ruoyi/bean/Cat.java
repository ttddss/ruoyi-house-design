package com.ruoyi.bean;

import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author tds
 * @version 1.0.0
 * @Company
 * @ClassName Cat
 * @Description TODO（这里用一句话描述这个类的作用)
 * @Date 2025-07-31 9:54
 */
@Getter
@Setter
@Component
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cat {

    private String name;

    private Integer age;

    private Date birthday;

    private Dog parent;

    @Autowired
    private Dog dog;

    @Async
    public void async() {
        System.out.println(1111);
    }
}
