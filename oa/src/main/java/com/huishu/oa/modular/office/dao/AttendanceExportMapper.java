package com.huishu.oa.modular.office.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.huishu.oa.modular.office.model.AttendanceExport;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 考勤表 Mapper
 * </p>
 *
 * @author yubb
 * @since 2018-12-07
 */
public interface AttendanceExportMapper extends BaseMapper<AttendanceExport> {

    List<AttendanceExport> selectExportExcel(@Param("condition") String condition,
                                             @Param("startTime") String startTime, @Param("endTime") String endTime);

}
