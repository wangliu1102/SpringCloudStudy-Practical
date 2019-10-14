package com.huishu.oa.core.util;

/**
 * 字符串替换特殊字符工具类
 * <p>
 * Created by zx
 * Date 2019/03/20 16:21
 */
public class ReplaceForStringUtil {

    public static String replace(String value) {
        return value.replace("& #40;", "(").
                replace("& #41;", ")").
                replace("& #39;", "'").
                replace("& lt;","<").
                replace("& gt;",">");
    }
}
