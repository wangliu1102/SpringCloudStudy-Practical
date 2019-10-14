package com.huishu.oa.modular.office.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.huishu.oa.modular.office.model.Leave;
import com.huishu.oa.modular.office.model.Weekly;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface IWeeklyService extends IService<Weekly> {

    /**
     * 添加周报
     * @param weekly
     */
    void addWeekly(@Param("weekly") Weekly weekly);

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
