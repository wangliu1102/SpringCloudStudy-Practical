package com.huishu.oa.modular.system.factory;

import cn.stylefeng.guns.generator.executor.model.GenQo;
import cn.stylefeng.guns.generator.modular.factory.DefaultTemplateFactory;
import com.huishu.oa.core.util.ToolUtil;


/**
 * 代码生成自定义工厂
 *
 * @author yubb
 * @Date 2019年6月20日16:27:22
 */
public class HuishuTemplateFactory extends DefaultTemplateFactory {
    public HuishuTemplateFactory() {
    }

    public static GenQo getHuishuParams() {
        GenQo genQo = new GenQo();
        //genQo.setProjectPath(ToolUtil.getWebRootPath((String)null));
        genQo.setProjectPath("/home/oa");
        genQo.setAuthor("huishu");
        genQo.setProjectPackage("com.huishu.oa.modular");
        genQo.setCorePackage("com.huishu.oa.core");
        genQo.setIgnoreTabelPrefix("sys_");
        genQo.setModuleName("system");
        genQo.setParentMenuName("系统管理");
        return genQo;
    }

}
