package com.huishu.oa.modular.office.model;

import com.baomidou.mybatisplus.annotations.TableName;
import com.huishu.oa.core.common.annotation.Excel;
import com.huishu.oa.modular.BaseModel;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @author zx
 */
@Data
@TableName("t_leave")
public class Leave extends BaseModel<Leave> {

    private static final long serialVersionUID = 1L;

    /**
     * 标题
     */
    private String title;
    
    
    /**
     * 部门id
     */
     private Integer deptId;

    /**
     * 内容
     */
    private String content;

    
    
    /**
     * 请假类型
     */
    private Integer leaveType;

    /**
     * 开始时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String startTime;

    /**
     * 结束时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String endTime;

    /**
     * 请假天数
     */
    private double days;
    
    /**
     * caowy
     * 2019/9/25
      * 状态(1：待审核(默认状态)  2：同意  3：驳回）
     */
    private Integer status=1 ;
    
    
    public static long getSerialVersionUID() {
        return serialVersionUID;
    }




	
    
}
