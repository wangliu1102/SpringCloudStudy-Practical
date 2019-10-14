package com.huishu.oa.modular.office.model;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import com.baomidou.mybatisplus.annotations.TableName;
import com.huishu.oa.modular.BaseModel;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 出差表 实体类
 * <p>
 * Created by caowy
 * Date 2019/9/17 14:08
 * version:1.0
 */
@Data
@TableName("t_business")
public class BusinessExport extends BaseRowModel {

    /**
     * 标题
     */
    @ExcelProperty(value = "标题" ,index = 0)
    private String title;

    /**
     * 客户 : 淮南通商银行  芜湖扬子农商行 格林  永鲜100
     */
    @ExcelProperty(value = "客户" ,index = 1)
    private String customer;
    
    /**
     * 目标城市 : 淮南   芜湖  六安  临泉
     */
    @ExcelProperty(value = "目标城市" ,index = 2)
    private String city;
    /**
     * 事由
     */
    @ExcelProperty(value = "事由" ,index = 3)
    private String reason;

    /**
     * 开始时间
     */
    @ExcelProperty(value = "开始时间" ,index = 4)
    private Date startTime;

    /**
     * 结束时间
     */
    @ExcelProperty(value = "结束时间" ,index = 5)
    private Date endTime;

    /**
     * 姓名
     */
    @ExcelProperty(value = "发布人",index = 6)
    private String  createBy;
    
    /**
     * 创建时间
     */
    @ExcelProperty(value = "创建时间",index = 7)
    private Date createTime;
    
    
    
}
