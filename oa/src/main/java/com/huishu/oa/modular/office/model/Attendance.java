package com.huishu.oa.modular.office.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.huishu.oa.modular.BaseModel;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 考勤表 实体类
 * <p>
 * Created by yubb
 * Date 2019/5/13 14:08
 * version:1.0
 */
@Data
@TableName("t_attendance")
public class Attendance extends BaseModel<Attendance> {

    /**
     * 星期
     */
    private String week;

    /**
     * 状态：正常、旷工、请假
     */
    private String state;

    /**
     * 签到时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date signinTime;

    /**
     * 签退时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date signoutTime;

    /**
     * IP地址
     */
    private String ip;

    /**
     * 名字
     */
    @TableField(exist = false)
    private String name;

    /**
     * 开始时间
     */
    @TableField(exist = false)
    private String startTime;

    /**
     * 结束时间
     */
    @TableField(exist = false)
    private String endTime;


}
