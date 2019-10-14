package com.huishu.oa.modular.office.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.huishu.oa.modular.office.model.Weekly;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 工作周报表 Mapper 接口
 * </p>
 *
 * @author zx
 * @since 2018-12-07
 */
public interface WeeklyMapper extends BaseMapper<Weekly> {

    /**
     * 获取周报列表
     */
    Page<Map<String, Object>> list(@Param("page") Page page, @Param("condition") String condition);

    /**
     * 分页查询
     * @param page
     * @return
     */
    List<Weekly> weeklyList(@Param("page") Page<Weekly> page, 
    		                @Param("condition") String condition,
    		                @Param("startTime") String startTime,
    		                @Param("endTime") String endTime);


}
