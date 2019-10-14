package com.huishu.oa.modular.office.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.huishu.oa.modular.office.model.Overtime;
import com.huishu.oa.modular.office.model.Weekly;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 加班表 Mapper 接口
 * </p>
 *
 * @author lyf
 * @since 2019-06-03
 */
public interface OvertimeMapper extends BaseMapper<Overtime> {

    /**
     * 获取周报列表
     */
    Page<Map<String, Object>> list(@Param("page") Page page, @Param("condition") String condition);
    /**
     * 分页查询
     *
     * @param page
     * @return
     */
    List<Overtime> overtimeList(@Param("page") Page<Overtime> page, @Param("condition") String condition, @Param("startTime") String startTime, @Param("endTime") String endTime);


}