package com.wl.springcloud.controller;

import com.wl.springcloud.service.ICourseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * Created by 王柳
 * Date 2019/10/2 15:40
 * version:1.0
 */
@Slf4j
@Controller
@RequestMapping("/course")
public class CourseController {

    @Autowired
    private ICourseService courseService;

    @ResponseBody
    @RequestMapping("/saveCourse")
    public Object saveCourse(@RequestBody Map<String, Object> requestMap) {
        log.info("进入课程服务----->>选课");
        return courseService.saveCourse(requestMap);
    }

    @RequestMapping("/list")
    @ResponseBody
    public Object listCourse() {
        log.info("进入课程服务------>>课程列表");
        return courseService.list();
    }
}
