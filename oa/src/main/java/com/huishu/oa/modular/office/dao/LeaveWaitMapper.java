package com.huishu.oa.modular.office.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.huishu.oa.modular.office.model.Leave;

/**
 * <p>
 * 请假表 Mapper 接口
 * </p>
 *
 * @author tb
 * @since 2019-06-03
 */
public interface LeaveWaitMapper extends BaseMapper<Leave> {
    /**
     * 分页查询
     *
     * @param page
     * @return
     */
    List<Map<String, Object>>  leaveList(@Param("leave") Leave leave,@Param("page") Page<Leave> page);

    /**
     * 修改请假状态
     * caowy
     */
    int setStatus(@Param("leaveId") Integer leaveId, @Param("status") int status);

    /**
     * 获取请假列表
     * caowy
     */
    Page<Map<String, Object>> list(@Param("page") Page page, @Param("condition") String condition);
}
