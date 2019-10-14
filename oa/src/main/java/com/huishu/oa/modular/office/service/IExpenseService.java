package com.huishu.oa.modular.office.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.service.IService;
import com.huishu.oa.modular.office.model.Expense;

/**
 * 报销管理 服务类
 *
 * @author zx
 * @date 2019-05-14
 */
public interface IExpenseService extends IService<Expense> {

    /**
     * 获取报销列表
     */


	List<Map<String, Object>> selectExpense(Expense expense);
}
