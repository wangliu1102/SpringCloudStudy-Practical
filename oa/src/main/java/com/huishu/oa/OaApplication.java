package com.huishu.oa;

import cn.stylefeng.roses.core.config.WebAutoConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * SpringBoot方式启动类
 *
 * @author zx
 * @Date 2019/5/21 12:06
 */
@SpringBootApplication(exclude = WebAutoConfiguration.class)
public class OaApplication {

    private final static Logger logger = LoggerFactory.getLogger(OaApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(OaApplication.class, args);
        logger.info("OaApplication is success!");
    }
}
