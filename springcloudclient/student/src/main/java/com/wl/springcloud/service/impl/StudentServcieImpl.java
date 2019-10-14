package com.wl.springcloud.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import com.wl.springcloud.dao.StudentMapper;
import com.wl.springcloud.entity.Course;
import com.wl.springcloud.entity.Student;
import com.wl.springcloud.feginservice.CourseFegin;
import com.wl.springcloud.service.IStudentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 王柳
 * Date 2019/10/2 15:49
 * version:1.0
 */
@Service
@Slf4j
public class StudentServcieImpl extends ServiceImpl<StudentMapper, Student> implements IStudentService {

    @Autowired
    private CourseFegin courseFegin;

    @Resource
    private StudentMapper studentMapper;

    @LcnTransaction
    @Transactional
    @Override
    public Map saveStudent(Map param) throws Exception {
        Map map = new HashMap();
        map.put("code", "-1");
        Map course = (Map) param.get("course");
        Map student = (Map) param.get("student");
        Student student1 = new Student();
        student1.setUserName((String) student.get("name"));
        student1.setAge(Integer.valueOf((String) student.get("age")));
        studentMapper.insert(student1);
        log.info("学生服务----->>saveStudent");
        Map courseMap = courseFegin.saveCourse(course);
        log.info("课程服务----->>saveCourse------>> courseMap: " + courseMap);
        if (courseMap == null){
            throw new Exception("课程服务发生熔断，调用课程服务失败，开始回退课程服务--->>选课");
        }
        Boolean exceFlag = (Boolean) student.get("exceFlag");
        if (exceFlag) {
            throw new RuntimeException();
        }
        map.put("code", "1");
        return map;
    }
}
