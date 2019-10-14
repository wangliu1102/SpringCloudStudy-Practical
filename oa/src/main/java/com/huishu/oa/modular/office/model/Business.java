package com.huishu.oa.modular.office.model;

import com.baomidou.mybatisplus.annotations.TableName;
import com.huishu.oa.modular.BaseModel;
import lombok.Data;

/**
 * <p>
 * 出差表
 * </p>
 *
 * @author xf
 * @since 2019-05-16
 */
@Data
@TableName("t_business")
public class Business extends BaseModel<Business> {

    /**
     * 标题
     */
    private String title;
    /**
     * 客户
     */
    private Integer customer;
    /**
     * 目的城市
     */
    private Integer city;
    /**
     * 事由
     */
    private String reason;
    /**
     * 开始时间
     */
    private String startTime;
    /**
     * 结束时间
     */
    private String endTime;

    /**
     * 姓名
     */
    private String name;



}
