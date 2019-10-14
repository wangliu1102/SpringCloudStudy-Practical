package com.huishu.oa.core.common.constant.dictmap;

import com.huishu.oa.core.common.constant.dictmap.base.AbstractDictMap;

/**
 * 出差的映射
 *
 * @author yubb
 * @date 2017-05-06 15:01
 */
public class BusinessDict extends AbstractDictMap {

    @Override
    public void init() {
        put("businessId", "标题id");
        put("title", "名称");
        put("customer", "客户");
        put("city", "目的城市");
        put("reason", "事由");
        put("startTime", "开始时间");
        put("endTime", "结束时间");
        put("id", "出差id");
    }

    @Override
    protected void initBeWrapped() {
    }
}
