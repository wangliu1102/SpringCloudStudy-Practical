package com.wl.springcloud;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author 王柳
 * @date 2019/11/7 14:37
 */
@SpringBootApplication
@EnableAdminServer
public class AdminMonitorApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdminMonitorApplication.class, args);
    }
}
