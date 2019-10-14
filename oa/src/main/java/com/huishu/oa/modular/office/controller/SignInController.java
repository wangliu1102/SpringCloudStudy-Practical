package com.huishu.oa.modular.office.controller;

import cn.stylefeng.roses.core.base.controller.BaseController;
import cn.stylefeng.roses.core.reqres.response.ResponseData;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.huishu.oa.core.common.annotation.Permission;
import com.huishu.oa.core.common.constant.factory.PageFactory;
import com.huishu.oa.core.common.page.PageInfoBT;
import com.huishu.oa.core.excel.ExcelUtil;
import com.huishu.oa.core.shiro.ShiroKit;
import com.huishu.oa.core.util.DateUtil;
import com.huishu.oa.core.util.ToolUtil;
import com.huishu.oa.modular.office.model.Attendance;
import com.huishu.oa.modular.office.model.AttendanceExport;
import com.huishu.oa.modular.office.service.IAttendanceService;
import com.huishu.oa.modular.office.wrapper.AttendanceWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cn.stylefeng.roses.core.util.HttpContext.getIp;

/**
 * 签到签退的控制器
 *
 * @author yubb
 * @date 2019年5月5日 19:45:36
 */
@Slf4j
@Controller
@RequestMapping("/signIn")
public class SignInController extends BaseController {

    private static String PREFIX = "/office/attendance/";

    @Autowired
    private IAttendanceService IAttendanceService;

    /**
     * 跳转到考勤列表页
     */
    @RequestMapping("/attendance")
    public String index() {
        return PREFIX + "attendance.html";
    }

    /**
     * 跳转到打卡页
     */
    @RequestMapping("")
    public String index(Model model) {
        Wrapper<Attendance> wrapper = new EntityWrapper<>();
        wrapper = wrapper.where("date_format(SIGNIN_TIME ,'%Y-%m-%d' ) = {0}", DateUtil.formatDate(new Date(), "yyyy-MM-dd"));
        wrapper = wrapper.eq("create_by", ShiroKit.getUser().getUserId());
        Attendance attendance = IAttendanceService.selectOne(wrapper);
        log.info("attendance=" + attendance);
        String signInTime = "";
        String signOutTime = "";
        String state = "";
        if (ToolUtil.isNotEmpty(attendance)) {
            if (attendance.getSigninTime() != null) {
                signInTime = DateUtil.getTime(attendance.getSigninTime());
            }
            if (attendance.getSignoutTime() != null) {
                signOutTime = DateUtil.getTime(attendance.getSignoutTime());
            }
            if (StringUtils.isNotEmpty(attendance.getState())) {
                state = attendance.getState();
            }
        }
        model.addAttribute("signInTime", signInTime);
        model.addAttribute("signOutTime", signOutTime);
        model.addAttribute("state", state);
        return PREFIX + "sign_in.html";
    }


    /**
     * 签到
     */
    @RequestMapping(value = "/signForIn")
    @ResponseBody
    public ResponseData signForIn() {
        Map<String, Object> messageMap = new HashMap<>();
        String message = IAttendanceService.insertAttendance();
        messageMap.put("message", message);
        return ResponseData.success(messageMap);
    }

    /**
     * 签退
     */
    @RequestMapping(value = "/signForOut")
    @ResponseBody
    public ResponseData signForOut() {
        Map<String, Object> messageMap = new HashMap<>();
        String message = IAttendanceService.updateAttendance();
        messageMap.put("message", message);
        return ResponseData.success(messageMap);
    }

    /**
     * 获取考勤列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(Attendance attendance) {
        Page<Attendance> page = new PageFactory<Attendance>().defaultPage();
        List<Map<String, Object>> result = IAttendanceService.getList(attendance,page);
        page.setRecords(new AttendanceWrapper(result).wrap());
        return new PageInfoBT<>(page);

    }

    /**
     * 获取考勤总条数
     */
    @RequestMapping(value = "/getCount")
    @ResponseBody
    public Object getCount(@RequestParam(required = false) String condition, @RequestParam(required = false) String startTime, @RequestParam(required = false) String endTime) {
        int result = IAttendanceService.getCount(condition, startTime, endTime);
        return result;
    }


    /**
     * 导出到Excel
     */
    @RequestMapping("/exportAll")
    @Permission
    public void exportExcel(HttpServletRequest request, HttpServletResponse response,
                            @RequestParam(required = false) String condition,
                            @RequestParam(required = false) String startTime,
                            @RequestParam(required = false) String endTime) {
        getSession().setAttribute("exportFlag", "true");
        try {
            String filaName = "考勤明细";
            List<AttendanceExport> list = IAttendanceService.selectExportExcel(condition, startTime, endTime);
            ExcelUtil.writeExcel(response, list, filaName, "考勤明细", new AttendanceExport());
            request.getSession().removeAttribute("exportFlag");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 判断是否导出excel完成，用于loading的显示和隐藏
     */
    @RequestMapping("/isExport")
    @ResponseBody
    public Object isExport() {
        JSONObject jsonObject = new JSONObject();
        if (getSession().getAttribute("exportFlag") == null) {
            jsonObject.put("resultCode", 0);
        } else {
            jsonObject.put("resultCode", -1);
        }
        return jsonObject;
    }


}
