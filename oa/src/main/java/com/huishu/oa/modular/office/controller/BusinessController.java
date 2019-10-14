package com.huishu.oa.modular.office.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.plugins.Page;
import com.huishu.oa.core.common.annotation.BusinessLog;
import com.huishu.oa.core.common.annotation.Permission;
import com.huishu.oa.core.common.constant.dictmap.BusinessDict;
import com.huishu.oa.core.common.constant.factory.ConstantFactory;
import com.huishu.oa.core.common.constant.factory.PageFactory;
import com.huishu.oa.core.common.exception.BizExceptionEnum;
import com.huishu.oa.core.common.page.PageInfoBT;
import com.huishu.oa.core.excel.ExcelUtil;
import com.huishu.oa.core.log.LogObjectHolder;
import com.huishu.oa.core.shiro.ShiroKit;
import com.huishu.oa.modular.office.model.AttendanceExport;
import com.huishu.oa.modular.office.model.Business;
import com.huishu.oa.modular.office.model.BusinessExport;
import com.huishu.oa.modular.office.service.IBusinessService;
import com.huishu.oa.modular.office.wrapper.BusinessWrapper;

import cn.stylefeng.roses.core.base.controller.BaseController;
import cn.stylefeng.roses.core.reqres.response.ResponseData;
import cn.stylefeng.roses.core.util.ToolUtil;
import cn.stylefeng.roses.kernel.model.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;

/**
 * 出差控制器
 *
 * @author yubb
 * @Date 2017-05-09 23:02:21
 */
@Slf4j
@Controller
@RequestMapping("/business")
public class BusinessController extends BaseController {

    private static String PREFIX = "/office/business/";

    @Autowired
    private IBusinessService businessService;

    /**
     * 跳转到出差列表首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "business.html";
    }

    /**
     * 跳转到添加出差
     */
    @RequestMapping("/business_add")
    public String businessAdd() {
        return PREFIX + "business_add.html";
    }

    /**
     * 跳转到修改出差
     */
    @RequestMapping("/business_update/{id}")
    public String businessUpdate(@PathVariable Long id, Model model) {
        Business business = businessService.selectById(id);
        if(business!=null) {
            model.addAttribute(business);
            LogObjectHolder.me().set(business);
        }
        return PREFIX + "business_edit.html";
    }

    /**
     * 获取出差列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(Business business) {
        Page<Business> page = new PageFactory<Business>().defaultPage();
        List<Map<String, Object>> result = businessService.list(business,page);
        page.setRecords(new BusinessWrapper(result).wrap());
        return new PageInfoBT<>(page);
    }


    /**
     * 获取出差总条数
     */
    @RequestMapping(value = "/getCount")
    @ResponseBody
    public Object getCount(@RequestParam(required = false) String condition, @RequestParam(required = false) String startTime, @RequestParam(required = false) String endTime) {
        int result = businessService.getCount(condition, startTime, endTime);
        return result;
    }
    /**
     * 导出Excel
     * 
     */
    @RequestMapping("/exportAll")
    @Permission
    public void exportExcel(HttpServletRequest request, HttpServletResponse response,
                            @RequestParam(required = false) String condition,
                            @RequestParam(required = false) String startTime,
                            @RequestParam(required = false) String endTime) {
        getSession().setAttribute("exportFlag", "true");
        try {
            String filaName = "出差管理";
            List<BusinessExport> list = businessService.selectExportExcel(condition, startTime, endTime);
            ExcelUtil.writeExcel(response, list, filaName, "出差管理", new BusinessExport());
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

    
    
    
    /**
     * 新增出差
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    @BusinessLog(value = "新增出差", key = "title,customer", dict = BusinessDict.class)
    public ResponseData add(@Valid Business business) {
        if (ToolUtil.isOneEmpty(business.getTitle(), business.getCustomer())) {
            throw new ServiceException(BizExceptionEnum.NULL_BY_PARAMS);
        }
        business.setCreateBy(ShiroKit.getUser().getUserId());
        business.setCreateTime(new Date());
        businessService.insert(business);
        return SUCCESS_TIP;
    }

    /**
     * 删除出差
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    @BusinessLog(value = "删除出差",  key = "id", dict = BusinessDict.class)
    public Object delete(@RequestParam Integer id) {
        //缓存出差名称
        LogObjectHolder.me().set(ConstantFactory.me().getBusinessTitle(id));
        this.businessService.deleteById(id);
        return SUCCESS_TIP;
    }

    /**
     * 修改出差
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    @BusinessLog(value = "修改出差", key = "title,customer", dict = BusinessDict.class)
    public Object update(Business business) {
        if (ToolUtil.isOneEmpty(business.getId(), business.getTitle(), business.getCustomer())) {
            throw new ServiceException(BizExceptionEnum.NULL_BY_PARAMS);
        }
        Business old = businessService.selectById(business.getId());
        old.setTitle(business.getTitle());
        old.setCustomer(business.getCustomer());
        old.setCity(business.getCity());
        old.setReason(business.getReason());
        old.setStartTime(business.getStartTime());
        old.setEndTime(business.getEndTime());
        this.businessService.updateById(old);
        return SUCCESS_TIP;
    }

}
