package com.huishu.oa.modular.office.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.huishu.oa.modular.office.model.Business;

/**
 * <p>
 * 出差表 Mapper 接口
 * </p>
 *
 * @author yubb
 * @since 2018-12-07
 */

public interface BusinessMapper extends BaseMapper<Business> {

    /**
     * 获取出差列表
     */
    List<Map<String, Object>> list(@Param("business") Business business,@Param("page")Page page);
     
    
    /**
     * 获取导出列表总数
     */
    Integer selectListCount(@Param("condition") String condition,
                            @Param("startTime") String startTime, @Param("endTime") String endTime);

    /**
     * 获取导出列表
     */
    List<Map<String, Object>> selectExportAll(@Param("page") Pagination page, @Param("condition") String condition,
                                              @Param("startTime") String startTime, @Param("endTime") String endTime);

}
