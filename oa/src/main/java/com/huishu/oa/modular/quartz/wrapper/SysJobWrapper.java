package com.huishu.oa.modular.quartz.wrapper;


import cn.stylefeng.roses.core.util.ToolUtil;
import com.huishu.oa.core.common.controller.BaseControllerWrapper;

import java.util.Map;

/**
 * 定时任务 包装类
 * <p>
 * Created by zx
 * Date 2019/01/12 11:51
 * version:1.0
 */
public class SysJobWrapper extends BaseControllerWrapper {

    public SysJobWrapper(Object obj) {
        super(obj);
    }

    @Override
    protected void warpTheMap(Map<String, Object> map) {
        String misfirePolicy = (String) map.get("misfirePolicy");
        String status = (String) map.get("status");
        String concurrent = (String) map.get("concurrent");

        if (ToolUtil.isNotEmpty(misfirePolicy)) {
            if (misfirePolicy.equals("0")) {
                map.put("misfirePolicyName", "默认策略");
            } else if (misfirePolicy.equals("1")) {
                map.put("misfirePolicyName", "立即执行");
            } else if (misfirePolicy.equals("2")) {
                map.put("misfirePolicyName", "执行一次");
            } else if (misfirePolicy.equals("3")) {
                map.put("misfirePolicyName", "放弃执行");
            } else {
                map.put("misfirePolicyName", "");
            }
        } else {
            map.put("misfirePolicyName", "");
        }

        if (ToolUtil.isNotEmpty(status)) {
            if (status.equals("0")) {
                map.put("statusName", "正常");
            } else if (status.equals("1")) {
                map.put("statusName", "暂停");
            } else {
                map.put("statusName", "");
            }
        } else {
            map.put("statusName", "");
        }

        if (ToolUtil.isNotEmpty(concurrent)) {
            if (concurrent.equals("0")) {
                map.put("concurrentName", "允许");
            } else if (concurrent.equals("1")) {
                map.put("concurrentName", "禁止");
            } else {
                map.put("concurrentName", "");
            }
        } else {
            map.put("concurrentName", "");
        }

    }
}
