package com.huishu.oa.modular.quartz.controller;

import cn.stylefeng.roses.core.base.controller.BaseController;
import cn.stylefeng.roses.core.reqres.response.ErrorResponseData;
import cn.stylefeng.roses.core.reqres.response.ResponseData;
import cn.stylefeng.roses.kernel.model.exception.ServiceException;
import com.alibaba.fastjson.JSONObject;
import com.huishu.oa.core.common.annotation.BusinessLog;
import com.huishu.oa.core.common.annotation.Permission;
import com.huishu.oa.core.common.constant.dictmap.SysJobDict;
import com.huishu.oa.core.common.exception.BizExceptionEnum;
import com.huishu.oa.core.log.LogObjectHolder;
import com.huishu.oa.core.shiro.ShiroKit;
import com.huishu.oa.modular.quartz.wrapper.SysJobWrapper;
import com.huishu.oa.modular.quartz.model.SysJob;
import com.huishu.oa.modular.quartz.service.ISysJobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 调度任务信息操作处理
 * <p>
 * Created by zx
 * Date 2019/01/12 10:11
 * version:1.0
 */
@Controller
@RequestMapping("/quartz/job")
public class SysJobController extends BaseController {

    private String PREFIX = "/quartz/job/";

    @Autowired
    private ISysJobService jobService;

    /**
     * @Description 跳转到调度任务信息列表
     * @auther zx
     * @date 2019/01/12 11:58
     * @params []
     */
    @RequestMapping("")
    public String job() {
        return PREFIX + "job.html";
    }

    /**
     * @Description 查询调度任务信息列表
     * @auther zx
     * @date 2019/01/12 14:26
     * @params [jobName, status, methodName]
     */
    @RequestMapping("/list")
    @Permission
    @ResponseBody
    public Object list(@RequestParam(required = false) String jobName, @RequestParam(required = false) String status,
                       @RequestParam(required = false) String methodName) {
        List<Map<String, Object>> list = jobService.selectJobList(jobName, status, methodName);
        return new SysJobWrapper(list).warp();
    }

    /**
     * @Description 任务调度状态修改
     * @auther zx
     * @date 2019/01/12 11:58
     * @params []
     */
    @BusinessLog(value = "定时任务调度状态修改", key = "jobId", dict = SysJobDict.class)
    @RequestMapping("/changeStatus")
    @Permission
    @ResponseBody
    public ResponseData changeStatus(SysJob job) {

            LogObjectHolder.me().set(job);
            job.setUpdateBy(ShiroKit.getUser().getAccount());
            job.setUpdateTime(new Date());
            int tip = 0;
            try {
                tip = jobService.changeStatus(job);
            }catch (Exception e){
                e.printStackTrace();
            }
            if (tip > 0) {
                return SUCCESS_TIP;
            } else {
                return new ErrorResponseData(500, "操作失败");
            }

    }

    /**
     * @Description 任务调度立即执行一次
     * @auther zx
     * @date 2019/01/12 11:58
     * @params []
     */
    @BusinessLog(value = "定时任务立即执行一次", key = "jobId", dict = SysJobDict.class)
    @Permission
    @RequestMapping("/run")
    @ResponseBody
    public ResponseData run(SysJob job) {

        int tip = 0 ;
        try{
            tip = jobService.run(job);
        }catch (Exception e){
            e.printStackTrace();
        }
        if (tip > 0) {
            return SUCCESS_TIP;
        } else {
            return new ErrorResponseData(500, "操作失败");
        }
    }

    /**
     * @Description 跳转到新增调度页面
     * @auther zx
     * @date 2019/01/12 12:07
     * @params []
     */
    @GetMapping("/job_add")
    public String add() {
        return PREFIX + "job_add.html";
    }

    /**
     * @Description 新增保存调度
     * @auther zx
     * @date 2019/01/12 12:07
     * @params []
     */
    @BusinessLog(value = "新增定时任务", key = "jobName,methodName,status,remark", dict = SysJobDict.class)
    @Permission
    @PostMapping("/add")
    @ResponseBody
    public ResponseData addSave(@Valid SysJob job, BindingResult result) {
        if (result.hasErrors()) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        job.setCreateBy(ShiroKit.getUser().getAccount());
        job.setCreateTime(new Date());
        try{
            jobService.insertJobCron(job);
        }catch (Exception e){
            e.printStackTrace();
        }
        return SUCCESS_TIP;
    }

    /**
     * @Description 跳转到修改调度页面
     * @auther zx
     * @date 2019/01/12 12:14
     * @params [jobId, mmap]
     */
    @RequestMapping("/job_edit/{jobId}")
    public String edit(@PathVariable("jobId") Integer jobId, Model model) {
        SysJob job = jobService.selectById(jobId);
        model.addAttribute("job", job);
        LogObjectHolder.me().set(job);
        return PREFIX + "job_edit.html";
    }

    /**
     * @Description 修改保存调度
     * @auther zx
     * @date 2019/01/12 12:16
     * @params [job]
     */
    @BusinessLog(value = "修改定时任务", key = "jobName,methodName,status,remark", dict = SysJobDict.class)
    @Permission
    @PostMapping("/edit")
    @ResponseBody
    public ResponseData editSave(@Valid SysJob job, BindingResult result) {
        if (result.hasErrors()) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        job.setUpdateBy(ShiroKit.getUser().getAccount());
        job.setUpdateTime(new Date());
        try{
            jobService.updateJobCron(job);
        }catch (Exception e){
            e.printStackTrace();
        }
        return SUCCESS_TIP;
    }

    /**
     * @Description 校验cron表达式是否有效
     * @auther zx
     * @date 2019/01/12 12:18
     * @params [job]
     */
    @PostMapping("/checkCronExpressionIsValid")
    @ResponseBody
    public Object checkCronExpressionIsValid(SysJob job) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("valid", jobService.checkCronExpressionIsValid(job.getCronExpression()));
        return jsonObject;
    }
}
