package com.huishu.oa.modular.quartz.task;


import com.huishu.oa.core.util.DateUtil;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 定时任务调度测试
 * <p>
 * Created by zx
 * Date 2019/01/12 10:11
 * version:1.0
 */
@Component("ryTask")
public class RyTask {
    public void ryParams(String params) {
        System.out.println("执行有参方法：" + DateUtil.getBeforeDayDate(Integer.valueOf(params)));
    }

    public void ryNoParams() {
        System.out.println("延时前执行无参方法");
        ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(1,
                new BasicThreadFactory.Builder().namingPattern("example-schedule-pool-%d").daemon(true).build());
        executorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                System.out.println("进入到里面的方法");
            }
        },0,1, TimeUnit.MINUTES);
        System.out.println("本程序存在1分钟秒后自动退出");
    }


}
