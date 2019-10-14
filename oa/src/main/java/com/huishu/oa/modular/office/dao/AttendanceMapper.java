package com.huishu.oa.modular.office.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.huishu.oa.modular.office.model.Attendance;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 考勤表 Mapper
 * </p>
 *
 * @author yubb
 * @since 2018-12-07
 */
public interface AttendanceMapper extends BaseMapper<Attendance> {

    /**
     * 获取导出列表总数
     */
    Integer selectListCount(@Param("condition") String condition,
                            @Param("startTime") String startTime, @Param("endTime") String endTime);

    /**
     * 获取导出列表
     */
    List<Map<String, Object>> selectExportAll(@Param("page") Pagination page, @Param("condition") String condition,
                                              @Param("startTime") String startTime, @Param("endTime") String endTime);


    /**
     * 获取签到列表
     */
    List<Map<String, Object>> getList(@Param("attendance")Attendance attendance,@Param("page")Page<Attendance> page);


}
