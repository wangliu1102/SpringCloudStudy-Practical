package com.huishu.oa.core.common.constant.dictmap;

import com.huishu.oa.core.common.constant.dictmap.base.AbstractDictMap;

/**
 * 部门的映射
 *
 * @author sf
 * @date 2019-05-10 15:01
 */
public class LeaveDict extends AbstractDictMap {

    @Override
    public void init() {
        put("id","请假id");
        put("title", "标题");
        put("content", "事由");
        put("startTime", "开始时间");
        put("endTime", "结束时间");
    }

    @Override
    protected void initBeWrapped() {
        putFieldWrapperMethodName("id", "getLeaveName");
    }
}
