package com.huishu.oa.core.common.constant.dictmap;

import com.huishu.oa.core.common.constant.dictmap.base.AbstractDictMap;

/**
 * 调度日志信息业务日志包装类
 *
 * Created by zx
 * Date 2019/01/12 15:01
 * version:1.0
 */
public class SysJobLogDict extends AbstractDictMap {

    @Override
    public void init() {
        put("id","定时任务调度日志id");
        put("ids","定时任务调度日志id集合");
    }

    @Override
    protected void initBeWrapped() {
    }
}
