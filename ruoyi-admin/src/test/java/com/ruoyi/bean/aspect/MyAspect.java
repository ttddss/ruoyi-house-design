package com.ruoyi.bean.aspect;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

/**
 * @author tds
 * @version 1.0.0
 * @Company
 * @ClassName MyAspect
 * @Description TODO（这里用一句话描述这个类的作用)
 * @Date 2025-08-01 4:35
 */
@Aspect
@Component
public class MyAspect {

    @Pointcut("execution(* com.ruoyi.bean.proxybean.*.*(..))")
    public void pointcut() {
    }



    @Before("pointcut()")
    public void before() {
        System.out.println("before");
    }
}
