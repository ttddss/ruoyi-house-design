package com.ruoyi.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author tds
 * @version 1.0.0
 * @Company
 * @ClassName DogHouse
 * @Description TODO（这里用一句话描述这个类的作用)
 * @Date 2025-07-31 11:26
 */
@Data
@Component
@NoArgsConstructor
public class DogHouse {

    private Dog dog;

    @Autowired
    DogHouse(Dog dog) {
        this.dog = dog;
    }
}
