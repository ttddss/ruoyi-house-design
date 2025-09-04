package com.ruoyi.web.controller.common;

import cn.hutool.core.thread.ThreadUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author tds
 * @version 1.0.0
 * @Company
 * @ClassName TestController
 * @Description TODO（这里用一句话描述这个类的作用)
 * @Date 2025-06-03 18:44
 */
@RestController
@RequestMapping("/commontest")
public class CommonTestController {

    private AtomicInteger times = new AtomicInteger(0);

    @GetMapping("/test1")
    public String test1() {
        int i = times.getAndAdd(1);
        System.out.println("test1:" + i);
        ThreadUtil.sleep(1000 * 1);
        return i + "";
    }
}
