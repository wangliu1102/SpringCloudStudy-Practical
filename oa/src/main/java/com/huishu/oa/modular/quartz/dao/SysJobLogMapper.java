package com.huishu.oa.modular.quartz.dao;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.huishu.oa.modular.quartz.model.SysJobLog;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 调度任务日志信息 Mapper接口
 * <p>
 * Created by zx
 * Date 2019/01/12 10:11
 * version:1.0
 */
public interface SysJobLogMapper extends BaseMapper<SysJobLog> {

    /**
     * 获取quartznewnew调度器日志的计划任务
     *
     * @return 调度任务日志集合
     */
    List<Map<String, Object>> selectJobLogList(@Param("jobName") String jobName, @Param("status") String status,
                                               @Param("methodName") String methodName, @Param("beginTime") String beginTime,
                                               @Param("endTime") String endTime);

    /**
     * 查询所有调度任务日志
     *
     * @return 调度任务日志列表
     */
    public List<SysJobLog> selectJobLogAll();

    /**
     * 通过调度任务日志ID查询调度信息
     *
     * @param jobLogId 调度任务日志ID
     * @return 调度任务日志对象信息
     */
    public SysJobLog selectJobLogById(Integer jobLogId);

    /**
     * 新增任务日志
     *
     * @param jobLog 调度日志信息
     * @return 结果
     */
    public int insertJobLog(SysJobLog jobLog);

    /**
     * 批量删除调度日志信息
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteJobLogByIds(String[] ids);

    /**
     * 删除任务日志
     *
     * @param jobId 调度日志ID
     * @return 结果
     */
    public int deleteJobLogById(Integer jobId);

    /**
     * 清空任务日志
     */
    public void cleanJobLog();
}
