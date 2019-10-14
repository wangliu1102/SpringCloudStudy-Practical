package com.huishu.oa.modular.office.model;

import com.baomidou.mybatisplus.annotations.TableName;
import com.huishu.oa.modular.BaseModel;
import lombok.Data;

/**
 * 周报表
 * @author zx
 * @since 2019-05-16
 */
@Data
@TableName("t_weekly")
public class Weekly extends BaseModel<Weekly> {

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;
}
