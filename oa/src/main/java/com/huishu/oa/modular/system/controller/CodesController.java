package com.huishu.oa.modular.system.controller;

import cn.stylefeng.roses.core.base.controller.BaseController;
import com.huishu.oa.modular.system.factory.HuishuTemplateFactory;
import com.huishu.oa.modular.system.service.TablesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 代码生成控制器
 *
 * @author yubb
 * @Date 2019年6月20日16:27:22
 */
@Controller
@RequestMapping("/codes")
public class CodesController extends BaseController {

    private static String PREFIX = "/code";
    @Autowired
    private TablesService tablesService;

    @RequestMapping("")
    public String blackboard(Model model) {
        model.addAttribute("tables", this.tablesService.getAllTables());
        model.addAttribute("params", HuishuTemplateFactory.getHuishuParams());
        model.addAttribute("templates", HuishuTemplateFactory.getDefaultTemplates());
        return PREFIX + "/code.html";
    }

}
