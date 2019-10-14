package com.huishu.oa.modular.office.controller;

import com.huishu.oa.config.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zx
 */
@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private RedisUtil redisUtil;

    @RequestMapping("/addCache")
    public void addCache() {
        redisUtil.set("key1", "helloWorld");
    }

    @RequestMapping("/getCache")
    public String getCache() {
        return redisUtil.get("key1").toString();
    }

}
