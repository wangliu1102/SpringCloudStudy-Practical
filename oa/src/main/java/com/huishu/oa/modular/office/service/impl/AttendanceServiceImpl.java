package com.huishu.oa.modular.office.service.impl;

import static cn.stylefeng.roses.core.util.HttpContext.getIp;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.huishu.oa.core.common.GlobalData;
import com.huishu.oa.core.common.annotion.DataScope;
import com.huishu.oa.core.common.constant.Const;
import com.huishu.oa.core.excel.ExcelUtil;
import com.huishu.oa.core.shiro.ShiroKit;
import com.huishu.oa.core.util.DateUtil;
import com.huishu.oa.core.util.ToolOS;
import com.huishu.oa.core.util.poi.PoiUtil;
import com.huishu.oa.core.util.poi.WriteExcelDataDelegated;
import com.huishu.oa.modular.office.dao.AttendanceExportMapper;
import com.huishu.oa.modular.office.dao.AttendanceMapper;
import com.huishu.oa.modular.office.model.Attendance;
import com.huishu.oa.modular.office.model.AttendanceExport;
import com.huishu.oa.modular.office.service.IAttendanceService;
import com.huishu.oa.modular.system.dao.DictMapper;
import com.huishu.oa.modular.system.model.Dict;

import lombok.extern.slf4j.Slf4j;

/**
 * 签到签退
 *
 * @author yubb
 * @Date 2019-05-05 22:20
 */
@Service
@Slf4j
public class AttendanceServiceImpl extends ServiceImpl<AttendanceMapper, Attendance> implements IAttendanceService {

    @Resource
    private AttendanceMapper attendanceMapper;

    @Resource
    private AttendanceExportMapper exportMapper;

    @Resource
    private DictMapper dictMapper;

    /**
     * 签到
     *
     * @author yubb
     * @Date 2018/12/23 5:00 PM
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String insertAttendance() {
        String normalTime = null ;
        List<Dict> list = dictMapper.selectByParentCode("SIGNTIME");
        log.info("Dictlist=="+list);
        for (Dict dict : list) {
            if(("上班时间").equals(dict.getName())){
                normalTime = dict.getCode();
            }
        }
        log.info("nomalTime=="+normalTime);
        //签到时根据日期和用户查询，当天签到了不能在签到了
        Wrapper<Attendance> wrapper = new EntityWrapper<>();
        wrapper = wrapper.where("date_format(SIGNIN_TIME ,'%Y-%m-%d' ) = {0}",DateUtil.formatDate(new Date(), "yyyy-MM-dd"));
        wrapper = wrapper.eq("create_by", ShiroKit.getUser().getUserId());
        Integer count = attendanceMapper.selectCount(wrapper);

        if(count>0){
            return GlobalData.CONFIGPARAMETERS.get("signagin")==null?Const.SIGNAGIN:GlobalData.CONFIGPARAMETERS.get("signagin");
        }else{
            String normalTimeNew =DateUtil.formatDate(new Date(), "yyyy-MM-dd")+" "+normalTime;
            Attendance attendance = new Attendance();
            attendance.setSigninTime(new Date());
            attendance.setCreateBy(ShiroKit.getUser().getUserId());
            attendance.setCreateTime(new Date());
            Date normalDate=DateUtil.parseTime(normalTimeNew);
            if(normalDate.before(new Date())){
                attendance.setState("LATE");
            }else{
                attendance.setState("NORMAL");
            }
            attendance.setIp(getIp());
            attendance.setWeek(ToolOS.getWeek());
            attendanceMapper.insert(attendance);
            return GlobalData.CONFIGPARAMETERS.get("signSuccess")==null?Const.SIGNSUCCESS:GlobalData.CONFIGPARAMETERS.get("signSuccess");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String updateAttendance() {
        //签退时根据日期和用户查询，当天签退了再次签到时更新签退时间
        Wrapper<Attendance> wrapper = new EntityWrapper<>();
        wrapper = wrapper.where("date_format(SIGNIN_TIME ,'%Y-%m-%d' ) = {0}",DateUtil.formatDate(new Date(), "yyyy-MM-dd"));
        wrapper = wrapper.eq("create_by", ShiroKit.getUser().getUserId());
        List<Attendance> attendanceList = attendanceMapper.selectList(wrapper);
        if(attendanceList != null){
            for(Attendance attendance : attendanceList){
                attendance.setSignoutTime(new Date());
                attendance.setCreateBy(ShiroKit.getUser().getUserId());
                attendanceMapper.updateById(attendance);
            }
        }else{
            Attendance attendance = new Attendance();
            attendance.setSignoutTime(new Date());
            attendance.setCreateBy(ShiroKit.getUser().getUserId());
            attendance.setCreateTime(new Date());
            attendance.setIp(ToolOS.getOsLocalHostIp());
            attendance.setWeek(ToolOS.getWeek());
            attendanceMapper.insert(attendance);
        }
        return GlobalData.CONFIGPARAMETERS.get("signSuccess")==null?Const.SIGNSUCCESS:GlobalData.CONFIGPARAMETERS.get("signSuccess");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertOneAttendance(Attendance attendance) {
        attendanceMapper.insert(attendance);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void exportExcel(String condition, String startTime, String endTime,String exportFlag, HttpServletRequest request, HttpServletResponse response)  {
        Wrapper<AttendanceExport> wrapper = new EntityWrapper<>();
        wrapper.setSqlSelect("week,state,signin_time,signout_time,ip ");
        List<AttendanceExport> list = exportMapper.selectList(wrapper);

        ExcelUtil.writeExcel(response, list, "kqExcel", "sheetkq", new AttendanceExport());

                try {
            // 总记录数
            Integer totalRowCount = attendanceMapper.selectListCount(condition, startTime, endTime);
            log.info("totalRowCount:  " + totalRowCount);
            Integer start = PoiUtil.getStart(totalRowCount);

            // 导出EXCEL文件名称
            String filaName = "考勤明细";
            // 标题
            String[] titles = {"星期", "考勤状态", "签到时间", "签退时间", "ip地址", "姓名"};

            String[] datas = {"week", "stateName", "signinTime", "signoutTime", "ip", "userName"};

            // 开始导入
            PoiUtil.exportExcelToWebsite(start, request, response, totalRowCount, filaName, titles, exportFlag, new WriteExcelDataDelegated() {
                @Override
                public Boolean writeExcelData(SXSSFSheet eachSheet, Integer startRowCount, Integer endRowCount, Integer currentPage, Integer pageSize) throws Exception {
                    Pagination page = new Pagination(currentPage, pageSize);
                    log.info("currentPage:" + currentPage + ",pageSize:" + pageSize);
                    List<Map<String, Object>> result = attendanceMapper.selectExportAll(page, condition,  startTime, endTime);
                    log.info("writeExcelData------------result.size: " + result.size());

                    Boolean isDownload = PoiUtil.writeWorkbook(datas, result, eachSheet, startRowCount, endRowCount);
                    return isDownload;
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    @DataScope
    public List<Map<String, Object>> getList(Attendance attendance,Page<Attendance> page) {
        return attendanceMapper.getList(attendance,page);
    }

    @Override
    public int getCount(String condition, String startTime, String endTime) {
        return attendanceMapper.selectListCount(condition, startTime, endTime);
    }

    @Override
    public List<AttendanceExport> selectExportExcel(String condition, String startTime, String endTime){
        return exportMapper.selectExportExcel(condition,  startTime, endTime);
    }

}
