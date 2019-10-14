package com.huishu.oa.modular.office.wrapper;

import cn.stylefeng.roses.core.base.warpper.BaseControllerWrapper;
import cn.stylefeng.roses.kernel.model.page.PageResult;
import com.baomidou.mybatisplus.plugins.Page;
import com.huishu.oa.core.common.constant.factory.ConstantFactory;

import java.util.List;
import java.util.Map;

/**
 * 请假
 *
 * @author xf
 * @date 2019年5月16日
 */
public class LeaveWaitWrapper extends BaseControllerWrapper {

    public LeaveWaitWrapper(Map<String, Object> single) {
        super(single);
    }

    public LeaveWaitWrapper(List<Map<String, Object>> multi) {
        super(multi);
    }

    public LeaveWaitWrapper(Page<Map<String, Object>> page) {
        super(page);
    }

    public LeaveWaitWrapper(PageResult<Map<String, Object>> pageResult) {
        super(pageResult);
    }

    @Override
    protected void wrapTheMap(Map<String, Object> map) {
        map.put("leaveType",ConstantFactory.me().getLeaveType((Integer) map.get("leaveType")));
    }
}
