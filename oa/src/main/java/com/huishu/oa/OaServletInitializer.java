package com.huishu.oa;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * OA Web程序启动类
 *
 * @author zx
 * @Date 2019-05-21 9:43
 */
public class OaServletInitializer extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(OaApplication.class);
    }
}
