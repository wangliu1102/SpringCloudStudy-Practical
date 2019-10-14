package com.huishu.oa.modular.office.model;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
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
public class AttendanceExport extends BaseRowModel {

    /**
     * 星期
     */
    @ExcelProperty(value = "星期" ,index = 0)
    private String week;

    /**
     * 状态：正常、旷工、请假
     */
    @ExcelProperty(value = "考勤状态" ,index = 1)
    private String stateName;

    /**
     * 签到时间
     */
    @ExcelProperty(value = "签到时间" ,index = 2)
    private Date signinTime;

    /**
     * 签退时间
     */
    @ExcelProperty(value = "签退时间" ,index = 3)
    private Date signoutTime;

    /**
     * IP地址
     */
    @ExcelProperty(value = "IP地址" ,index = 4)
    private String ip;
    /**
     * 姓名
     */
    @ExcelProperty(value = "姓名" ,index = 5)
    private String userName;


}
