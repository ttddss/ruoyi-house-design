package com.ruoyi.bean.proxybean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
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
public class Pa {

    @Autowired
    private Pb pb;

    public Pb getPb() {
        return pb;
    }

    public void callPb() {
        System.out.println(pb);
    }

    public void fa() {

    }
}
