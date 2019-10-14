package com.huishu.oa.modular.system.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.huishu.oa.modular.system.dao.ConfigMapper;
import com.huishu.oa.modular.system.model.Config;
import com.huishu.oa.modular.system.service.IConfigService;

/**
 * <P>
 * 配置表 服务实现类
 * <P>
 * 
 * @author lyf
 * @since 2019-06-17
 */
@Service
public class ConfigServiceImpl extends ServiceImpl<ConfigMapper, Config> implements IConfigService{

	@Resource
	private ConfigMapper configMapper;

	@Override
	public List<Config> configList(Page<Config> page, String configName, String configKey, String configType, String startTime,
			String endTime) {
		return this.configMapper.configList(page, configName, configKey, configType, startTime, endTime);
	}


}
