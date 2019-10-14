package com.huishu.oa.modular.office.controller;

import cn.stylefeng.roses.core.base.controller.BaseController;
import cn.stylefeng.roses.core.util.ToolUtil;
import cn.stylefeng.roses.kernel.model.exception.ServiceException;
import com.baomidou.mybatisplus.plugins.Page;
import com.huishu.oa.core.common.annotation.BusinessLog;
import com.huishu.oa.core.common.constant.dictmap.OverTimeDict;
import com.huishu.oa.core.common.constant.factory.PageFactory;
import com.huishu.oa.core.common.exception.BizExceptionEnum;
import com.huishu.oa.core.common.page.PageInfoBT;
import com.huishu.oa.core.shiro.ShiroKit;
import com.huishu.oa.core.util.DateUtil;
import com.huishu.oa.modular.office.model.Overtime;
import com.huishu.oa.modular.office.model.Weekly;
import com.huishu.oa.modular.office.service.IOvertimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.List;

/**
 * 加班控制器
 *
 * @author lyf
 * 2019年6月13日
 */
@Controller
@RequestMapping("/overtime")
public class OvertimeController extends BaseController {

    private String PREFIX = "/office/overtime/";

    @Autowired
    private IOvertimeService IOvertimeService;

    /**
     * 跳转到加班管理首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "overtime.html";
    }

    /**
     * 跳转到添加加班
     */
    @RequestMapping("/overtime_add")
    public String businessAdd() {
        return PREFIX + "overtime_add.html";
    }

    /**
     * 跳转到修改加班
     */
    @RequestMapping("/overtime_update/{id}")
    public String businessUpdate(@PathVariable Long id, Model model) {
        Overtime overtime = IOvertimeService.selectById(id);
        model.addAttribute("OverTime", overtime);
        return PREFIX + "overtime_edit.html";
    }

    /**
     * 获取周报列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition, String startTime, String endTime) {
        Page<Overtime> page = new PageFactory<Overtime>().defaultPage();
        List<Overtime> result = this.IOvertimeService.overtimeList(page, condition, startTime, endTime);
        page.setRecords(result);
        return new PageInfoBT<>(page);
    }
    /**
     * 新增加班
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    @BusinessLog(value = "新增加班", key = "id", dict = OverTimeDict.class)
    public Object add(Overtime overtime) {
        if (ToolUtil.isOneEmpty(overtime.getEndTime(), overtime.getStartTime(), overtime.getContent(),
                overtime.getTitle())) {
            throw new ServiceException(BizExceptionEnum.DB_RESOURCE_NULL);
        }
        overtime.setCreateBy(ShiroKit.getUser().getId().toString());
        overtime.setCreateTime(new Date());
        hoursSet(overtime);
        this.IOvertimeService.insert(overtime);
        return SUCCESS_TIP;

    }

    /**
     * 删除加班
     */
    @BusinessLog(value = "删除加班", key = "id", dict = OverTimeDict.class)
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Long id) {
        if (id == null) {
            throw new ServiceException(BizExceptionEnum.DB_RESOURCE_NULL);
        }

        Overtime overtime = IOvertimeService.selectById(id);

        IOvertimeService.deleteById(overtime);
        return SUCCESS_TIP;
    }

    /**
     * 修改加班
     */
    @BusinessLog(value = "修改加班", key = "id", dict = OverTimeDict.class)
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(Overtime overtime) {
        if (ToolUtil.isOneEmpty(overtime.getId(), overtime.getTitle(), overtime.getContent())) {
            throw new ServiceException(BizExceptionEnum.DB_RESOURCE_NULL);
        }
        Overtime ot = this.IOvertimeService.selectById(overtime.getId());
        ot.setTitle(overtime.getTitle());
        ot.setType(overtime.getType());
        ot.setContent(overtime.getContent());
        ot.setStartTime(overtime.getStartTime());
        ot.setEndTime(overtime.getEndTime());
        hoursSet(ot);
        this.IOvertimeService.updateById(ot);
        return SUCCESS_TIP;
    }

    /**
     * 配置hours
     */
    private void hoursSet(Overtime overtime) {
        long differenceMillisecond = DateUtil.getDifferHoursOrMilliseconds(overtime.getStartTime(),
                overtime.getEndTime(), 1);
        long differenceDays = DateUtil.getDifferHoursOrMilliseconds(overtime.getStartTime(), overtime.getEndTime(), 2);
        long halfHours = 1000 * 60 * 30;
        if (differenceMillisecond >= halfHours) {
            overtime.setHours(differenceDays + 1);
        } else {
            overtime.setHours(differenceDays);
        }
    }

}
