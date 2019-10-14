package com.huishu.oa.modular.office.wrapper;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.plugins.Page;

import cn.stylefeng.roses.core.base.warpper.BaseControllerWrapper;
import cn.stylefeng.roses.kernel.model.page.PageResult;

/**
 * 加班
 */
public class OverTimeWrapper extends BaseControllerWrapper {

    public OverTimeWrapper(Map<String, Object> single) {
        super(single);
    }

    public OverTimeWrapper(List<Map<String, Object>> multi) {
        super(multi);
    }

    public OverTimeWrapper(Page<Map<String, Object>> page) {
        super(page);
    }

    public OverTimeWrapper(PageResult<Map<String, Object>> pageResult) {
        super(pageResult);
    }
    
	@Override
    protected void wrapTheMap(Map<String, Object> map) {
       
    }  
}
