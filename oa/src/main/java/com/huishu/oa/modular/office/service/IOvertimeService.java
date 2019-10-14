package com.huishu.oa.modular.office.service;

import java.util.List;

import com.huishu.oa.modular.office.model.Overtime;
import com.huishu.oa.modular.office.model.Weekly;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;


/**
 * <p>
 * 加班表 服务类
 * </p>
 * @author lyf
 */
public interface IOvertimeService extends IService<Overtime> {

    /**
     * 分页查询
     * @param page
     * @return
     */
    List<Overtime> overtimeList(@Param("page") Page<Overtime> page, @Param("condition") String condition, @Param("startTime") String startTime, @Param("endTime") String endTime);

}