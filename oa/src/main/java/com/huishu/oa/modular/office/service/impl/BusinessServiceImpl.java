package com.huishu.oa.modular.office.service.impl;

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
import com.huishu.oa.core.excel.ExcelUtil;
import com.huishu.oa.core.util.poi.PoiUtil;
import com.huishu.oa.core.util.poi.WriteExcelDataDelegated;
import com.huishu.oa.modular.office.dao.BusinessExportMapper;
import com.huishu.oa.modular.office.dao.BusinessMapper;
import com.huishu.oa.modular.office.model.Business;
import com.huishu.oa.modular.office.model.BusinessExport;
import com.huishu.oa.modular.office.service.IBusinessService;
import com.huishu.oa.modular.system.dao.DictMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class BusinessServiceImpl extends ServiceImpl<BusinessMapper, Business> implements IBusinessService {

    @Resource
    private BusinessMapper businessMapper;

    @Resource
    private BusinessExportMapper exportMapper;
    
    @Resource
    private DictMapper dictMapper;

  
    
    
    @Override
    @com.huishu.oa.core.common.annotion.DataScope
    public List<Map<String, Object>> list(Business business,Page<Business> page) {
        return businessMapper.list(business,page);
    }


	@Override
	public int getCount(String condition, String startTime, String endTime) {		
		return businessMapper.selectListCount(condition, startTime, endTime);
	}
	
	 @Override
	    public List<BusinessExport> selectExportExcel(String condition, String startTime, String endTime){
	        return exportMapper.selectExportExcel(condition,  startTime, endTime);
	 }

	

    /**
     *  导出
     * 
     */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void exportExcel(String condition, String startTime, String endTime, String exportFlag,
			HttpServletRequest request, HttpServletResponse response) {
		Wrapper<BusinessExport>wrapper = new EntityWrapper<>();
		wrapper.setSqlSelect("title,customer,city,reason,startTime,endTime,createBy,createTime");
		List<BusinessExport>list = exportMapper.selectList(wrapper);
		ExcelUtil.writeExcel(response, list, "kqExcel", "sheetkq", new BusinessExport());
		 try {
			 //总记录数
		 Integer totalRowCount = businessMapper.selectListCount(condition, startTime, endTime);
		 log.info("totalRowCount: "+ totalRowCount);
		 Integer start = PoiUtil.getStart(totalRowCount);

         // 导出EXCEL文件名称
         String filaName = "出差管理";
         // 标题
         String[] titles = {"标题", "客户/供应商", "目标城市", "事由","开始时间", "结束时间","发布者","创建时间" };

         String[] datas = {"title", "customer", "city","reason","startTime", "endTime", "createBy","createTime"};

         // 开始导入
         PoiUtil.exportExcelToWebsite(start, request, response, totalRowCount, filaName, titles, exportFlag, new WriteExcelDataDelegated() {
             @Override
             public Boolean writeExcelData(SXSSFSheet eachSheet, Integer startRowCount, Integer endRowCount, Integer currentPage, Integer pageSize) throws Exception {
                 Pagination page = new Pagination(currentPage, pageSize);
                 log.info("currentPage:" + currentPage + ",pageSize:" + pageSize);
                 List<Map<String, Object>> result = businessMapper.selectExportAll(page, condition,  startTime, endTime);
                 log.info("writeExcelData------------result.size: " + result.size());

                 Boolean isDownload = PoiUtil.writeWorkbook(datas, result, eachSheet, startRowCount, endRowCount);
                 return isDownload;
             }
         });
     }catch (Exception e){
         e.printStackTrace();
     }
 }


	
}