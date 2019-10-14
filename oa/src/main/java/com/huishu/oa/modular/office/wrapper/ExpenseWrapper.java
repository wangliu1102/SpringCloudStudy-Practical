package com.huishu.oa.modular.office.wrapper;

import cn.stylefeng.roses.core.base.warpper.BaseControllerWrapper;
import cn.stylefeng.roses.kernel.model.page.PageResult;
import com.baomidou.mybatisplus.plugins.Page;
import com.huishu.oa.core.common.constant.factory.ConstantFactory;

import java.util.List;
import java.util.Map;

/**
 * 报销列表的包装
 *
 * @author zx
 * @Date 2019年4月25日 18:10:31
 */
public class ExpenseWrapper extends BaseControllerWrapper {

    public ExpenseWrapper(Map<String, Object> single) {
        super(single);
    }

    public ExpenseWrapper(List<Map<String, Object>> multi) {
        super(multi);
    }

    public ExpenseWrapper(Page<Map<String, Object>> page) {
        super(page);
    }

    public ExpenseWrapper(PageResult<Map<String, Object>> pageResult) {
        super(pageResult);
    }

    @Override
    protected void wrapTheMap(Map<String, Object> map) {
        Integer createBy = Integer.parseInt(map.get("createBy").toString());
        map.put("createName", ConstantFactory.me().getUserNameById(createBy));
    }
}
