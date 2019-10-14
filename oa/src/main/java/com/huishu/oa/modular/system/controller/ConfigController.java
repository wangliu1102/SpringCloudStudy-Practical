package com.huishu.oa.modular.system.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baomidou.mybatisplus.plugins.Page;
import com.huishu.oa.StartupRunner;
import com.huishu.oa.core.common.annotation.BusinessLog;
import com.huishu.oa.core.common.constant.dictmap.ConfigDict;
import com.huishu.oa.core.common.constant.factory.PageFactory;
import com.huishu.oa.core.common.exception.BizExceptionEnum;
import com.huishu.oa.core.common.page.PageInfoBT;
import com.huishu.oa.core.shiro.ShiroKit;
import com.huishu.oa.modular.system.model.Config;
import com.huishu.oa.modular.system.service.IConfigService;

import cn.stylefeng.roses.core.base.controller.BaseController;
import cn.stylefeng.roses.core.util.ToolUtil;
import cn.stylefeng.roses.kernel.model.exception.ServiceException;

/**
 * 参数设置控制器
 * 
 * 
 * @author lyf
 * @since 2019-06-17
 */
@Controller
@RequestMapping("/config")
public class ConfigController extends BaseController {

	private String PREFIX = "/system/config/";

	@Autowired
	private IConfigService IConfigService;
	@Autowired
	private StartupRunner startupRunner;

	/**
	 * 跳转到参数设置首页
	 */
	@RequestMapping("")
	public String index() {
		return PREFIX + "config.html";
	}

	/**
	 * 跳转到添加参数
	 */
	@RequestMapping("/config_add")
	public String businessAdd() {
		return PREFIX + "config_add.html";
	}

	/**
	 * 跳转到修改参数
	 */
	@RequestMapping("/config_update/{id}")
	public String businessUpdate(@PathVariable Integer id, Model model) {
		Config config = IConfigService.selectById(id);
		model.addAttribute("Config", config);
		return PREFIX + "config_edit.html";
	}

	/**
	 * 获取参数列表
	 * 
	 */
	@RequestMapping(value = "/list")
	@ResponseBody
	public Object list(String configName, String configKey, String configType, String startTime, String endTime) {
		Page<Config> page = new PageFactory<Config>().defaultPage();
		List<Config> result = this.IConfigService.configList(page, configName, configKey, configType, startTime, endTime);
		page.setRecords(result);
		return new PageInfoBT<>(page);
	}

	/**
	 * 新增参数
	 */
	@RequestMapping(value = "/add")
	@ResponseBody
	@BusinessLog(value = "新增参数", key = "id", dict = ConfigDict.class)
	public Object add(Config config) {
		if (ToolUtil.isOneEmpty(config.getConfigName(), config.getConfigKey(), config.getConfigValue(),
				config.getConfigType(),config.getRemark())) {
			throw new ServiceException(BizExceptionEnum.DB_RESOURCE_NULL);
		}
		config.setCreateBy(ShiroKit.getUser().getId().toString());
		config.setCreateTime(new Date());
		this.IConfigService.insert(config);
		startupRunner.flushConfigs();
		return SUCCESS_TIP;

	}

	/**
	 * 删除参数
	 */
	@BusinessLog(value = "删除参数", key = "id", dict = ConfigDict.class)
	@RequestMapping(value = "/delete")
	@ResponseBody
	public Object delete(@RequestParam Integer id) {
		if (id == null) {
			throw new ServiceException(BizExceptionEnum.DB_RESOURCE_NULL);
		}

		Config config = IConfigService.selectById(id);

		IConfigService.deleteById(config);
		startupRunner.flushConfigs();
		return SUCCESS_TIP;
	}

	/**
	 * 修改参数
	 */
	@BusinessLog(value = "修改参数", key = "id", dict = ConfigDict.class)
	@RequestMapping(value = "/update")
	@ResponseBody
	public Object update(Config config) {
		if (ToolUtil.isOneEmpty(config.getId(), config.getConfigName(), config.getConfigKey(), config.getConfigValue(),
				config.getConfigType(),config.getRemark())) {
			throw new ServiceException(BizExceptionEnum.DB_RESOURCE_NULL);
		}
		Config conf = this.IConfigService.selectById(config.getId());
		conf.setConfigName(config.getConfigName());
		conf.setConfigKey(config.getConfigKey());
		conf.setConfigValue(config.getConfigValue());
		conf.setConfigType(config.getConfigType());
		conf.setRemark(config.getRemark());
		this.IConfigService.updateById(conf);
		startupRunner.flushConfigs();
		return SUCCESS_TIP;
	}

}
