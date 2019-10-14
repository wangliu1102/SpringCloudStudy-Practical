package com.huishu.oa.modular;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;
import com.huishu.oa.core.common.annotation.Excel;
import com.huishu.oa.core.common.annotation.Excel.Type;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Author: zx
 * Date: Created in 2019/01/13 9:50
 * Copyright: Copyright (c) 2019
 * Description： 基础类
 */
@Data
public abstract class BaseModel<T extends BaseModel> extends Model<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    protected Integer id;

    /**
     * 创建时间（create_time）
     */
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss", type = Type.EXPORT)
    protected Date createTime;

    /**
     * 创建人,对应userId (create_by)
     */
    protected String createBy;

    /**
     * 修改时间 (update_time)
     */
    protected Date updateTime;

    /**
     * 修改人,对应userId (update_by)
     */
    protected String updateBy;

    /**
     * 请求参数
     */
    @TableField(exist = false)
    private Map<String, Object> params;


    public Map<String, Object> getParams() {
        if (params == null) {
            params = new HashMap<>();
        }
        return params;
    }


    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
