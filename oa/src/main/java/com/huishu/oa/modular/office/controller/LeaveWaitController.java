package com.huishu.oa.modular.office.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baomidou.mybatisplus.plugins.Page;
import com.huishu.oa.core.common.annotation.BusinessLog;
import com.huishu.oa.core.common.annotation.Permission;
import com.huishu.oa.core.common.constant.Const;
import com.huishu.oa.core.common.constant.dictmap.LeaveDict;
import com.huishu.oa.core.common.constant.factory.PageFactory;
import com.huishu.oa.core.common.constant.state.LeaveStatus;
import com.huishu.oa.core.common.constant.state.ManagerStatus;
import com.huishu.oa.core.common.exception.BizExceptionEnum;
import com.huishu.oa.core.common.page.PageInfoBT;
import com.huishu.oa.core.shiro.ShiroKit;
import com.huishu.oa.modular.office.model.Leave;
import com.huishu.oa.modular.office.service.ILeaveWaitService;
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
@RequestMapping("/leaveWait")
public class LeaveWaitController extends BaseController {

    private String PREFIX = "/office/leave/";

    @Autowired
    private ILeaveWaitService leaveWaitService;
    
 
    /**
     * 跳转到请假待批准首页
     */
    @RequestMapping("")
    public String leaveWait() {
        return PREFIX + "leave_wait.html";
    }
 
 
    /**
     * 跳转到查看请假详情
     */
    @RequestMapping("/leave_detail/{id}")
    public String leaveDetail(@PathVariable Long id, Model model) {
        Leave leave = new Leave();
        if (id != null) {
            leave = leaveWaitService.selectById(id);
        }
        model.addAttribute("leave", leave);
        return PREFIX + "leave_detail.html";
    }
    
 
    /**
     * 获取请假列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(Leave leave) {
        Page<Leave> page = new PageFactory<Leave>().defaultPage();
        List<Map<String, Object>> result = this.leaveWaitService.leaveWaitList(leave,page);
        page.setRecords(new LeaveWrapper(result).wrap());
        return new PageInfoBT<>(page);
    }

    /**
     * 请假详情
     */
    @RequestMapping(value = "/detail/{id}")
    @ResponseBody
    public Object detail(@PathVariable("id") Long id) {
        return leaveWaitService.selectById(id);
    }

    /**
     * 批准请假
     * caowy
     * 2019/9/20
     */
    @RequestMapping("/agree")
    @BusinessLog(value = "批准请假", key = "leaveId", dict = LeaveDict.class)
    @Permission(Const.ADMIN_NAME)
    @ResponseBody
    public ResponseData agree(@RequestParam Integer leaveId) {
        if (ToolUtil.isEmpty(leaveId)) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        this.leaveWaitService.setStatus(leaveId, LeaveStatus.AGREE.getCode());
        return SUCCESS_TIP;
    }

    /**
     * 请假驳回
     * caowy
     * 2019/9/20
     */
    @RequestMapping("/unagree")
    @BusinessLog(value = "请假驳回", key = "leaveId", dict = LeaveDict.class)
    @Permission(Const.ADMIN_NAME)
    @ResponseBody
    public ResponseData unagree(@RequestParam Integer leaveId) {
        if (ToolUtil.isEmpty(leaveId)) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        this.leaveWaitService.setStatus(leaveId, LeaveStatus.UNAGREE.getCode());
        return SUCCESS_TIP;
    }

    
	

	
	
	
	
}
