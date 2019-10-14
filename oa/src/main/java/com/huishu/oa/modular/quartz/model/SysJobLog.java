package com.huishu.oa.modular.quartz.model;

import com.baomidou.mybatisplus.annotations.TableName;
import com.huishu.oa.modular.BaseModel;
import lombok.Data;

import java.util.Date;

/**
 * 定时任务调度日志表 sys_job_log
 * <p>
 * Created by zx
 * Date 2019/01/12 10:11
 * version:1.0
 */
@TableName("sys_job_log")
@Data
public class SysJobLog extends BaseModel<SysJobLog> {

    /**
     * 任务名称
     */
    private String jobName;

    /**
     * 任务组名
     */
    private String jobGroup;

    /**
     * 任务方法
     */
    private String methodName;

    /**
     * 方法参数
     */
    private String methodParams;

    /**
     * 日志信息
     */
    private String jobMessage;

    /**
     * 执行状态（0正常 1失败）
     */
    private String status;

    /**
     * 异常信息
     */
    private String exceptionInfo;
    /**
     * 异常信息
     */
    private Date startTime;
    /**
     * 异常信息
     */
    private Date endTime;



}
