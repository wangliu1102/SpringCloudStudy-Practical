package com.huishu.oa.core.common.constant.dictmap;

import com.huishu.oa.core.common.constant.dictmap.base.AbstractDictMap;

/**
 * 通知的映射
 *
 * @author zx
 * @Date 2019-05-06 15:01
 */
public class NoticeMap extends AbstractDictMap {

    @Override
    public void init() {
        put("title", "标题");
        put("content", "内容");
    }

    @Override
    protected void initBeWrapped() {
    }
}
