package com.huishu.oa.modular.quartz.util;


import cn.stylefeng.roses.core.util.SpringContextHolder;
import com.huishu.oa.core.common.constant.Const;
import com.huishu.oa.core.common.constant.ScheduleConstants;
import com.huishu.oa.core.util.ToolUtil;
import com.huishu.oa.modular.quartz.model.SysJob;
import com.huishu.oa.modular.quartz.model.SysJobLog;
import com.huishu.oa.modular.quartz.service.ISysJobLogService;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * 定时任务处理
 * <p>
 * Created by zx
 * Date 2019/01/12 10:11
 * version:1.0
 */
@DisallowConcurrentExecution
public class ScheduleJob extends QuartzJobBean {
    private static final Logger log = LoggerFactory.getLogger(ScheduleJob.class);

    private ExecutorService service = Executors.newSingleThreadExecutor();

    private final static ISysJobLogService jobLogService = SpringContextHolder.getBean("sysJobLogServiceImpl");

    @Override
    protected void executeInternal(JobExecutionContext context) {
        SysJob job = new SysJob();
        BeanUtils.copyBeanProp(job, context.getMergedJobDataMap().get(ScheduleConstants.TASK_PROPERTIES));

        SysJobLog jobLog = new SysJobLog();
        jobLog.setJobName(job.getJobName());
        jobLog.setJobGroup(job.getJobGroup());
        jobLog.setMethodName(job.getMethodName());
        jobLog.setMethodParams(job.getMethodParams());
        jobLog.setCreateTime(new Date());

        long startTime = System.currentTimeMillis();

        try {
            // 执行任务
            log.info("任务开始执行 - 名称：{} 方法：{}", job.getJobName(), job.getMethodName());
            ScheduleRunnable task = new ScheduleRunnable(job.getJobName(), job.getMethodName(), job.getMethodParams());
            Future<?> future = service.submit(task);
            future.get();
            long times = System.currentTimeMillis() - startTime;
            // 任务状态 0：成功 1：失败
            jobLog.setStatus(Const.SUCCESS);
            jobLog.setJobMessage(job.getJobName() + " 总共耗时：" + times + "毫秒");

            log.info("任务执行结束 - 名称：{} 耗时：{} 毫秒", job.getJobName(), times);
        } catch (Exception e) {
            log.info("任务执行失败 - 名称：{} 方法：{}", job.getJobName(), job.getMethodName());
            log.error("任务执行异常  - ：", e);
            long times = System.currentTimeMillis() - startTime;
            jobLog.setJobMessage(job.getJobName() + " 总共耗时：" + times + "毫秒");
            // 任务状态 0：成功 1：失败
            jobLog.setStatus(Const.FAIL);
            jobLog.setExceptionInfo(ToolUtil.substring(e.getMessage(), 0, 2000));
        } finally {
            jobLogService.addJobLog(jobLog);
        }
    }
}
