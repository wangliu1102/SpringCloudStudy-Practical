package com.huishu.oa.modular.office.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.huishu.oa.modular.office.model.Expense;
import com.huishu.oa.modular.office.model.Weekly;
import com.huishu.oa.modular.system.model.User;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 报销表 Mapper 接口
 * </p>
 *
 * @author zx
 * @since 2019-05-07
 */
public interface ExpenseMapper extends BaseMapper<Expense> {

	
	 /**
     * 根据条件查询用户列表
     */
	  List<Map<String, Object>> selectExpense(Expense expense);

	  
	
	  
	  
	  
}
