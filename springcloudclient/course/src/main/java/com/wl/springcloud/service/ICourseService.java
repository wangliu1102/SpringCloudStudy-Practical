package com.wl.springcloud.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wl.springcloud.entity.Course;

import java.util.Map;

/**
 * Created by 王柳
 * Date 2019/10/2 15:37
 * version:1.0
 */
public interface ICourseService extends IService<Course> {

    Map saveCourse(Map param);

}
