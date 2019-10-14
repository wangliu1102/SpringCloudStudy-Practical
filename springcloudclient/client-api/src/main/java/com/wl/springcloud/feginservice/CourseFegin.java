package com.wl.springcloud.feginservice;

import com.wl.springcloud.entity.Course;
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

    @RequestMapping(value = "/course/saveCourse", method = RequestMethod.POST)
    Map saveCourse(Map param);
}
