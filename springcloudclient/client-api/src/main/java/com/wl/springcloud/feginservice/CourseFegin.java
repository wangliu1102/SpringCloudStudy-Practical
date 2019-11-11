package com.wl.springcloud.feginservice;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

/**
 * Created by 王柳
 * Date 2019/10/2 16:04
 * version:1.0
 */
@FeignClient(name = "course", fallback = CourseFeginFallback.class)
public interface CourseFegin {

    /**
     * @description  指明controller层的url地址和请求方式
     * @author 王柳
     * @date 2019/11/8 15:22
     * @params [param]
     */
    @RequestMapping(value = "/course/saveCourse", method = RequestMethod.POST)
    Map saveCourse(Map param);
}
