package com.wl.springcloud;

import com.codingapi.txlcn.tc.config.EnableDistributedTransaction;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Created by 王柳
 * Date 2019/10/2 15:26
 * version:1.0
 */
@SpringBootApplication
@EnableDistributedTransaction
@MapperScan("com.wl.springcloud.dao")
public class CourseApplication {
    public static void main(String[] args) {
        SpringApplication.run(CourseApplication.class, args);
    }
}
