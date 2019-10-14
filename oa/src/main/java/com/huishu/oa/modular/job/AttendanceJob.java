package com.huishu.oa.modular.job;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.huishu.oa.core.util.ToolOS;
import com.huishu.oa.modular.office.model.Attendance;
import com.huishu.oa.modular.office.service.IAttendanceService;
import com.huishu.oa.modular.system.model.User;
import com.huishu.oa.modular.system.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Author: yubb
 * Date: Created in 2018/10/30 9:16
 * Copyright: Copyright (c) 2018
 * Description：  考勤定时任务
 */

@Component
@Slf4j
public class AttendanceJob {

    @Autowired
    private IAttendanceService iAttendanceService;
    @Autowired
    private IUserService userService;


    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HHmmss");

    //@Scheduled(cron = "00 50 23 * * ?") //每天23:50执行一次
    @Scheduled(cron="${jobs.schedule}")
    public void test1()  {
        log.info("==============考勤定时任务开始===============");
        try{
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String nowDate = sdf.format(new Date());
            Wrapper<User> userWrapper = new EntityWrapper<>();
            List<User>  userList = userService.selectList(userWrapper);

            for(User user :userList){
                Wrapper<Attendance> wrapper = new EntityWrapper<>();
                wrapper = wrapper.where("date_format(SIGNIN_TIME ,'%Y-%m-%d' ) = {0}",nowDate);
                wrapper = wrapper.eq("create_by", user.getId().toString());
                Attendance attendance = iAttendanceService.selectOne(wrapper);
                if(attendance ==null){
                    Attendance attendanceOne = new Attendance();
                    attendanceOne.setState("LEAVE");
                    attendance.setWeek(ToolOS.getWeek());
                    attendanceOne.setCreateTime(new Date());
                    attendanceOne.setCreateBy(user.getId().toString());
                    iAttendanceService.insertOneAttendance(attendanceOne);
                }
            }
        }catch(Exception e){
            e.printStackTrace();
            log.info(e.getMessage());
        }
        log.info("==============考勤定时任务结束===============");

    }
}
