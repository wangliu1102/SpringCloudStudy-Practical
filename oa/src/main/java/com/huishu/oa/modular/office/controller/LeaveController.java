package com.huishu.oa.modular.office.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baomidou.mybatisplus.plugins.Page;
import com.huishu.oa.core.common.GlobalData;
import com.huishu.oa.core.common.annotation.BusinessLog;
import com.huishu.oa.core.common.constant.Const;
import com.huishu.oa.core.common.constant.dictmap.LeaveDict;
import com.huishu.oa.core.common.constant.factory.ConstantFactory;
import com.huishu.oa.core.common.constant.factory.PageFactory;
import com.huishu.oa.core.common.exception.BizExceptionEnum;
import com.huishu.oa.core.common.page.PageInfoBT;
import com.huishu.oa.core.log.LogObjectHolder;
import com.huishu.oa.core.shiro.ShiroKit;
import com.huishu.oa.core.util.DateUtil;
import com.huishu.oa.core.util.poi.ExcelUtil;
import com.huishu.oa.modular.office.model.Leave;
import com.huishu.oa.modular.office.service.ILeaveService;
import com.huishu.oa.modular.office.wrapper.LeaveWrapper;

import cn.stylefeng.roses.core.base.controller.BaseController;
import cn.stylefeng.roses.core.reqres.response.ResponseData;
import cn.stylefeng.roses.core.util.ToolUtil;
import cn.stylefeng.roses.kernel.model.exception.ServiceException;

/**
 * 请假控制器
 *
 * @author tb
 * @Date 2019年6月3日10:27:22
 */
@Controller
@RequestMapping("/leave")
public class LeaveController extends BaseController {

    private String PREFIX = "/office/leave/";



    @Autowired
    private ILeaveService leaveService;


    /**
     * 跳转到请假管理首页
     */

    @RequestMapping("")
    public String index() {
        return PREFIX + "leave.html";
    }

    
    /**
     * 跳转到添加请假
     */
    @RequestMapping("/leave_add")
    public String leaveAdd() {
        return PREFIX + "leave_add.html";
    }

    /**
     * 跳转到修改请假
     */
    @RequestMapping("/leave_update/{id}")
    public String supplierUpdate(@PathVariable Integer id, Model model) {
        Leave leave = leaveService.selectById(id);
        model.addAttribute("leave", leave);
        LogObjectHolder.me().set(leave);
        return PREFIX + "leave_edit.html";
    }
 
   
    /**
     * 新增请假
     */
    @BusinessLog(value = "添加请假", key = "title", dict = LeaveDict.class)
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(Leave leave){
        if (ToolUtil.isOneEmpty(leave.getEndTime(), leave.getStartTime(), leave.getContent(), leave.getTitle())) {
            throw new ServiceException(BizExceptionEnum.NULL_BY_PARAMS);
        }
        long day= DateUtil.getDaySub(leave.getStartTime(),leave.getEndTime());
        double hour=DateUtil.getDifferHours(leave.getStartTime(),leave.getEndTime());
        double config = GlobalData.CONFIGPARAMETERS.get("four")==null? Const.FOUR : Double.parseDouble(GlobalData.CONFIGPARAMETERS.get("four"));
        if(day>= Const.ZERO && hour<= config && hour>Const.ZERO){
           leave.setDays(day+Const.HALF_DAY);
        }else if(day>=Const.ZERO && hour>config){
           leave.setDays(day+Const.ONE_DAY);
        }

        leave.setCreateBy(ShiroKit.getUser().getUserId());
        this.leaveService.addLeave(leave);
        return SUCCESS_TIP;
    }


    /**
     * 获取请假列表
     */
    /**
     * 获取请假列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(Leave leave) {
        Page<Leave> page = new PageFactory<Leave>().defaultPage();
        List<Map<String, Object>> result = this.leaveService.leaveList(leave,page);
        page.setRecords(new LeaveWrapper(result).wrap());
        return new PageInfoBT<>(page);
    }

    /**
     * 修改请假
     */
    @BusinessLog(value = "修改请假", key = "title,startTime,endTime,content", dict = LeaveDict.class)
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(Leave leave){
        if (ToolUtil.isOneEmpty(leave.getTitle(),leave.getStartTime(),leave.getEndTime(),leave.getContent(),leave.getId())) {
            throw new ServiceException(BizExceptionEnum.NULL_BY_PARAMS);
        }
        leave.setUpdateTime(new Date());
        leave.setUpdateBy(ShiroKit.getUser().getUserId());
        long day= DateUtil.getDaySub(leave.getStartTime(),leave.getEndTime());
        double hour=DateUtil.getDifferHours(leave.getStartTime(),leave.getEndTime());
        double config = GlobalData.CONFIGPARAMETERS.get("four")==null? Const.FOUR : Double.parseDouble(GlobalData.CONFIGPARAMETERS.get("four"));
        if(day>= Const.ZERO && hour<= config && hour>Const.ZERO){
            leave.setDays(day+Const.HALF_DAY);
        }else if(day>=Const.ZERO && hour>config){
            leave.setDays(day+Const.ONE_DAY);
        }

        this.leaveService.updateById(leave);
        return SUCCESS_TIP;
    }

    /**
     * 删除请假
     */
    @BusinessLog(value = "删除请假", key = "leaveId", dict = LeaveDict.class)
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer leaveId) {
        //缓存请假名称
        LogObjectHolder.me().set(ConstantFactory.me().getLeaveName(leaveId));
        leaveService.deleteById(leaveId);
        return SUCCESS_TIP;
    }

	 /**
     * 导出excel
     */
    @PostMapping("/exportExcel")
    @ResponseBody
    public ResponseData exportExcel() {
        List<Leave> list = leaveService.selectList(null);
        ExcelUtil<Leave> util = new ExcelUtil<>(Leave.class);
        return util.exportExcel(list, "用户数据");
    }
	
	
	
	
	
}
