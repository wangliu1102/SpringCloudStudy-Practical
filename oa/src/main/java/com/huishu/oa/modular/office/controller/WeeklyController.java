package com.huishu.oa.modular.office.controller;

import cn.stylefeng.roses.core.base.controller.BaseController;
import cn.stylefeng.roses.core.util.ToolUtil;
import cn.stylefeng.roses.kernel.model.exception.ServiceException;
import com.baomidou.mybatisplus.plugins.Page;
import com.huishu.oa.core.common.annotation.BusinessLog;
import com.huishu.oa.core.common.constant.dictmap.WeeklyDict;
import com.huishu.oa.core.common.constant.factory.PageFactory;
import com.huishu.oa.core.common.exception.BizExceptionEnum;
import com.huishu.oa.core.common.page.PageInfoBT;
import com.huishu.oa.core.log.LogObjectHolder;
import com.huishu.oa.core.shiro.ShiroKit;
import com.huishu.oa.core.util.DateTimeKit;
import com.huishu.oa.core.util.DateUtil;
import com.huishu.oa.core.util.ReplaceForStringUtil;
import com.huishu.oa.modular.office.model.Weekly;
import com.huishu.oa.modular.office.service.IWeeklyService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * 周报控制器
 *
 * @author lzl
 * @Date 2019年6月5日11:00:22
 */
@Controller
@RequestMapping("/weekly")
public class WeeklyController extends BaseController {

    private String PREFIX = "/office/weekly/";

    @Autowired
    private IWeeklyService weeklyService;

    /**
     * 跳转到周报管理首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "weekly.html";
    }

    /**
     * 跳转到添加周报
     */
    @RequestMapping("/weekly_add")
    public String weeklyAdd(Model model) {
        //工作周报标题
        String title= DateUtil.formatDate(DateTimeKit.getWeekMonday(),"yyyy-MM-dd")+"到"+DateUtil.formatDate(DateTimeKit.getWeekFriday(),"yyyy-MM-dd")+"工作周报";
        model.addAttribute("title", title);
        return PREFIX + "weekly_add.html";
    }

    /**
     * 跳转到修改周报
     */
    @RequestMapping("/weekly_update/{id}")
    public String weeklyUpdate(@PathVariable Long id, Model model) {
        Weekly weekly = new Weekly();
        if (id != null) {
            weekly = weeklyService.selectById(id);
        }
        model.addAttribute("weekly", weekly);
        LogObjectHolder.me().set(weekly);
        return PREFIX + "weekly_edit.html";
    }

    /**
     * 跳转到查看周报详情
     */
    @RequestMapping("/weekly_detail/{id}")
    public String weeklyDetail(@PathVariable Long id, Model model) {
        Weekly weekly = new Weekly();
        if (id != null) {
            weekly = weeklyService.selectById(id);
        }
        model.addAttribute("weekly", weekly);
        return PREFIX + "weekly_detail.html";
    }

    /**
     * 新增周报
     */
    @BusinessLog(value = "添加周报", key = "id", dict = WeeklyDict.class)
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(Weekly weekly) {
        if (ToolUtil.isOneEmpty(weekly, weekly.getTitle(), weekly.getContent())) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        //替换特殊字符
        weekly.setContent(ReplaceForStringUtil.replace(weekly.getContent()));
        weekly.setCreateBy(ShiroKit.getUser().getId().toString());
        weekly.setCreateTime(new Date());
        weekly.setUpdateBy(ShiroKit.getUser().getId().toString());
        weekly.setUpdateTime(new Date());
        weekly.insert();
        return SUCCESS_TIP;
    }

    /**
     * 获取周报列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition, String startTime, String endTime) {
        Page<Weekly> page = new PageFactory<Weekly>().defaultPage();
        List<Weekly> result = this.weeklyService.weeklyList(page, condition, startTime, endTime);
        page.setRecords(result);
        return new PageInfoBT<>(page);
    }

    /**
     * 周报详情
     */
    @RequestMapping(value = "/detail/{id}")
    @ResponseBody
    public Object detail(@PathVariable("id") Long id) {
        return weeklyService.selectById(id);
    }

    /**
     * 修改周报
     */
    @BusinessLog(value = "修改周报", key = "id", dict = WeeklyDict.class)
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(Weekly weekly) {
        if (ToolUtil.isOneEmpty(weekly, weekly.getId(), weekly.getTitle(), weekly.getContent())) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        //替换特殊字符
        weekly.setContent(ReplaceForStringUtil.replace(weekly.getContent()));
        weekly.setUpdateBy(ShiroKit.getUser().getId().toString());
        weekly.setUpdateTime(new Date());
        this.weeklyService.updateById(weekly);
        return SUCCESS_TIP;
    }

    /**
     * 删除周报
     */
    @BusinessLog(value = "删除周报", key = "id", dict = WeeklyDict.class)
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam String id) {
        if (StringUtils.isEmpty(id)) {
            throw new ServiceException(BizExceptionEnum.DB_RESOURCE_NULL);
        }
        Weekly weekly = new Weekly().selectById(id);
        if (ToolUtil.isNotEmpty(weekly)) {
            if (ShiroKit.isAdmin() || weekly.getCreateBy().equals(ShiroKit.getUser().getUserId())) {
                weeklyService.deleteById(Integer.parseInt(id));
            } else {
                throw new ServiceException(BizExceptionEnum.NO_PERMITION);
            }
        }
        return SUCCESS_TIP;
    }

}
