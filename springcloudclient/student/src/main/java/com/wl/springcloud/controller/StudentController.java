package com.wl.springcloud.controller;

import com.wl.springcloud.service.IStudentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 王柳
 * Date 2019/10/2 15:49
 * version:1.0
 */
@Api(tags = "学生服务")
@Controller
@RequestMapping("/student")
@Slf4j
@RefreshScope
public class StudentController {

    @Value("${word}")
    private String word;

    @Autowired
    private IStudentService studentService;

    @ApiOperation(value = "保存学生和选课")
    @RequestMapping(value = "/saveStudentAndCourse", method = RequestMethod.POST)
    @ResponseBody
    public Object saveStudentAndCourse(@RequestBody Map<String, Object> requestMap) {
        log.info("进入学生服务----->>添加学生");
        Map map = new HashMap();
        try {
            map = studentService.saveStudent(requestMap);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("code", "-1");
            map.put("msg", "保存失败");
        }
        return map;
    }

    @ApiOperation(value = "查询学生列表")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public Object listCourse() {
        log.info("进入学生服务----->>学生列表");
        log.info("word: " + word);
        return studentService.list();
    }
}
