package com.huishu.oa.modular.system.wrapper;

import cn.stylefeng.roses.core.base.warpper.BaseControllerWrapper;
import cn.stylefeng.roses.kernel.model.page.PageResult;
import com.baomidou.mybatisplus.plugins.Page;
import com.huishu.oa.core.common.constant.factory.ConstantFactory;
import com.huishu.oa.core.util.ToolUtil;

import java.util.List;
import java.util.Map;

/**
 * 用户管理的包装类
 *
 * @author zx
 * @Date 2019年2月13日 下午10:47:03
 */
public class UserWrapper extends BaseControllerWrapper {

    public UserWrapper(Map<String, Object> single) {
        super(single);
    }

    public UserWrapper(List<Map<String, Object>> multi) {
        super(multi);
    }

    public UserWrapper(Page<Map<String, Object>> page) {
        super(page);
    }

    public UserWrapper(PageResult<Map<String, Object>> pageResult) {
        super(pageResult);
    }

    @Override
    protected void wrapTheMap(Map<String, Object> map) {
        map.put("sexName", ConstantFactory.me().getSexName((Integer) map.get("sex")));
        map.put("roleName", ConstantFactory.me().getRoleName((String) map.get("roleId")));
        if (ToolUtil.isNotEmpty(map.get("deptId"))) {
            map.put("deptName", ConstantFactory.me().getDeptName(Integer.parseInt(map.get("deptId").toString())));
        } else {
            map.put("deptName", "");
        }
        map.put("statusName", ConstantFactory.me().getStatusName((Integer) map.get("status")));
    }

}
