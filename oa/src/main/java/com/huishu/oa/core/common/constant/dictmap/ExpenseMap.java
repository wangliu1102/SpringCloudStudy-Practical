package com.huishu.oa.core.common.constant.dictmap;

import com.huishu.oa.core.common.constant.dictmap.base.AbstractDictMap;

/**
 * 报销的映射
 *
 * @author zx
 * @date 2019-05-06 15:01
 */
public class ExpenseMap extends AbstractDictMap {

    @Override
    public void init() {
        put("expenseId", "标题id");
        put("title", "标题");
        put("money", "金额");
        put("comment", "备注");
        put("attachment", "附件");
    }

    @Override
    protected void initBeWrapped() {
    }
}
