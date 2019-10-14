package com.huishu.oa.core.common.constant.dictmap;

import com.huishu.oa.core.common.constant.dictmap.base.AbstractDictMap;

/**
 * 部门的映RFRF射
 *
 * @author sf
 * @date 2019-05-10 15:01
 */
public class OverTimeDict extends AbstractDictMap {
	 @Override
	    public void init() {
	        put("id", "加班id");
	        put("type", "类型");
	        put("title", "名称");
	        put("content", "内容");
	        put("startTime", "开始时间");
	        put("endTime", "结束时间");
	    }

	    @Override
	    protected void initBeWrapped() {
	    	 putFieldWrapperMethodName("id", "getOverTimeName");
	    }

}
