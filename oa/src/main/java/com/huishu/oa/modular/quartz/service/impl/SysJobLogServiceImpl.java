package com.huishu.oa.modular.quartz.service.impl;


import cn.hutool.core.convert.Convert;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.huishu.oa.modular.quartz.dao.SysJobLogMapper;
import com.huishu.oa.modular.quartz.model.SysJobLog;
import com.huishu.oa.modular.quartz.service.ISysJobLogService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 定时任务调度日志信息 服务实现类
 * <p>
 * Created by zx
 * Date 2019/01/12 10:11
 * version:1.0
 */
@Service
public class SysJobLogServiceImpl extends ServiceImpl<SysJobLogMapper, SysJobLog> implements ISysJobLogService {

    @Resource
    private SysJobLogMapper jobLogMapper;


    /**
     * 获取quartznew调度器日志的计划任务
     *
     * @return 调度任务日志集合
     */
    @Override
    public List<Map<String, Object>> selectJobLogList(String jobName, String status, String methodName, String beginTime, String endTime) {
        return jobLogMapper.selectJobLogList(jobName, status, methodName, beginTime, endTime);
    }
    /**
     * 通过调度任务日志ID查询调度信息
     *
     * @return 调度任务日志对象信息
     */
    @Override
    public SysJobLog selectJobLogById(Integer jobLogId)
    {
        return jobLogMapper.selectJobLogById(jobLogId);
    }

    /**
     * 新增任务日志
     *
     * @param jobLog 调度日志信息
     */
    @Override
    public void addJobLog(SysJobLog jobLog)
    {
        jobLogMapper.insertJobLog(jobLog);
    }

    /**
     * 批量删除调度日志信息
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteJobLogByIds(String ids)
    {
        return jobLogMapper.deleteJobLogByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除任务日志
     *
     * @param jobId 调度日志ID
     */
    @Override
    public int deleteJobLogById(Integer jobId)
    {
        return jobLogMapper.deleteJobLogById(jobId);
    }

    /**
     * 清空任务日志
     */
    @Override
    public void cleanJobLog()
    {
        jobLogMapper.cleanJobLog();
    }
}
