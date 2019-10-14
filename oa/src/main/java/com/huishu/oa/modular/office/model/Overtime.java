package com.huishu.oa.modular.office.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.huishu.oa.modular.BaseModel;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author lyf
 */
@Data
@TableName("t_overtime")
public class Overtime extends BaseModel<Overtime> {

    /**
     * 标题
     */
    private String title;

    /**
     * 加班类型
     */
    private String type;

    /**
     * 内容
     */
    private String content;

    /**
     * 开始时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    /**
     * 结束时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    /**
     * 加班时长
     */
    @TableField("hours")
    private Long hours;

}

