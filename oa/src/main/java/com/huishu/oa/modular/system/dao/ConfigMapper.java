package com.huishu.oa.modular.system.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.huishu.oa.modular.system.model.Config;



/**
 * <p>
 * 配置表 Mapper 接口
 * </p>
 * 
 * @author lyf
 * @since 2019-06-14
 */
public interface ConfigMapper extends BaseMapper<Config> {
	/**
     * 分页查询
     *
     * @param page
     * @return
     */
	List<Config> configList(@Param("page") Page<Config> page, @Param("configName") String configName, @Param("configKey") String configKey,
			@Param("configType") String configType, @Param("startTime") String startTime, @Param("endTime") String endTime );


}
