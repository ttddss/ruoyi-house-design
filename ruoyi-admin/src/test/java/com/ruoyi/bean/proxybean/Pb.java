package com.ruoyi.bean.proxybean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author tds
 * @version 1.0.0
 * @Company
 * @ClassName Pa
 * @Description TODO（这里用一句话描述这个类的作用)
 * @Date 2025-08-01 4:40
 */
@Component
public class Pb {

    @Autowired
    private Pa pa;

    public void callPa() {
        System.out.println(pa);
    }
}
