package com.ruoyi.web.controller.pay;

import cn.hutool.extra.servlet.ServletUtil;
import com.ruoyi.pay.service.IAlipayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;



/**
 *
 *
 * @author tongdashuai
 * @version 1.0
 * @description
 * @date 2025/3/27 17:53
 */
@Slf4j
@Controller
@RequestMapping("/page/alipay")
public class AlipayPageController {



    /**
     * 子商户授权回调
     */
    @GetMapping("/authCallback")
    public String authCallback(HttpServletRequest request) {
        log.info("子商户授权回调参数：" + ServletUtil.getParamMap(request));
        return "success.html";
    }


}
