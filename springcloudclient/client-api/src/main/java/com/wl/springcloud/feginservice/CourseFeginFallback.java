package com.wl.springcloud.feginservice;

import com.wl.springcloud.entity.Course;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Created by 王柳
 * Date 2019/10/2 16:05
 * version:1.0
 */
@Component
@Slf4j
public class CourseFeginFallback implements CourseFegin {

    @Override
    public Map saveCourse(Map param) {
        log.info("-------------saveCourse发生了熔断!");
        return null;
    }
}
