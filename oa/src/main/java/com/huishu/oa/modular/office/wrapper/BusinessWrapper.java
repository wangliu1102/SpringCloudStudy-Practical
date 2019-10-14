package com.huishu.oa.modular.office.wrapper;

import cn.stylefeng.roses.core.base.warpper.BaseControllerWrapper;
import cn.stylefeng.roses.kernel.model.page.PageResult;
import com.baomidou.mybatisplus.plugins.Page;
import com.huishu.oa.core.common.constant.factory.ConstantFactory;

import java.util.List;
import java.util.Map;

/**
 * 出差
 *
 * @author xf
 * @date 2019年5月16日
 */
public class BusinessWrapper extends BaseControllerWrapper {

    public BusinessWrapper(Map<String, Object> single) {
        super(single);
    }

    public BusinessWrapper(List<Map<String, Object>> multi) {
        super(multi);
    }

    public BusinessWrapper(Page<Map<String, Object>> page) {
        super(page);
    }

    public BusinessWrapper(PageResult<Map<String, Object>> pageResult) {
        super(pageResult);
    }

    @Override
    protected void wrapTheMap(Map<String, Object> map) {
        Integer creater = (Integer) map.get("createUser");
        map.put("createrName", ConstantFactory.me().getUserNameById(creater));
        map.put("customer",ConstantFactory.me().getCustomer((Integer) map.get("customer")));
        map.put("city",ConstantFactory.me().getBusinessCity((Integer) map.get("city")));
    }
}
