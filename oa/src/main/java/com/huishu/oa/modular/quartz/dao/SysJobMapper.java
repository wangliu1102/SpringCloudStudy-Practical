package com.huishu.oa.modular.quartz.dao;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.huishu.oa.modular.quartz.model.SysJob;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 调度任务信息 Mapper接口
 * <p>
 * Created by zx
 * Date 2019/01/12 10:11
 * version:1.0
 */
public interface SysJobMapper extends BaseMapper<SysJob> {

    /**
     * 查询调度任务日志集合
     *
     * @return 操作日志集合
     */
    List<Map<String, Object>> selectJobList(@Param("jobName") String jobName, @Param("status") String status, @Param("methodName") String methodName);

    /**
     * 查询所有调度任务
     *
     * @return 调度任务列表
     */
    public List<SysJob> selectJobAll();

    /**
     * 通过调度ID查询调度任务信息
     *
     * @param jobId 调度ID
     * @return 角色对象信息
     */
    public SysJob selectJobById(Integer jobId);

    /**
     * 通过调度ID删除调度任务信息
     *
     * @param jobId 调度ID
     * @return 结果
     */
    public int deleteJobById(Integer jobId);

    /**
     * 批量删除调度任务信息
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteJobByIds(Integer[] ids);

    /**
     * 修改调度任务信息
     *
     * @param job 调度任务信息
     * @return 结果
     */
    public int updateJob(SysJob job);

    /**
     * 新增调度任务信息
     *
     * @param job 调度任务信息
     * @return 结果
     */
    public int insertJob(SysJob job);
}
