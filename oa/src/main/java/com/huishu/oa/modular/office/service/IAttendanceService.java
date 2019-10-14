package com.huishu.oa.modular.office.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.huishu.oa.modular.office.model.Attendance;
import com.huishu.oa.modular.office.model.AttendanceExport;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 考勤表 服务实现类
 * </p>
 *
 * @author yubb
 * @since 2019/06/06
 */
public interface IAttendanceService extends IService<Attendance> {

    /**
     * 签到
     * @author yubb
     * @Date 2019/06/06 22:20
     */
    String insertAttendance();

    /**
     * 签退
     * @author yubb
     * @Date 2019/06/06 5:00 PM
     */
    String updateAttendance();

    /**
     * 签退
     * @author yubb
     * @Date 2019/06/06 5:00 PM
     */
    void insertOneAttendance(Attendance attendance);

    /**
     * 导出到excel
     * @author yubb
     * @Date 2019/06/06 5:00 PM
     */
    void exportExcel(String condition, String startTime, String endTime,String exportFlag, HttpServletRequest request, HttpServletResponse response);

    /**
     * 获取签到列表
     * @author yubb
     * @Date 2019/06/06 5:00 PM
     */
    List<Map<String, Object>> getList(Attendance attendance,Page<Attendance> page);
    /**
     * 查询总条数
     * @author yubb
     * @Date 2019/06/06 5:00 PM
     */
    int getCount(String condition, String startTime, String endTime);

    List<AttendanceExport> selectExportExcel(String condition, String startTime, String endTime);


}
