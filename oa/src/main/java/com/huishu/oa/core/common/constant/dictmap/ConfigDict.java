package com.huishu.oa.core.common.constant.dictmap;

import com.huishu.oa.core.common.constant.dictmap.base.AbstractDictMap;

/**
 * 参数映射
 * 
 * @author lyf
 * @since 2019-06-20
 */
public class ConfigDict extends AbstractDictMap {

	@Override
	public void init() {
		put("id","参数主键");
		put("configName","参数名称");
		put("configKey","参数键名");
		put("congfigType","系统内置");
		put("remark","备注");	
		
	}

	@Override
	protected void initBeWrapped() {
		
	}
	

}
