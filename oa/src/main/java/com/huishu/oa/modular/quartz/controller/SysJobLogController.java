package com.huishu.oa.modular.quartz.controller;

import cn.stylefeng.roses.core.base.controller.BaseController;
import cn.stylefeng.roses.core.reqres.response.ResponseData;
import com.huishu.oa.core.common.annotation.BusinessLog;
import com.huishu.oa.core.common.annotation.Permission;
import com.huishu.oa.core.common.constant.dictmap.SysJobLogDict;
import com.huishu.oa.modular.quartz.service.ISysJobLogService;
import com.huishu.oa.modular.quartz.wrapper.SysJobLogWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 调度日志操作处理
 * <p>
 * Created by zx
 * Date 2019/01/12 10:11
 * version:1.0
 */
@Controller
@RequestMapping("/quartz/jobLog")
public class SysJobLogController extends BaseController {

    private String PREFIX = "/quartz/jobLog/";

    @Autowired
    private ISysJobLogService jobLogService;

    /**
     * @Description 跳转到调度日志列表
     * @auther zx
     * @date 2019/01/12 11:58
     * @params []
     */
    @GetMapping("")
    public String jobLog() {
        return PREFIX + "jobLog.html";
    }

    /**
     * @Description 查询调度日志列表
     * @auther zx
     * @date 2019/01/12 14:26
     * @params [jobName, status, methodName, beginTime, endTime]
     */
    @PostMapping("/list")
    @Permission
    @ResponseBody
    public Object list(@RequestParam(required = false) String jobName, @RequestParam(required = false) String status,
                       @RequestParam(required = false) String methodName, @RequestParam(required = false) String beginTime,
                       @RequestParam(required = false) String endTime) {
        List<Map<String, Object>> list = jobLogService.selectJobLogList(jobName, status, methodName, beginTime, endTime);
        return new SysJobLogWrapper(list).warp();
    }


    /**
     * @Description 删除调度日志（批量）
     * @auther zx
     * @date 2019/01/12 14:31
     * @params [ids]
     */
    @BusinessLog(value = "删除定时任务调度日志", key = "ids", dict = SysJobLogDict.class)
    @Permission
    @PostMapping("/remove")
    @ResponseBody
    public ResponseData remove(String ids) {
        jobLogService.deleteJobLogByIds(ids);
        return SUCCESS_TIP;
    }

    /**
     * @Description
     * @auther zx
     * @date 2019/01/12 14:31
     * @params []
     */
    @BusinessLog(value = "清空定时任务调度日志", key = "id", dict = SysJobLogDict.class)
    @Permission
    @PostMapping("/clean")
    @ResponseBody
    public ResponseData clean() {
        jobLogService.cleanJobLog();
        return SUCCESS_TIP;
    }

    /**
     * @Description 日志详情
     * @auther zx
     * @date 2019/01/12 14:51
     * @params [jobLogId, model]
     */
    @Permission
    @GetMapping("/detail/{jobLogId}")
    public String detail(@PathVariable("jobLogId") Long jobLogId, Model model) {
        model.addAttribute("jobLog", jobLogService.selectById(jobLogId));
        return PREFIX + "detail.html";
    }
}
