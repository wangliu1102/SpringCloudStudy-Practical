package com.huishu.oa.modular.office.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.huishu.oa.modular.office.model.Attendance;
import com.huishu.oa.modular.office.model.Leave;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 请假表 Mapper 接口
 * </p>
 *
 * @author tb
 * @since 2019-06-03
 */
public interface LeaveMapper extends BaseMapper<Leave> {
    /**
     * 分页查询
     *
     * @param page
     * @return
     */
	 List<Map<String, Object>> leaveList(@Param("leave")Leave leave,@Param("page")Page<Leave> page);

    /**
     * 获取请假列表
     * caowy
     */
    Page<Map<String, Object>> list(@Param("page") Page page,
                                   @Param("condition") String condition);
}
