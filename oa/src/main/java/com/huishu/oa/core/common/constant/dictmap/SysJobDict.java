package com.huishu.oa.core.common.constant.dictmap;


import com.huishu.oa.core.common.constant.dictmap.base.AbstractDictMap;

/**
 * 调度信息业务日志包装类
 *
 * Created by zx
 * Date 2019/01/12 14:58
 * version:1.0
 */
public class SysJobDict extends AbstractDictMap {

    @Override
    public void init() {
        put("id","定时任务信息");
        put("jobId","定时任务id");
        put("jobName","任务名称");
        put("methodName","方法名称");
        put("status","任务状态（0正常 1暂停）");
        put("remark","备注");
    }

    @Override
    protected void initBeWrapped() {
        //putFieldWrapperMethodName("id","getSysJobInfoById");
    }
}
