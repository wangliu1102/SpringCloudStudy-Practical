package com.wl.springcloud.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import com.wl.springcloud.dao.CourseMapper;
import com.wl.springcloud.entity.Course;
import com.wl.springcloud.service.ICourseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 王柳
 * Date 2019/10/2 15:38
 * version:1.0
 */
@Service
@Slf4j
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements ICourseService {

    @Resource
    private CourseMapper courseMapper;

    @LcnTransaction
    @Transactional
    @Override
    public Map saveCourse(Map param) {
        Map map = new HashMap();
        map.put("code", "-1");
        Boolean exceFlag = (Boolean) param.get("exceFlag");
        if (exceFlag) {
            throw new RuntimeException();
        } else {
            Course course = new Course();
            course.setName((String) param.get("name"));
            courseMapper.insert(course);
            map.put("code", "1");
        }
        return map;
    }
}
