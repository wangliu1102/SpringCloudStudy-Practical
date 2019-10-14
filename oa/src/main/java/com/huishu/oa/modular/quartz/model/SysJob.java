package com.huishu.oa.modular.quartz.model;

import cn.stylefeng.roses.core.util.ToolUtil;
import com.baomidou.mybatisplus.annotations.TableName;
import com.huishu.oa.modular.BaseModel;
import com.huishu.oa.modular.quartz.util.CronUtils;
import lombok.Data;

import java.util.Date;

/**
 * 定时任务调度表 sys_job
 * <p>
 * Created by zx
 * Date 2019/01/12 10:11
 * version:1.0
 */
@TableName("sys_job")
@Data
public class SysJob extends BaseModel<SysJob> {

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
     * cron执行表达式
     */
    private String cronExpression;

    /**
     * cron计划策略（0 默认策略 1立即执行 2执行一次 3放弃执行）
     */
    private String misfirePolicy;

    /**
     * 任务状态（0正常 1暂停）
     */
    private String status;

    /**
     * 是否并发执行（0允许 1禁止）
     */
    private String concurrent;

    /**
     * 备注
     */
    private String remark;


    public Date getNextValidTime() {
        if (ToolUtil.isNotEmpty(cronExpression)) {
            return CronUtils.getNextExecution(cronExpression);
        }
        return null;
    }


}
