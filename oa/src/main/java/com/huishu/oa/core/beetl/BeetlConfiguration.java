package com.huishu.oa.core.beetl;

import cn.stylefeng.roses.core.util.SpringContextHolder;
import com.huishu.oa.config.properties.OaProperties;
import com.huishu.oa.core.tag.DictSelectorTag;
import com.huishu.oa.core.util.KaptchaUtil;
import com.huishu.oa.core.util.ToolUtil;
import org.beetl.core.Context;
import org.beetl.core.Function;
import org.beetl.ext.spring.BeetlGroupUtilConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * beetl拓展配置,绑定一些工具类,方便在模板中直接调用
 *
 * @author zx
 * @Date 2019/2/22 21:03
 */
public class BeetlConfiguration extends BeetlGroupUtilConfiguration {

    @Autowired
    private Environment env;

    @Autowired
    private DictSelectorTag dictSelectorTag;

    @Override
    public void initOther() {
        //全局共享变量
        Map<String, Object> shared = new HashMap<>();
        shared.put("version", SpringContextHolder.getBean(OaProperties.class).getVersion());
        groupTemplate.setSharedVars(shared);
        //全局共享方法
        groupTemplate.registerFunctionPackage("shiro", new ShiroExt());
        groupTemplate.registerFunctionPackage("tool", new ToolUtil());
        groupTemplate.registerFunctionPackage("kaptcha", new KaptchaUtil());
        groupTemplate.registerTagFactory("dictSelector", () -> dictSelectorTag);

        groupTemplate.registerFunction("env", new Function() {
            @Override
            public String call(Object[] paras, Context ctx) {
                String key = (String) paras[0];
                String value = env.getProperty(key);
                if (value != null) {
                    return getStr(value);
                }
                if (paras.length == 2) {
                    return (String) paras[1];
                }
                return null;
            }

            String getStr(String str) {
                try {
                    return new String(str.getBytes("iso8859-1"), StandardCharsets.UTF_8);
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}
