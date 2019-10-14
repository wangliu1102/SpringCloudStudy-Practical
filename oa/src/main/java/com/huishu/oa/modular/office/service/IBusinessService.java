package com.huishu.oa.modular.office.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.huishu.oa.modular.office.model.Business;
import com.huishu.oa.modular.office.model.BusinessExport;

/**
 * <p>
 * 工作出差表 服务实现类
 * </p>
 *
 * @author yubb
 * @since 2019-05-16
 */

public interface IBusinessService  extends IService<Business> {

    /**
     * 获取出差列表
     *
     * @author yubb
     * @Date 2018/12/23 6:05 PM
     */
    List<Map<String, Object>> list(Business business,Page<Business> page);
    
    /**
     * 查询总条数
     * @author caowy
     * @Date 2019/09/17 11:34
     */
    int getCount(String condition, String startTime, String endTime);
	
    List<BusinessExport> selectExportExcel(String condition, String startTime, String endTime);
    
    /**
     * 导出到excel
     * @author caowy
     * @Date 2019/09/17 11:36
     */
    void exportExcel(String condition, String startTime, String endTime,String exportFlag, HttpServletRequest request, HttpServletResponse response);
    
 
    
}

