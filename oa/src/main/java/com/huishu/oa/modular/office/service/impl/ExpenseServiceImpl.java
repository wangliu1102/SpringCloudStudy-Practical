package com.huishu.oa.modular.office.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.huishu.oa.core.common.annotion.DataScope;
import com.huishu.oa.modular.office.dao.ExpenseMapper;
import com.huishu.oa.modular.office.model.Expense;
import com.huishu.oa.modular.office.service.IExpenseService;
import com.huishu.oa.modular.system.model.User;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 工作出差表 服务实现类
 * </p>
 *
 * @author zx
 * @since 2019-05-16
 */
@Service
public class ExpenseServiceImpl extends ServiceImpl<ExpenseMapper, Expense> implements IExpenseService {

	@Override
    public List<Map<String, Object>> selectExpense(Expense expense) {
        return this.baseMapper.selectExpense(expense);
    }
}
