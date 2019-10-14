package com.huishu.oa.modular.office.wrapper;

import cn.stylefeng.roses.core.base.warpper.BaseControllerWrapper;
import cn.stylefeng.roses.kernel.model.page.PageResult;
import com.baomidou.mybatisplus.plugins.Page;

import java.util.List;
import java.util.Map;

/**
 * 日志列表的包装类
 *
 * @author yubb
 * @Date 2019年4月5日22:56:24
 */
public class AttendanceWrapper extends BaseControllerWrapper {

    public AttendanceWrapper(Map<String, Object> single) {
        super(single);
    }

    public AttendanceWrapper(List<Map<String, Object>> multi) {
        super(multi);
    }

    public AttendanceWrapper(Page<Map<String, Object>> page) {
        super(page);
    }

    public AttendanceWrapper(PageResult<Map<String, Object>> pageResult) {
        super(pageResult);
    }

    @Override
    protected void wrapTheMap(Map<String, Object> map) {
    }
}
