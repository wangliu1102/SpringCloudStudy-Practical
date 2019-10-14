package com.huishu.oa.modular.quartz.wrapper;



import cn.stylefeng.roses.core.util.ToolUtil;
import com.huishu.oa.core.common.controller.BaseControllerWrapper;

import java.util.Map;

/**
 * 调度信息日志包装类
 * <p>
 * Created by zx
 * Date 2019/01/12 14:23
 * version:1.0
 */
public class SysJobLogWrapper extends BaseControllerWrapper {

    public SysJobLogWrapper(Object obj) {
        super(obj);
    }

    @Override
    protected void warpTheMap(Map<String, Object> map) {
        String status = (String) map.get("status");
        String concurrent = (String) map.get("concurrent");
        if (ToolUtil.isNotEmpty(status)) {
            if (status.equals("0")) {
                map.put("statusName", "成功");
            } else if (status.equals("1")) {
                map.put("statusName", "失败");
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
