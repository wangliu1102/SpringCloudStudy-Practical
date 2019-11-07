package com.wl.springcloud.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.util.Date;

/**
 * @description  〈权限实体〉
 * @author 王柳
 * @date 2019/11/7 9:43
 */
@Data
@TableName("es_permission")
public class Permission extends Model<Permission> {
    @TableId
    private int id;
    private String zuulPrefix;
    private String servicePrefix;
    private String method;
    private String uri;
    private Date createTime;
    private Date updateTime;
}
