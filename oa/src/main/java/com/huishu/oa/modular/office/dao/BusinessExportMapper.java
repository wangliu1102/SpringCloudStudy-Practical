package com.huishu.oa.modular.office.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.huishu.oa.modular.office.model.BusinessExport;

public interface BusinessExportMapper extends BaseMapper<BusinessExport>{
	 
	List<BusinessExport> selectExportExcel(@Param("condition") String condition,
               @Param("startTime") String startTime, @Param("endTime") String endTime);
}
