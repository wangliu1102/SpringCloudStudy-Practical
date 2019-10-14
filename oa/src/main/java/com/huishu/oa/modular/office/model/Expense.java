package com.huishu.oa.modular.office.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.huishu.oa.modular.BaseModel;
import lombok.Data;

/**
 * <p>
 * 报销表
 * </p>
 *
 * @author zx
 * @since 2019-05-16
 */
@Data
@TableName("t_expense")
public class Expense extends BaseModel<Expense> {

    private static final long serialVersionUID = 1L;

    /**
     * 标题
     */
    private String title;

    /**
     * 金额
     */
    private Double money;

    /**
     * 备注
     */
    private String comment;

    /**
     * 附件
     */
    private String attachment;

    /**
     * 开始时间
     */
    @TableField(exist = false)
    private String beginTime;

    /**
     * 结束时间
     */
    @TableField(exist = false)
    private String endTime;

}
