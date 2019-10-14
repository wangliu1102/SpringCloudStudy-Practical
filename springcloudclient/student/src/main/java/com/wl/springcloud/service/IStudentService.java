package com.wl.springcloud.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wl.springcloud.entity.Student;

import java.util.Map;

/**
 * Created by 王柳
 * Date 2019/10/2 15:49
 * version:1.0
 */
public interface IStudentService extends IService<Student> {

    Map saveStudent(Map param) throws Exception;
}
