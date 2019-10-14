package com.huishu.oa.core.util;

import com.huishu.oa.config.properties.OaProperties;
import cn.stylefeng.roses.core.util.SpringContextHolder;

/**
 * 验证码工具类
 */
public class KaptchaUtil {

    /**
     * 获取验证码开关
     */
    public static Boolean getKaptchaOnOff() {
        return SpringContextHolder.getBean(OaProperties.class).getKaptchaOpen();
    }
}