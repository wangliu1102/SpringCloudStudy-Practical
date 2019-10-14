package com.huishu.oa.modular.system.model;

import com.baomidou.mybatisplus.annotations.TableName;
import com.huishu.oa.modular.BaseModel;

import lombok.Data;

/**
 * @author lyf
 * @since 2019-06-13
 */
@Data
@TableName("sys_config")
public class Config extends BaseModel<Config>{
    /**
     * 参数名称
     */
    private String configName;

    /**
     * 参数键名
     */
    private String configKey;

    /**
     * 参数键值
     */
    private String configValue;

    /**
     * 系统内置（Y是 N否）
     */
    private String configType;
    
    /**
     * 备注
     */
    private String remark;
    

}
